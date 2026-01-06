package com.preshow.payment.config;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public final class RazorpayWebhookUtils {

    private RazorpayWebhookUtils() {}

    public static boolean verifySignature(String payload,String actualSignature,String secret) {
        try {
            String expectedSignature = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secret)
                    .hmacHex(payload);

            return MessageDigest.isEqual(
                    expectedSignature.getBytes(StandardCharsets.UTF_8),
                    actualSignature.getBytes(StandardCharsets.UTF_8)
            );
        } catch (Exception e) {
            return false;
        }
    }
}
