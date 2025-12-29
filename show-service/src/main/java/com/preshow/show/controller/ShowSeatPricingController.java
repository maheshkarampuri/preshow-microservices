package com.preshow.show.controller;

import com.preshow.show.model.ShowSeatPricing;
import com.preshow.show.service.ShowSeatPricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/show-pricing")
@RequiredArgsConstructor
public class ShowSeatPricingController {
    private final ShowSeatPricingService service;

    @PostMapping
    public ShowSeatPricing create(@RequestBody ShowSeatPricing p){ return service.create(p); }

    @GetMapping
    public List<ShowSeatPricing> getAll(){ return service.getAll(); }

    @GetMapping("/show/{showId}")
    public List<ShowSeatPricing> getByShow(@PathVariable UUID showId){ return service.getByShowId(showId); }

    @PutMapping("/{id}")
    public ShowSeatPricing update(@PathVariable UUID id, @RequestBody ShowSeatPricing p){ return service.update(id, p); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id){ service.delete(id); }
}
