package io.bpoller.unilend.service;


import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class SchedulerService {

    private Logger logger = getLogger(getClass());

    @Scheduled(fixedRate = 5000)
    public void trigger() {
        logger.info("Scheduler triggered !");
    }

}
