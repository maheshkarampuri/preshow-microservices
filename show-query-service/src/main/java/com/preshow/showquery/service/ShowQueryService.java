package com.preshow.showquery.service;

import com.preshow.showquery.dto.ShowSeatResponse;
import com.preshow.showquery.dto.ShowSeatWrapperResponse;
import com.preshow.showquery.enums.SeatCategory;
import com.preshow.showquery.model.MovieShowListing;
import com.preshow.showquery.repository.MovieShowListingRepository;
import com.preshow.showquery.repository.ShowSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowQueryService {

    private final MovieShowListingRepository repository;

    private final ShowSeatRepository showSeatRepository;

    // POST: INSERT SAMPLE / ANY DATA FROM POSTMAN
    public MovieShowListing insertSample(MovieShowListing sample) {
        return repository.save(sample);
    }

    // GET: FETCH READ MODEL
    public MovieShowListing getShowsForMovie(String movieId, LocalDate date) {
        return repository.findByMovieIdAndDate(movieId, date)
                .orElseThrow(() -> new RuntimeException(" No shows found for movie " + movieId + " on " + date));
    }

    public ShowSeatWrapperResponse getShowSeats(String showId) {

        ShowSeatWrapperResponse doc = showSeatRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show seats not found for showId: " + showId));

        // Sort seats by seatNumber (A1, A2, A3 ...)
        List<ShowSeatResponse> sortedSeats = doc.getSeats().stream()
                .sorted(Comparator.comparing(ShowSeatResponse::getSeatNumber))
                .toList();

        doc.setSeats(sortedSeats);
        return doc;
    }

}