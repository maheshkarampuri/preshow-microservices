package com.preshow.theater.repository;

import com.preshow.theater.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TheaterRepository extends JpaRepository<Theater, UUID> {
}