package io.bpoller.unilend.service;

import io.bpoller.unilend.model.BidHistory;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import reactor.core.publisher.Flux;
import reactor.core.tuple.Tuple;

import java.io.IOException;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.slf4j.LoggerFactory.getLogger;

public class BidHistoryRetrieverTest {

    private Logger logger = getLogger(getClass());

    private BidHistoryRetriever bidHistoryRetriever;

    @Before
    public void init() {
        initMocks(this);
        bidHistoryRetriever = new BidHistoryRetriever();
    }

    @Test
    public void shouldRetrieveIds() throws IOException, InterruptedException {

        long t = System.currentTimeMillis();

        Flux
                .from(bidHistoryRetriever.retrieveHistory("30559"))
                .map(bidHisto -> {
                    logger.info("Request Delay : {}", System.currentTimeMillis() - t);
                    return bidHisto.reduceByInterestRate();
                })
                .consume(histoValues -> histoValues.entrySet()
                        .stream()
                        .map(entry -> Tuple.of(entry.getKey(), entry.getValue()))
                        .sorted(this::sort)
                        .collect(AmountSummarizer::new, AmountSummarizer::accept, AmountSummarizer::combine)
                        .stream()
                        .forEach((entry) -> logger.info("{}%-{}€---{}€", entry.get(0), entry.get(1), entry.get(2))));
    }

    private int sort(Tuple left, Tuple right) {
        Float leftF = Float.parseFloat((String) left.get(0));
        Float rightF = Float.parseFloat((String) right.get(0));
        return Float.compare(leftF, rightF);
    }
}