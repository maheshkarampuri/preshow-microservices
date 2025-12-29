package com.preshow.booking.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "booking_seats")
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor @Builder
public class BookingSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID bookingId;  // references Booking.id

    @Column(nullable = false)
    private UUID showSeatId; // references ShowSeat.id (from show-service)
}