package com.preshow.show.repository;

import com.preshow.show.model.ShowSeatPricing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ShowSeatPricingRepository extends JpaRepository<ShowSeatPricing, UUID> {
    List<ShowSeatPricing> findByShowId(UUID showId);
}