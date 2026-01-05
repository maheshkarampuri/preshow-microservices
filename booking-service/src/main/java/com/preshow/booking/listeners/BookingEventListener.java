package com.preshow.booking.listeners;

import com.preshow.booking.dto.BookingConfirmedEvent;
import com.preshow.booking.service.SeatLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class BookingEventListener {

    private final SeatLockService seatLockService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleBookingConfirmed(BookingConfirmedEvent event) {

        seatLockService.unlockSeats(
                event.showId(),
                event.seatIds()
        );
    }
}
