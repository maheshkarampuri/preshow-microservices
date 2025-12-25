package com.preshow.movie.service;

import com.preshow.movie.model.Movie;
import com.preshow.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository repository;

    public Movie addMovie(Movie movie) {
        return repository.save(movie);
    }

    public List<Movie> getAllMovies() {
        return repository.findAll();
    }

    public Movie getMovie(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));
    }
}