package com.preshow.seat.controller;

import com.preshow.seat.model.Seat;
import com.preshow.seat.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/seats")
@RequiredArgsConstructor
public class SeatController {


    private final SeatService seatService;

    // CREATE
    @PostMapping
    public ResponseEntity<Seat> createSeat(@RequestBody Seat seat) {
        return ResponseEntity.ok(seatService.createSeat(seat));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Seat>> getSeats() {
        return ResponseEntity.ok(seatService.getAllSeats());
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<Seat> getSeat(@PathVariable UUID id) {
        return ResponseEntity.ok(seatService.getSeatById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Seat> updateSeat(
            @PathVariable UUID id,
            @RequestBody Seat seat) {
        return ResponseEntity.ok(seatService.updateSeat(id, seat));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSeat(@PathVariable UUID id) {
        seatService.deleteSeat(id);
        return ResponseEntity.ok("Seat deleted successfully");
    }
}