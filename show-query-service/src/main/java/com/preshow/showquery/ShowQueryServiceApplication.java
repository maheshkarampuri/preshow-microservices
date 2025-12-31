package com.preshow.showquery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ShowQueryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShowQueryServiceApplication.class, args);
        System.out.println("Successfully Executed Show Query Service...");
    }

}
