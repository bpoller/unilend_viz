package io.bpoller.unilend.eventstore.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.reactivestreams.client.Success;
import io.bpoller.unilend.eventstore.Eventstore;
import io.bpoller.unilend.model.project.ProjectEvent;
import org.bson.Document;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.TopicProcessor;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class ProjectEventStore implements Eventstore<String, ProjectEvent> {

    private final MongoCollection<Document> collection;
    private final TopicProcessor<ProjectEvent> topic;
    private final ObjectMapper mapper;

    private Logger logger = getLogger(getClass());

    @Autowired
    public ProjectEventStore(MongoDatabase db, ObjectMapper mapper) {
        topic = TopicProcessor.create();
        collection = db.getCollection("ProjectEvents");
        this.mapper = mapper;
    }

    @Override
    public Mono<Void> store(ProjectEvent event) {

        return Flux.just(event)
                .flatMap(e -> {
                    try {
                        return Mono.just(mapper.writeValueAsString(e));
                    } catch (JsonProcessingException exception) {
                        return Mono.error(exception);
                    }
                })
                .map(Document::parse)
                .flatMap(collection::insertOne)
                .map(s-> event)

                .flatMap(this::sendToTopic)
                .after();
    }

    private Publisher<Void> sendToTopic(ProjectEvent projectEvent) {
        logger.info("I gotta push this into the topic now");
        topic.onNext(projectEvent);
        return Mono.empty().after();
    }


    @Override
    public Publisher<ProjectEvent> replay(String aggregatId) {
        return null;
    }

    @Override
    public Publisher<Void> snapshot(String aggregateId) {
        return Mono.error(new Exception("not implemented yet"));
    }

    @Override
    public void subscribe(Subscriber<? super ProjectEvent> s) {
        topic.subscribe(s);

    }
}