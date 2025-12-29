package com.preshow.show.repository;

import com.preshow.show.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ShowRepository extends JpaRepository<Show, UUID> {
    List<Show> findByMovieIdAndShowTimeBetween(UUID movieId, LocalDateTime start, LocalDateTime end);

}
