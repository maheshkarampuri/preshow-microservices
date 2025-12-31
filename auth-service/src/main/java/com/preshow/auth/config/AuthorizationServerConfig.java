package com.preshow.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;


import java.util.UUID;

@Configuration
public class AuthorizationServerConfig {

    @Bean
    public RegisteredClientRepository registeredClientRepository(
            PasswordEncoder encoder) {

        RegisteredClient internalClient =
                RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId("auth-service")
                        .clientSecret(encoder.encode("auth-secret"))
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC) // 👈 REQUIRED
                        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                        .scope("internal.write")
                        .build();

        return new InMemoryRegisteredClientRepository(internalClient);
    }
}