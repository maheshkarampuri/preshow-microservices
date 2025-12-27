package com.preshow.seat.controller;

import com.preshow.seat.model.Seat;
import com.preshow.seat.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    // Create Seat
    @PostMapping
    public ResponseEntity<Seat> createSeat(@RequestBody Seat seat) {
        Seat savedSeat = seatService.createSeat(seat);
        return ResponseEntity.ok(savedSeat);
    }

    // Get All Seats for a Show
    @GetMapping("/{theaterId}/{movieId}/{showTime}")
    public ResponseEntity<List<Seat>> getSeats(
            @PathVariable UUID theaterId,
            @PathVariable UUID movieId,
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime showTime
    ) {
        List<Seat> seats = seatService.getSeats(theaterId, movieId, showTime);
        return ResponseEntity.ok(seats);
    }

    // Update Seat Status
    @PatchMapping("/{seatId}/status")
    public ResponseEntity<String> updateSeatStatus(
            @PathVariable UUID seatId,
            @RequestParam String status
    ) {
        seatService.updateStatus(seatId, status);
        return ResponseEntity.ok("Seat status updated to " + status.toUpperCase());
    }
}