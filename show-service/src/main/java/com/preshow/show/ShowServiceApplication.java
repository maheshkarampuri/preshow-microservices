package com.preshow.show;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ShowServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShowServiceApplication.class, args);
    }

}
