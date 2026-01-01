package com.preshow.show.client;

import com.preshow.show.config.OAuthClientConfig;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeignAuthInterceptor implements RequestInterceptor {

    private final OAuth2AuthorizedClientManager manager;
    private final OAuthClientConfig tokenProvider;

    @Override
    public void apply(RequestTemplate template) {
        String token = tokenProvider.getServiceToken(manager);
        template.header("Authorization", "Bearer " + token);
    }
}