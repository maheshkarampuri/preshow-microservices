package com.preshow.theater.controller;

import com.preshow.theater.model.Theater;
import com.preshow.theater.repository.TheaterRepository;
import com.preshow.theater.service.TheaterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/theaters")
@AllArgsConstructor
public class TheaterController {

    private final TheaterService service;
    private final TheaterRepository repo;


    @GetMapping("/{id}/name")
    public String getTheaterName(@PathVariable UUID id){
        return repo.findById(id)
                .map(Theater::getName)
                .orElse("UNKNOWN");
    }

    @GetMapping
    public List<Theater> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Theater> getById(@PathVariable UUID id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Theater> create(@RequestBody Theater theater) {
        return ResponseEntity.ok(service.save(theater));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Theater> update(@PathVariable UUID id, @RequestBody Theater theater) {
        return ResponseEntity.ok(service.update(id, theater));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}