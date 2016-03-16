package io.bpoller.unilend;

import org.reactivestreams.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import static reactor.core.publisher.TopicProcessor.create;

@Configuration
@EnableScheduling
public class AppConfiguration {

    @Bean
    public Processor<Integer, Integer> triggerTopic()
    {
        return create("triggerTopic", 4);
    }
}
