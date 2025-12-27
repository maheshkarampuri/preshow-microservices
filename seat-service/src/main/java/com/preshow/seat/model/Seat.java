package com.preshow.seat.model;

import com.preshow.seat.enums.SeatCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "seats")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID theaterId;

    @Column(nullable = false, length = 10)
    private String seatNumber; // A1, A2, B5

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatCategory category; // GOLD, SILVER, PREMIUM
}