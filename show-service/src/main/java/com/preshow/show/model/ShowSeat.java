package com.preshow.show.model;

import com.preshow.show.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "show_seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID showId;

    private UUID seatId;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;
}