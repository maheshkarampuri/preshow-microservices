package com.preshow.show.client;

import com.preshow.show.dto.SeatDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "seat-service")
public interface SeatClient {
    @GetMapping("/seats/internal/theater/{theaterId}/ids")
    List<UUID> getSeatIds(@PathVariable UUID theaterId);

    @GetMapping("/seats/internal/theater/{theaterId}")
    List<SeatDTO> getSeatsByTheater(@PathVariable UUID theaterId);
}
