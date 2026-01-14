package com.preshow.show.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.preshow.show.client.MovieClient;
import com.preshow.show.client.SeatClient;
import com.preshow.show.client.TheaterClient;
import com.preshow.show.dto.SeatDTO;
import com.preshow.show.dto.ShowCreatedEvent;
import com.preshow.show.dto.ShowSeatResponse;
import com.preshow.show.dto.ShowSeatWrapperResponse;
import com.preshow.show.enums.OutboxStatus;
import com.preshow.show.enums.SeatCategory;
import com.preshow.show.enums.SeatStatus;
import com.preshow.show.events.ShowEventProducer;
import com.preshow.show.model.OutboxEvent;
import com.preshow.show.model.Show;
import com.preshow.show.model.ShowSeat;
import com.preshow.show.model.ShowSeatPricing;
import com.preshow.show.repository.OutboxEventRepository;
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
public class ShowTxService {
    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;
    private final ShowSeatPricingRepository seatPricingRepository;
    private final OutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final SeatClient seatClient;

    @Transactional
    public Show createTransactional(Show show, List<UUID> seatIds,String theaterName, String movieName) {

        Show saved = showRepository.save(show);

        List<ShowSeat> showSeats = seatIds.stream()
                .map(seatId -> ShowSeat.builder()
                        .showId(saved.getId())
                        .seatId(seatId)
                        .status(SeatStatus.AVAILABLE)
                        .build())
                .toList();

        showSeatRepository.saveAll(showSeats);

        createDefaultPricing(saved.getId());

        // 🔹 Create ShowCreated event
        ShowCreatedEvent showCreatedEvent = new ShowCreatedEvent(
                saved.getId(),
                saved.getTheaterId(),
                theaterName,
                saved.getMovieId(),
                movieName,
                saved.getShowTime().toLocalDate(),
                saved.getShowTime().toLocalTime().toString()
        );

        outboxRepository.save(
                OutboxEvent.builder()
                        .id(UUID.randomUUID())
                        .topic("show-created")
                        .aggregateId(saved.getId().toString())
                        .payload(toJson(showCreatedEvent))
                        .status(OutboxStatus.PENDING)
                        .build()
        );

        // 3️⃣ Outbox Event: ShowSeatsCreated

        outboxRepository.save(
                OutboxEvent.builder()
                        .id(UUID.randomUUID())
                        .topic("show-seats-updated")
                        .aggregateId(saved.getId().toString())
                        .payload(toJson(getShowSeats(saved.getId())))
                        .status(OutboxStatus.PENDING)
                        .build()
        );

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

    private String toJson(Object event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize event for outbox", e);
        }
    }

    public ShowSeatWrapperResponse getShowSeats(UUID showId) {

        Show show = showRepository.findById(showId).orElseThrow(() -> new RuntimeException("Show not found"));

        List<SeatDTO> seats = seatClient.getSeatsByTheater(show.getTheaterId());

        Map<UUID, SeatStatus> statusMap = showSeatRepository.findByShowId(showId).stream()
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
