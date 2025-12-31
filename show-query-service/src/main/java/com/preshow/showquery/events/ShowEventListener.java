package com.preshow.showquery.events;

import com.preshow.showquery.client.TheaterClient;
import com.preshow.showquery.dto.ShowCreatedEvent;
import com.preshow.showquery.model.MovieShowListing;
import com.preshow.showquery.model.ShowInfo;
import com.preshow.showquery.model.TheaterInfo;
import com.preshow.showquery.repository.MovieShowListingRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ShowEventListener {

    private final MovieShowListingRepository repo;
    private final TheaterClient theaterClient;

    @KafkaListener(topics = "show-created", groupId = "show-query-group")
    public void listen(ShowCreatedEvent e) {
        System.out.println("🎧 RECEIVED EVENT in show-query-service: " + e);


        String documentId = e.movieId() + "_" + e.showDate();

        MovieShowListing listing = repo.findById(documentId)
                .orElse(new MovieShowListing(
                        documentId,
                        e.movieId(),
                        e.showDate(),
                        new ArrayList<>()
                ));


        String name = theaterClient.getName(e.theaterId());

        ShowInfo showInfo = new ShowInfo(
                e.showId(),
                e.showTimeText(),
                "AVAILABLE"
        );

        // Update Theater List
        Optional<TheaterInfo> theaterOpt = listing.getTheaters().stream()
                .filter(t -> t.theaterId().equals(e.theaterId()))
                .findFirst();

        if (theaterOpt.isPresent()) {
            theaterOpt.get().shows().add(showInfo);
        } else {
            listing.getTheaters().add(
                    new TheaterInfo(
                            e.theaterId(),
                            name,
                            new ArrayList<>(List.of(showInfo))
                    )
            );
        }

        repo.save(listing);
    }
}