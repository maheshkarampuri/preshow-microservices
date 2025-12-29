package com.preshow.theater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TheaterServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TheaterServiceApplication.class, args);
    }

}
