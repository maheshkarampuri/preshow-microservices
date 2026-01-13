package com.preshow.showquery.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document("show-read")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieShowListing {
    @Id
    private String id;
    private String movieId;
    private String movieName;
    private LocalDate date;
    private List<TheaterInfo> theaters = new ArrayList<>();
}