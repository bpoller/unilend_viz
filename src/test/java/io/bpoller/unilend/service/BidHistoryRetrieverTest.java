package io.bpoller.unilend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.bpoller.unilend.model.Bid;
import io.bpoller.unilend.model.BidHistory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Map;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.slf4j.LoggerFactory.getLogger;

public class BidHistoryRetrieverTest {

    private Logger logger = getLogger(getClass());

    @Mock
    private Publisher<String> publisher;

    @Mock
    private Subscriber<BidHistory> subscriber;

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
        Map<String, Integer> histoValues = bidHistoryRetriever.retrieveHistory("28802").reduceByInterestRate();

        histoValues.entrySet()
                .stream()
                .sorted(this::sort)
                .forEach((entry)->logger.info("{}%-{}€", entry.getKey(), entry.getValue()));
    }

    private int sort(Map.Entry<String, Integer> left, Map.Entry<String, Integer> right) {
        Float leftF = Float.parseFloat(left.getKey());
        Float rightF = Float.parseFloat(right.getKey());
        return Float.compare(leftF, rightF);
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