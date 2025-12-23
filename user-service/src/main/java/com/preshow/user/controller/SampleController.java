package com.preshow.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sample")
public class SampleController {
    @GetMapping("/test")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Test is working");
    }
}
