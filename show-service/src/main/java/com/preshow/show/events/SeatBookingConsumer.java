package com.preshow.show.events;

import com.preshow.show.dto.BookingConfirmedEvent;
import com.preshow.show.service.ShowSeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatBookingConsumer {

    private final ShowSeatService showSeatService;

    @KafkaListener(topics = "seats-booked",groupId = "show-service")
    public void onSeatsBooked(BookingConfirmedEvent event) {
        System.out.println("🎟 Seats booked event received: "+ event);
        showSeatService.markSeatsAsBooked(event.showId(),event.seatIds());
    }
}