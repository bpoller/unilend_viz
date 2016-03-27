package io.bpoller.unilend.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import io.bpoller.unilend.model.Bid;
import org.bson.Document;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static com.mongodb.client.model.Filters.eq;

@Repository
public class BidRepository implements ReactiveRepository<Bid> {

    private final ObjectMapper mapper;
    private final MongoCollection<Document> col;

    @Autowired
    public BidRepository(ObjectMapper mapper, MongoDatabase db) {
        col = db.getCollection("Bids");
        this.mapper = mapper;
    }

    @Override
    public Mono<Void> insert(Publisher<Bid> elements) {
        return Flux.from(elements).flatMap(p -> {
            try {
                Document doc = Document.parse(mapper.writeValueAsString(p));
                return Mono.from(col.insertOne(doc));
            } catch (JsonProcessingException ex) {
                return Mono.error(ex);
            }
        }).after();
    }

    @Override
    public Flux<Bid> list() {
        return Flux.from(this.col.find()).flatMap(doc -> {
            try {
                return Flux.just(mapper.readValue(doc.toJson(), Bid.class));
            }
            catch (IOException ex) {
                return Flux.error(ex);
            }
        });
    }

    @Override
    public Mono<Bid> findById(String id) {
        return Mono.from(this.col.find(eq("id", id))).then(doc -> {
            try {
                return Mono.just(mapper.readValue(doc.toJson(), Bid.class));
            }
            catch (IOException ex) {
                return Mono.error(ex);
            }
        });
    }
}