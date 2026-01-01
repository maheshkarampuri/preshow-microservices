package com.preshow.show.events;

import com.preshow.show.dto.ShowCreatedEvent;
import com.preshow.show.dto.ShowSeatWrapperResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShowEventProducer {
    private final KafkaTemplate<String, Object> template;

    public void sendShowCreated(ShowCreatedEvent event){
        template.send("show-created", event);
    }

    public void sendShowSeats(ShowSeatWrapperResponse event){
        template.send("show-seats-updated", event);
    }
}
