package com.preshow.show.service;

import com.preshow.show.client.SeatClient;
import com.preshow.show.dto.SeatDTO;
import com.preshow.show.dto.ShowSeatResponse;
import com.preshow.show.dto.ShowSeatWrapperResponse;
import com.preshow.show.enums.SeatCategory;
import com.preshow.show.enums.SeatStatus;
import com.preshow.show.model.Show;
import com.preshow.show.model.ShowSeat;
import com.preshow.show.model.ShowSeatPricing;
import com.preshow.show.repository.ShowRepository;
import com.preshow.show.repository.ShowSeatPricingRepository;
import com.preshow.show.repository.ShowSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowSeatService {
    private final ShowSeatRepository repo;
    private final ShowRepository showRepository;
    private final ShowSeatPricingRepository seatPricingRepository;
    private final SeatClient seatClient;

    public ShowSeat create(ShowSeat seat){ return repo.save(seat); }
    public List<ShowSeat> getAll(){ return repo.findAll(); }
    public List<ShowSeat> getByShowId(UUID showId){ return repo.findByShowId(showId); }
    public ShowSeat getById(UUID id){ return repo.findById(id).orElseThrow(); }

    public ShowSeat update(UUID id, ShowSeat data){
        ShowSeat s = getById(id);
        s.setShowId(data.getShowId());
        s.setSeatId(data.getSeatId());
        s.setStatus(data.getStatus());
        return repo.save(s);
    }

    @Transactional
    public void markSeatsAsBooked(UUID showId, List<UUID> seatIds) {
        repo.markBooked(showId, seatIds);
    }

    public void delete(UUID id){ repo.deleteById(id); }

    public ShowSeatWrapperResponse getShowSeats(UUID showId) {

        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));

        List<SeatDTO> seats = seatClient.getSeatsByTheater(show.getTheaterId());

        Map<UUID, SeatStatus> statusMap = repo.findByShowId(showId).stream()
                .collect(Collectors.toMap(ShowSeat::getSeatId, ShowSeat::getStatus));

        Map<SeatCategory, BigDecimal> pricingMap = seatPricingRepository.findByShowId(showId).stream()
                .collect(Collectors.toMap(ShowSeatPricing::getCategory, ShowSeatPricing::getPrice));

        List<ShowSeatResponse> seatResponses = seats.stream()
                .map(seat -> ShowSeatResponse.builder()
                        .id(seat.getId())
                        .seatNumber(seat.getSeatNumber())
                        .category(seat.getCategory())
                        .price(pricingMap.get(seat.getCategory()))
                        .status(statusMap.getOrDefault(seat.getId(), SeatStatus.AVAILABLE))
                        .build())
                .toList();

        return ShowSeatWrapperResponse.builder()
                .showId(showId)
                .seats(seatResponses)
                .build();
    }

}
