package com.preshow.showquery.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

import java.time.Instant;
import java.util.Collection;
import java.util.List;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class OAuthClientConfig {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;

    private OAuth2AuthorizedClient cached;

    @Bean
    public OAuth2AuthorizedClientManager authClientManager() {

        OAuth2AuthorizedClientProvider provider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        var manager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                clientRegistrationRepository,
                authorizedClientService
        );

        manager.setAuthorizedClientProvider(provider);
        return manager;
    }

    /** 🚀 Pass manager as argument to avoid circular dependency */
    public String getServiceToken(OAuth2AuthorizedClientManager manager) {

        if (cached != null &&
                cached.getAccessToken() != null &&
                cached.getAccessToken().getExpiresAt() != null &&
                cached.getAccessToken().getExpiresAt().isAfter(Instant.now().plusSeconds(30))) {

            return cached.getAccessToken().getTokenValue();
        }

        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest
                .withClientRegistrationId("internal-client")
                .principal("service-client")
                .build();

        cached = manager.authorize(request);

        if (cached == null || cached.getAccessToken() == null) {
            throw new IllegalStateException("❌ Could not obtain OAuth2 token!");
        }

        return cached.getAccessToken().getTokenValue();
    }

}
