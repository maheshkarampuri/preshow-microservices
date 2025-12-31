package com.preshow.showquery.service;

import com.preshow.showquery.model.MovieShowListing;
import com.preshow.showquery.repository.MovieShowListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShowQueryService {

    private final MovieShowListingRepository repository;

    // POST: INSERT SAMPLE / ANY DATA FROM POSTMAN
    public MovieShowListing insertSample(MovieShowListing sample) {
        return repository.save(sample);
    }

    // GET: FETCH READ MODEL
    public MovieShowListing getShowsForMovie(UUID movieId, LocalDate date) {
        return repository.findByMovieIdAndDate(movieId, date)
                .orElseThrow(() -> new RuntimeException(" No shows found for movie " + movieId + " on " + date));
    }
}