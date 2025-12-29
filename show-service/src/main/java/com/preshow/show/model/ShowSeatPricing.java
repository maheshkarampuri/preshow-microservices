package com.preshow.show.model;

import com.preshow.show.enums.SeatCategory;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "show_seat_pricing")
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ShowSeatPricing {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID showId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatCategory category;

    @Column(nullable = false)
    private BigDecimal price;
}