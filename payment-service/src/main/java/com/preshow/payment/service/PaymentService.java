package com.preshow.payment.service;

import com.preshow.payment.config.RazorpayProperties;
import com.preshow.payment.dto.CreatePaymentRequest;
import com.preshow.payment.dto.CreatePaymentResponse;
import com.preshow.payment.enums.PaymentStatus;
import com.preshow.payment.model.Payment;
import com.preshow.payment.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RazorpayClient razorpayClient;
    private final RazorpayProperties razorpayProperties;

    public CreatePaymentResponse createPayment(UUID userId, CreatePaymentRequest request) {

        Payment payment;

        System.out.println("booking id = "+request.getBookingId());

        try {
            payment = paymentRepository.save(
                    Payment.builder()
                            .bookingId(request.getBookingId())
                            .userId(userId)
                            .amount(request.getAmount())
                            .status(PaymentStatus.INITIATED)
                            .provider("RAZORPAY")
                            .build()
            );
        } catch (DataIntegrityViolationException ex) {

            throw new IllegalStateException("Payment already exists for booking");

        }


        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put(
                    "amount",
                    request.getAmount()
                            .multiply(BigDecimal.valueOf(100))
                            .intValue()
            );
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", request.getBookingId().toString());
            orderRequest.put("payment_capture", 1);

            Order order = razorpayClient.orders.create(orderRequest);

            payment.setProviderOrderId(order.get("id"));
            paymentRepository.save(payment);

            return CreatePaymentResponse.builder()
                    .paymentId(payment.getId())
                    .razorpayOrderId(order.get("id"))
                    .amount(payment.getAmount())
                    .currency("INR")
                    .keyId(razorpayProperties.getKeyId())
                    .build();

        } catch (RazorpayException ex) {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new RuntimeException("Failed to create Razorpay order", ex);
        }


    }
}
