package com.preshow.showquery.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "theater-service")
public interface TheaterClient {

    @GetMapping("/theaters/{id}/name")
    String getName(@PathVariable String id);
}