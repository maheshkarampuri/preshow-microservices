package com.preshow.auth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


@Configuration
@RequiredArgsConstructor
public class JwkConfig {

    private final JwtProperties jwtProperties;

    @Bean
    public RSAKey rsaKey() throws Exception {
        RSAPublicKey publicKey = loadPublicKey();
        RSAPrivateKey privateKey = loadPrivateKey();

        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID("auth-key")
                .build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
        return new ImmutableJWKSet<>(new JWKSet(rsaKey));
    }

    private RSAPublicKey loadPublicKey() throws Exception {
        String key = new String(jwtProperties.getPublicKey().getInputStream().readAllBytes())
                .replaceAll("-----\\w+ PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        return (RSAPublicKey) KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(key)));
    }

    private RSAPrivateKey loadPrivateKey() throws Exception {
        String key = new String(jwtProperties.getPrivateKey().getInputStream().readAllBytes())
                .replaceAll("-----\\w+ PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        return (RSAPrivateKey) KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key)));
    }
}
