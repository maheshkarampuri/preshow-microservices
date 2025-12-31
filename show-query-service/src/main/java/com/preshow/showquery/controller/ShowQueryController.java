package com.preshow.showquery.controller;

import com.preshow.showquery.model.MovieShowListing;
import com.preshow.showquery.service.ShowQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/queries")
@RequiredArgsConstructor
public class ShowQueryController {

    private final ShowQueryService service;

    // INSERT SAMPLE DATA USING POSTMAN
    @PostMapping("/sample")
    public MovieShowListing insertSample(@RequestBody MovieShowListing data) {
        return service.insertSample(data);
    }

    // GET DATA FOR A MOVIE & DATE
    @GetMapping("/movie/{movieId}")
    public MovieShowListing getMovieShows(
            @PathVariable UUID movieId,
            @RequestParam LocalDate date
    ) {
        return service.getShowsForMovie(movieId, date);
    }
}