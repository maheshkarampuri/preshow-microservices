package com.preshow.booking.repository;

import com.preshow.booking.model.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, UUID> {
}
