package io.bpoller.unilend.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.bpoller.unilend.model.BidHistory;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class DisplayDude {

    private Logger logger = getLogger(getClass());

    private ObjectMapper objectMapper;

    @Autowired
    public DisplayDude(Publisher<BidHistory> bidTopic, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        Flux.from(bidTopic).consume(this::show);
    }

    private void show(BidHistory bidHistory) {
        logger.info("Ok we got it to display {}", toJSON(bidHistory.reduceByInterestRate()));
    }


    private String toJSON(Object bid) {
        try {
            return objectMapper.writeValueAsString(bid);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "error";
        }
    }
}