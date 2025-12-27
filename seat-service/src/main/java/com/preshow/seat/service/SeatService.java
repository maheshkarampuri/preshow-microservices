package com.preshow.seat.service;

import com.preshow.seat.model.Seat;
import com.preshow.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;


    public Seat createSeat(Seat seat) {
        return seatRepository.save(seat);
    }

    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    public Seat getSeatById(UUID id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat not found with id " + id));
    }

    public Seat updateSeat(UUID id, Seat updatedSeat) {
        Seat existing = getSeatById(id);

        existing.setTheaterId(updatedSeat.getTheaterId());
        existing.setSeatNumber(updatedSeat.getSeatNumber());
        existing.setCategory(updatedSeat.getCategory());

        return seatRepository.save(existing);
    }

    public void deleteSeat(UUID id) {
        seatRepository.deleteById(id);
    }

}