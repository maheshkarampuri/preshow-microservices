package com.preshow.seat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SeatServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeatServiceApplication.class, args);
        System.out.println("Successfully Executed Seat Service");
    }

}
