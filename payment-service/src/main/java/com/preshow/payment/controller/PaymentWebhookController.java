package com.preshow.payment.controller;

import com.preshow.payment.config.RazorpayProperties;
import com.preshow.payment.config.RazorpayWebhookUtils;
import com.preshow.payment.service.PaymentWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments/webhook")
public class PaymentWebhookController {

    private final RazorpayProperties properties;
    private final PaymentWebhookService webhookService;


    @PostMapping
    public ResponseEntity<Void> handleWebhook(@RequestBody String payload,@RequestHeader("X-Razorpay-Signature") String signature) {

        // 🔍 PRINT RAW PAYLOAD
        System.out.println("🔔 Razorpay Webhook Payload:");
        System.out.println(payload);


        System.out.println(" properties.getWebhookSecret() : "+ properties.getWebhookSecret());
        boolean valid = RazorpayWebhookUtils.verifySignature(
                payload,
                signature,
                properties.getWebhookSecret()
        );

        if (!valid) {
            System.out.println("❌ Invalid Razorpay signature");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        System.out.println("✅ Razorpay signature verified");
        webhookService.process(payload);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok().body("Success");
    }
}
