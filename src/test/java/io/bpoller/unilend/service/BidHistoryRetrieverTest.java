package io.bpoller.unilend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.bpoller.unilend.model.Bid;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.io.IOException;

import static org.mockito.MockitoAnnotations.initMocks;

public class BidHistoryRetrieverTest {


    @Mock
    private Publisher<String> publisher;

    @Mock
    private Subscriber<Bid> subscriber;

    private BidHistoryRetriever bidHistoryRetriever;

    private ObjectMapper objectMapper;

    @Before
    public void init() {
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        initMocks(this);
        bidHistoryRetriever = new BidHistoryRetriever(publisher, subscriber);
    }

    @Test
    public void shouldRetrieveIds() throws IOException, InterruptedException {
        bidHistoryRetriever.retrieveHistory("28802").consume((bid)-> System.out.println(toJSON(bid)));
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