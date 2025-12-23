package com.preshow.auth.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;


@RestController
@RequestMapping("/.well-known")
@RequiredArgsConstructor
public class JwksController {

    private final KeyPair keyPair;

    @GetMapping("/jwks.json")
    public Map<String, Object> jwks() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .keyID("auth-key")
                .build();

        return new JWKSet(rsaKey).toJSONObject();
    }
}
