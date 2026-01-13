package com.preshow.show.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "movie-service")
public interface MovieClient {
    @GetMapping("/movies/{id}/name")
    String getName(@PathVariable String id);
}