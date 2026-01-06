package com.preshow.payment.controller;

import com.preshow.payment.dto.CreatePaymentRequest;
import com.preshow.payment.dto.CreatePaymentResponse;
import com.preshow.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public CreatePaymentResponse createPayment(@AuthenticationPrincipal Jwt jwt, @RequestBody CreatePaymentRequest request) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return paymentService.createPayment(userId,request);
    }
}
