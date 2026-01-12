package com.preshow.booking.service;

import com.preshow.booking.dto.BookingConfirmedEvent;
import com.preshow.booking.dto.SeatSnapshot;
import com.preshow.booking.enums.BookingStatus;
import com.preshow.booking.exception.CommonException;
import com.preshow.booking.model.Booking;
import com.preshow.booking.model.BookingSeat;
import com.preshow.booking.repository.BookingRepository;
import com.preshow.booking.repository.BookingSeatRepository;
import com.preshow.booking.repository.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final SeatLockService seatLockService;
    private final ApplicationEventPublisher eventPublisher;
    private final ProcessedEventRepository processedEventRepository;

    @Transactional
    public UUID createBooking(UUID userId,UUID showId,List<UUID> seatIds,Map<UUID, SeatSnapshot> seatSnapshotMap){

        List<UUID> lockedSeats = new ArrayList<>();

        try {
            for (UUID seatId : seatIds) {

                //  REDIS LOCK
                boolean locked = seatLockService.lockSeat(showId, seatId, userId);
                if (!locked) {
                    throw new CommonException("Seat already locked", HttpStatus.CONFLICT);
                }

                lockedSeats.add(seatId);
            }

            // CREATE BOOKING
            Booking booking = bookingRepository.save(
                    Booking.builder()
                            .userId(userId)
                            .showId(showId)
                            .status(BookingStatus.INITIATED)
                            .totalAmount(
                                    seatIds.stream()
                                            .map(id -> seatSnapshotMap.get(id).price())
                                            .reduce(BigDecimal.ZERO, BigDecimal::add)
                            )
                            .build()
            );

            // SNAPSHOT BOOKING SEATS
            for (UUID seatId : seatIds) {
                SeatSnapshot seat = seatSnapshotMap.get(seatId);

                bookingSeatRepository.save(
                        BookingSeat.builder()
                                .bookingId(booking.getId())
                                .showSeatId(seatId)
                                .seatNumber(seat.seatNumber())
                                .price(seat.price())
                                .build()
                );
            }

            return booking.getId();

        } catch (Exception ex) {
            // rollback redis locks
            seatLockService.unlockSeats(showId, lockedSeats);
            throw ex;
        }
    }

    // ===================== CONFIRM =====================
    @Transactional
    public void confirmBooking(UUID bookingId, UUID paymentId) {

        // 🔁 IDEMPOTENCY

//        if (processedEventRepository.existsById(eventId)) {
//            return;
//        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow();


        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            return;
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setPaymentId(paymentId);
        bookingRepository.save(booking);

        List<UUID> seatIds =
                bookingSeatRepository.findSeatIdsByBookingId(bookingId);

        // publish AFTER COMMIT
        eventPublisher.publishEvent(
                new BookingConfirmedEvent(booking.getShowId(), seatIds,booking.getId())
        );
    }

    // ===================== CANCEL =====================
    @Transactional
    public void cancelBooking(UUID bookingId) {

        // 🔁 IDEMPOTENCY

//        if (processedEventRepository.existsById(eventId)) {
//            return;
//        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow();

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return;
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        List<UUID> seatIds =
                bookingSeatRepository.findSeatIdsByBookingId(bookingId);

        seatLockService.unlockSeats(booking.getShowId(), seatIds);
    }

//    @Transactional
//    public void confirmBooking(UUID bookingId,UUID showId,List<UUID> seatIds,UUID paymentId) {
//
//        Booking booking = bookingRepository.findById(bookingId)
//                .orElseThrow();
//
//        booking.setStatus(BookingStatus.CONFIRMED);
//        booking.setPaymentId(paymentId);
//
//        bookingRepository.save(booking);
//
//        // ✅ publish event (locks released AFTER COMMIT)
//        eventPublisher.publishEvent(
//                new BookingConfirmedEvent(showId, seatIds)
//        );
//    }
//
//    @Transactional
//    public void cancelBooking(UUID bookingId,UUID showId,List<UUID> seatIds) {
//
//        Booking booking = bookingRepository.findById(bookingId)
//                .orElseThrow();
//
//        booking.setStatus(BookingStatus.CANCELLED);
//        bookingRepository.save(booking);
//
//        // release locks immediately on cancel
//        seatLockService.unlockSeats(showId, seatIds);
//    }

}
