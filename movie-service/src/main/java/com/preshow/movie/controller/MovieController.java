package com.preshow.movie.controller;

import com.preshow.movie.model.Movie;
import com.preshow.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService service;

    @PostMapping
    public ResponseEntity<Movie> create(@RequestBody Movie movie) {
        return ResponseEntity.ok(service.addMovie(movie));
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAll() {
        return ResponseEntity.ok(service.getAllMovies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getMovie(id));
    }
}