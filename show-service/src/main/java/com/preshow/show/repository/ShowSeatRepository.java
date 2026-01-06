package com.preshow.show.repository;

import com.preshow.show.model.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, UUID> {
    List<ShowSeat> findByShowId(UUID showId);


    @Modifying
    @Query("""
    update ShowSeat s
    set s.status = 'BOOKED'
    where s.showId = :showId
      and s.seatId in :seatIds
      and s.status = 'AVAILABLE'
""")
    void markBooked(
            @Param("showId") UUID showId,
            @Param("seatIds") List<UUID> seatIds
    );

}