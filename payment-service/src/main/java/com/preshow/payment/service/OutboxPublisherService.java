package com.preshow.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.preshow.payment.dto.PaymentFailedEvent;
import com.preshow.payment.dto.PaymentSuccessEvent;
import com.preshow.payment.enums.OutboxStatus;
import com.preshow.payment.model.OutboxEvent;
import com.preshow.payment.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisherService {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 3000)
    @Transactional
    public void publish() {

        List<OutboxEvent> events =
                outboxEventRepository.findTop10ByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING);

        for (OutboxEvent event : events) {
            try {
                // Convert JSON → POJO
                Object payload =
                        switch (event.getTopic()) {
                            case "payment-success" ->
                                    objectMapper.readValue(
                                            event.getPayload(),
                                            PaymentSuccessEvent.class
                                    );
                            case "payment-failed" ->
                                    objectMapper.readValue(
                                            event.getPayload(),
                                            PaymentFailedEvent.class
                                    );
                            default -> throw new IllegalStateException("Unknown topic");
                        };

                // Send typed event
                kafkaTemplate
                        .send(event.getTopic(), event.getAggregateId(), payload)
                        .get();

                event.setStatus(OutboxStatus.SENT);
                outboxEventRepository.save(event);

            } catch (Exception ex) {
                log.error("Failed to publish outbox event {}", event.getId(), ex);
                break;
            }
        }
    }
}
