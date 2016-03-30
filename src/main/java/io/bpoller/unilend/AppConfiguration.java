package io.bpoller.unilend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;
import io.bpoller.unilend.model.BidHistory;
import org.reactivestreams.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

import static reactor.core.publisher.TopicProcessor.create;

@Configuration
@EnableScheduling
public class AppConfiguration {

    @Value("${mongo.connectionurl}")
    private String mongoConnectionURL;


    @Bean
    public Processor<Integer, Integer> triggerTopic() {
        return create("triggerTopic");
    }

    @Bean
    public Processor<String, String> ongoingProjectsTopic() {
        return create("ongoingProjectsTopic");
    }

    @Bean
    public Processor<BidHistory, BidHistory> bidTopic() {
        return create("bidHistoryTopic");
    }

    @Bean
    ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json().build();
    }

    @Bean
    MongoDatabase mongoDatabase() {
        return MongoClients.create(mongoConnectionURL).getDatabase("unilend");
    }
}