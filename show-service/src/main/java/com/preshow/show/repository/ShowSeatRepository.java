package com.preshow.show.repository;

import com.preshow.show.model.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, UUID> {
    List<ShowSeat> findByShowId(UUID showId);
}