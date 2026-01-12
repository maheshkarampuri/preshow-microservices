package com.preshow.show.model;

import com.preshow.show.enums.SeatCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
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

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}