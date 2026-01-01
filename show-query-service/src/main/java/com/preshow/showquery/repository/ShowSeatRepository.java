package com.preshow.showquery.repository;

import com.preshow.showquery.dto.ShowSeatWrapperResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShowSeatRepository extends MongoRepository<ShowSeatWrapperResponse, String> {
}
