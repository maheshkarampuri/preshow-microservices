package com.preshow.booking.controller;

import com.preshow.booking.dto.BookingRequest;
import com.preshow.booking.dto.CreateBookingResponse;
import com.preshow.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<CreateBookingResponse> createBooking(@AuthenticationPrincipal Jwt jwt,
                                                               @RequestBody BookingRequest request) {
        UUID userId = UUID.fromString(jwt.getSubject());
        UUID bookingId = bookingService.createBooking(userId,request.getShowId(),request.getSeatIds(),request.getSeatSnapshotMap());

        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateBookingResponse(bookingId));
    }
}