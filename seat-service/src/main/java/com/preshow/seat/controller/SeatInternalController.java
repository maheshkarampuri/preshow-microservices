package com.preshow.seat.controller;

import com.preshow.seat.dto.SeatDTO;
import com.preshow.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/seats/internal")
@RequiredArgsConstructor
public class SeatInternalController {

    private final SeatRepository seatRepository;

    @GetMapping("/theater/{theaterId}/ids")
    public List<UUID> getSeatIds(@PathVariable UUID theaterId) {
        return seatRepository.findIdsByTheaterId(theaterId);
    }

    @GetMapping("/theater/{theaterId}")
    public List<SeatDTO> getSeatsByIds(@PathVariable UUID theaterId) {
        return seatRepository.findByTheaterId(theaterId);
    }

}