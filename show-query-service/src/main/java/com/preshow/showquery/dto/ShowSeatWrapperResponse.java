package com.preshow.showquery.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "show-seats")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShowSeatWrapperResponse {
    @Id
    private String showId;
    private List<ShowSeatResponse> seats;
}