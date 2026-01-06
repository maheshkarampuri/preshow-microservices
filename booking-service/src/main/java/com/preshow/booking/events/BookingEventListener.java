package com.preshow.booking.events;

import com.preshow.booking.dto.BookingConfirmedEvent;
import com.preshow.booking.service.SeatLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class BookingEventListener {

    private final SeatLockService seatLockService;
    private final KafkaTemplate<String, Object> template;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleBookingConfirmed(BookingConfirmedEvent event) {

        seatLockService.unlockSeats(
                event.showId(),
                event.seatIds()
        );

        template.send("seats-booked",event);

    }
}
