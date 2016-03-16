package io.bpoller.unilend.service;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Flux.from;

@Service
public class OngoingProjectRetriever {

    private Logger logger = getLogger(getClass());

    @Autowired
    OngoingProjectRetriever(Publisher<Integer> triggerPublisher) {
        from(triggerPublisher).consume(this::onTrigger);
    }

    private void onTrigger(Integer integer) {
        logger.info("Received signal from Scheduler {}.", integer);
    }
}