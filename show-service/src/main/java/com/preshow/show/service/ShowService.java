package com.preshow.show.service;

import com.preshow.show.client.SeatClient;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShowService {
    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;
    private final SeatClient seatClient;
    private final ShowSeatPricingRepository seatPricingRepository;

    public Show create(Show show){

        Show saved = showRepository.save(show);

        // get seats from seat-service for the theater
        List<UUID> seatIds = seatClient.getSeatIds(show.getTheaterId());

        // create show seats
        List<ShowSeat> showSeats = seatIds.stream()
                .map(id -> new ShowSeat(null, show.getId(), id, SeatStatus.AVAILABLE))
                .toList();

        showSeatRepository.saveAll(showSeats);

        //Create pricing for categories
        createDefaultPricing(saved.getId());

        return saved;

    }

    private void createDefaultPricing(UUID showId) {
        Map<SeatCategory, BigDecimal> defaultPrices = Map.of(
                SeatCategory.SILVER, new BigDecimal("150"),
                SeatCategory.GOLD, new BigDecimal("250"),
                SeatCategory.PLATINUM, new BigDecimal("350"),
                SeatCategory.VIP, new BigDecimal("500")
        );

        List<ShowSeatPricing> pricing = defaultPrices.entrySet().stream()
                .map(e -> ShowSeatPricing.builder()
                        .showId(showId)
                        .category(e.getKey())
                        .price(e.getValue())
                        .build())
                .toList();

        seatPricingRepository.saveAll(pricing);
    }
    public List<Show> getAll(){ return showRepository.findAll(); }
    public Show getById(UUID id){ return showRepository.findById(id).orElseThrow(); }
    public Show update(UUID id, Show data){
        Show s = getById(id);
        s.setTheaterId(data.getTheaterId());
        s.setMovieId(data.getMovieId());
        s.setShowTime(data.getShowTime());
        return showRepository.save(s);
    }
    public void delete(UUID id){ showRepository.deleteById(id); }
}
