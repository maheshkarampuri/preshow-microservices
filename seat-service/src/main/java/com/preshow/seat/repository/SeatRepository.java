package com.preshow.seat.repository;

import com.preshow.seat.dto.SeatDTO;
import com.preshow.seat.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SeatRepository extends JpaRepository<Seat, UUID> {
    @Query("SELECT s.id FROM Seat s WHERE s.theaterId = :theaterId")
    List<UUID> findIdsByTheaterId(UUID theaterId);

    @Query("SELECT new com.preshow.seat.dto.SeatDTO(s.id, s.seatNumber, s.category) " +
            "FROM Seat s WHERE s.theaterId = :theaterId")
    List<SeatDTO> findByTheaterId(UUID theaterId);

}