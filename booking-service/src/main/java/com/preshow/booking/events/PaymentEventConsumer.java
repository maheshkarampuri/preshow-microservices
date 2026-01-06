package com.preshow.booking.events;

import com.preshow.booking.dto.PaymentFailedEvent;
import com.preshow.booking.dto.PaymentSuccessEvent;
import com.preshow.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {

    private final BookingService bookingService;

    // ===================== PAYMENT SUCCESS =====================
    @KafkaListener(
            topics = "payment-success",
            groupId = "booking-service"
    )
    @Transactional
    public void onPaymentSuccess(PaymentSuccessEvent event) {

        System.out.println("Payment success event: {}"+event);
        bookingService.confirmBooking(event.bookingId(),event.paymentId());
    }

    // ===================== PAYMENT FAILURE =====================
    @KafkaListener(
            topics = "payment-failed",
            groupId = "booking-service"
    )
    @Transactional
    public void onPaymentFailed(PaymentFailedEvent event) {

        System.out.println("Payment failed event: {}"+event);

        bookingService.cancelBooking(event.bookingId());

    }
}