package com.preshow.payment.event;

import com.preshow.payment.dto.BookingConfirmed;
import com.preshow.payment.enums.PaymentStatus;
import com.preshow.payment.model.Payment;
import com.preshow.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingEventConsumer {

    private final PaymentRepository paymentRepository;

    @KafkaListener(topics = "booking-confirmed")
    @Transactional
    public void onBookingConfirmed(BookingConfirmed event) {

        System.out.println("Event  : "+event);

        Payment payment =
                paymentRepository.findByBookingId(event.bookingId())
                        .orElseThrow();

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            payment.setStatus(PaymentStatus.COMPLETED);
            paymentRepository.save(payment);
        }
    }
}
