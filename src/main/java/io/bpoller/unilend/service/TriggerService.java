package io.bpoller.unilend.service;


import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class TriggerService {

    private Logger logger = getLogger(getClass());
    private AtomicInteger i = new AtomicInteger(0);
    private Subscriber<Integer> subscriber;

    @Autowired
    public TriggerService(Subscriber<Integer> subscriber) {
        this.subscriber = subscriber;
    }

    @Scheduled(fixedRate = 5000)
    public void trigger() {
        logger.info("Scheduler triggered !");
        subscriber.onNext(i.incrementAndGet());
    }
}