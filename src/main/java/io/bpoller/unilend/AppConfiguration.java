package io.bpoller.unilend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
public class AppConfiguration {

    @Value("${mongo.connectionurl}")
    private String mongoConnectionURL;

    @Value("${mongo.dbname}")
    private String mongoDbName;

    @Bean
    ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json().build();
    }

    @Bean
    MongoDatabase mongoDatabase() {


        return MongoClients.create(mongoConnectionURL).getDatabase(mongoDbName);
    }
}