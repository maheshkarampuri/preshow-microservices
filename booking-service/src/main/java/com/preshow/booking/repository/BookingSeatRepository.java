package com.preshow.booking.repository;

import com.preshow.booking.model.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, UUID> {
    @Query("select bs.showSeatId from BookingSeat bs where bs.bookingId = :bookingId")
    List<UUID> findSeatIdsByBookingId(@Param("bookingId") UUID bookingId);
}
