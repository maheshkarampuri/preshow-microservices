package com.preshow.show.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.preshow.show.dto.ShowCreatedEvent;
import com.preshow.show.dto.ShowSeatWrapperResponse;
import com.preshow.show.enums.OutboxStatus;
import com.preshow.show.model.OutboxEvent;
import com.preshow.show.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxEventRepository outboxRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 3000)
    public void publish() {

        List<OutboxEvent> events =
                outboxRepository.findTop10ByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING);

        for (OutboxEvent event : events) {
            try {

                Object payload =
                        switch (event.getTopic()) {
                            case "show-created" ->
                                    objectMapper.readValue(
                                            event.getPayload(),
                                            ShowCreatedEvent.class
                                    );
                            case "show-seats-updated" ->
                                    objectMapper.readValue(
                                            event.getPayload(),
                                            ShowSeatWrapperResponse.class
                                    );
                            default -> throw new IllegalStateException("Unknown topic");
                        };

                kafkaTemplate.send(
                        event.getTopic(),
                        event.getAggregateId(),
                        payload
                );

                System.out.println("event.getPayload() : "+event.getPayload());

                event.setStatus(OutboxStatus.SENT);
                outboxRepository.save(event);

                System.out.println("Sent Event...");

            } catch (Exception ex) {
                event.setStatus(OutboxStatus.PENDING);
                outboxRepository.save(event);
            }
        }
    }
}
