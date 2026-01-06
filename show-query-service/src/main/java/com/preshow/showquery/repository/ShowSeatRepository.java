package com.preshow.showquery.repository;

import com.preshow.showquery.dto.ShowSeatWrapperResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShowSeatRepository extends MongoRepository<ShowSeatWrapperResponse, String> {
    Optional<ShowSeatWrapperResponse> findByShowId(String showId);
}
