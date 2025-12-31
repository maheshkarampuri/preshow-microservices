package com.preshow.showquery.repository;

import com.preshow.showquery.model.MovieShowListing;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface MovieShowListingRepository extends MongoRepository<MovieShowListing, String> {
    Optional<MovieShowListing> findByMovieIdAndDate(UUID movieId, LocalDate date);
}