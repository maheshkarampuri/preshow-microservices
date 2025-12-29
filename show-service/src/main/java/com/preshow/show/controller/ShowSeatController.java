package com.preshow.show.controller;

import com.preshow.show.model.ShowSeat;
import com.preshow.show.service.ShowSeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/show-seats")
@RequiredArgsConstructor
public class ShowSeatController {
    private final ShowSeatService service;

    @PostMapping
    public ShowSeat create(@RequestBody ShowSeat seat){ return service.create(seat); }

    @GetMapping
    public List<ShowSeat> getAll(){ return service.getAll(); }

    @GetMapping("/show/{showId}")
    public List<ShowSeat> getByShow(@PathVariable UUID showId){ return service.getByShowId(showId); }

    @PutMapping("/{id}")
    public ShowSeat update(@PathVariable UUID id, @RequestBody ShowSeat seat){ return service.update(id, seat); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id){ service.delete(id); }
}
