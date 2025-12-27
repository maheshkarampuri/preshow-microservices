package com.preshow.seat.service;

import com.preshow.seat.enums.SeatStatus;
import com.preshow.seat.model.Seat;
import com.preshow.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository repository;

    public Seat createSeat(Seat seat) {
        return repository.save(seat);
    }

    public List<Seat> getSeats(UUID theaterId, UUID movieId, LocalDateTime showTime) {
        return repository.findByTheaterIdAndMovieIdAndShowTime(theaterId, movieId, showTime);
    }

    public void updateStatus(UUID seatId, String status) {
        Seat seat = repository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        try {
            SeatStatus seatStatus = SeatStatus.valueOf(status.toUpperCase());
            seat.setStatus(seatStatus);
            repository.save(seat);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status. Allowed: AVAILABLE, HOLD, BOOKED");
        }
    }

}