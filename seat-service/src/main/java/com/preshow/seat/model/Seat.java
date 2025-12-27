package com.preshow.seat.model;

import com.preshow.seat.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "seats",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "unique_seat_for_show",
            columnNames = {"theater_id", "movie_id", "show_time", "seat_number"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "theater_id", nullable = false)
    private UUID theaterId;

    @Column(name = "movie_id", nullable = false)
    private UUID movieId;

    @Column(name = "show_time", nullable = false)
    private LocalDateTime showTime;

    @Column(name = "seat_number", nullable = false, length = 10)
    private String seatNumber; // A1, A2, B5, etc.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status;
}
