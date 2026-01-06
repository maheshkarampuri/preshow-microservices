package com.preshow.booking.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "processed_events",
    indexes = {
        @Index(name = "idx_processed_event_time", columnList = "processedAt")
    }
)
public class ProcessedEvent {

    @Id
    private UUID eventId;

    @Column(nullable = false)
    private Instant processedAt;
}
