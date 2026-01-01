package com.preshow.showquery.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Bean
    public RecordMessageConverter converter(ObjectMapper objectMapper) {
        // This bean tells Spring to use the method parameter type
        // to convert the incoming JSON string/map.
        return new JsonMessageConverter(objectMapper);
    }
}