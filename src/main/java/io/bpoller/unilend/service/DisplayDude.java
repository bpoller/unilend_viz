package io.bpoller.unilend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.bpoller.unilend.model.BidHistory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.tuple.Tuple;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class DisplayDude {

    private Logger logger = getLogger(getClass());

    private ObjectMapper objectMapper;

    @Autowired
    public DisplayDude(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    void show(BidHistory bidHistory) {

        logger.info("Project {}", bidHistory.getProjectId());

        bidHistory.reduceByInterestRate().entrySet()
                .stream()
                .map(entry -> Tuple.of(entry.getKey(), entry.getValue()))
                .sorted(this::sort)
                .collect(AmountSummarizer::new, AmountSummarizer::accept, AmountSummarizer::combine)
                .stream()
                .forEach((entry) -> logger.info("{}%-{}€---{}€", entry.get(0), entry.get(1), entry.get(2)));

        logger.info("----------------");
    }

    private int sort(Tuple left, Tuple right) {
        Float leftF = Float.parseFloat((String) left.get(0));
        Float rightF = Float.parseFloat((String) right.get(0));
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