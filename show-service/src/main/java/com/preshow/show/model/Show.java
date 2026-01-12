package com.preshow.show.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "shows")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID theaterId;
    private UUID movieId;
    private LocalDateTime showTime;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}