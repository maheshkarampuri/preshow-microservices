package com.preshow.show.controller;

import com.preshow.show.client.SeatClient;
import com.preshow.show.model.Show;
import com.preshow.show.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/shows")
@RequiredArgsConstructor
public class ShowController {
    private final ShowService service;

    @PostMapping
    public Show create(@RequestBody Show s){
        return service.create(s);
    }

    @GetMapping
    public List<Show> getAll(){ return service.getAll(); }

    @GetMapping("/{id}")
    public Show get(@PathVariable UUID id){ return service.getById(id); }

    @PutMapping("/{id}")
    public Show update(@PathVariable UUID id, @RequestBody Show s){ return service.update(id, s); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id){ service.delete(id); }
}
