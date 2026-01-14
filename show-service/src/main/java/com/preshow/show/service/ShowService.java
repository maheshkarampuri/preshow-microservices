package com.preshow.show.service;

import com.preshow.show.client.MovieClient;
import com.preshow.show.client.SeatClient;
import com.preshow.show.client.TheaterClient;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowService {
    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;
    private final SeatClient seatClient;
    private final ShowSeatPricingRepository seatPricingRepository;
    private final ShowTxService showTxService;
    private final TheaterClient theaterClient;
    private final MovieClient movieClient;

    public Show create(Show show) {
        List<UUID> seatIds = seatClient.getSeatIds(show.getTheaterId());
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalStateException("No seats found for theater");
        }
        String theaterName = theaterClient.getName(String.valueOf(show.getTheaterId()));
        String movieName = movieClient.getName(String.valueOf(show.getMovieId()));

        return showTxService.createTransactional(show, seatIds,theaterName,movieName);
    }


//    @Transactional
//    public Show create(Show show){
//
//        Show saved = showRepository.save(show);
//
//        // get seats from seat-service for the theater
//        List<UUID> seatIds = seatClient.getSeatIds(show.getTheaterId());
//
//        // create show seats
////        List<ShowSeat> showSeats = seatIds.stream()
////                .map(id -> new ShowSeat(null, show.getId(), id, SeatStatus.AVAILABLE))
////                .toList();
//
//        List<ShowSeat> showSeats = seatIds.stream()
//                .map(seatId -> ShowSeat.builder()
//                        .showId(show.getId())
//                        .seatId(seatId)
//                        .status(SeatStatus.AVAILABLE)
//                        .build())
//                .toList();
//
//        showSeatRepository.saveAll(showSeats);
//
//        //Create pricing for categories
//        createDefaultPricing(saved.getId());
//
//        String theaterName = theaterClient.getName(String.valueOf(saved.getTheaterId()));
//        String movieName = movieClient.getName(String.valueOf(saved.getMovieId()));
//
//        // Emit Event to Kafka
//        ShowCreatedEvent event = new ShowCreatedEvent(
//                saved.getId(),
//                saved.getTheaterId(),
//                theaterName,
//                saved.getMovieId(),
//                movieName,
//                saved.getShowTime().toLocalDate(),
//                saved.getShowTime().toLocalTime().toString()
//        );
//
//        producer.sendShowCreated(event);
//
//        System.out.println("📩 ShowCreatedEvent published: " + event);
//
//        producer.sendShowSeats(getShowSeats(saved.getId()));
//
//        return saved;
//
//    }

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
