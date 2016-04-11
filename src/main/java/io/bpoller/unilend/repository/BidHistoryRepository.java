package io.bpoller.unilend.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import io.bpoller.unilend.model.BidHistory;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static com.mongodb.client.model.Filters.eq;

@Repository
public class BidHistoryRepository implements ReactiveRepository<BidHistory> {

    private final ObjectMapper mapper;
    private final MongoCollection<Document> collection;

    @Autowired
    public BidHistoryRepository(ObjectMapper mapper, MongoDatabase db) {
        collection = db.getCollection("BidHistory");
        this.mapper = mapper;
    }

    @Override
    public Mono<Void> insert(BidHistory elements) {
        try {
            Document doc = Document.parse(mapper.writeValueAsString(elements));
            return Mono.empty(collection.insertOne(doc));
        } catch (JsonProcessingException ex) {
            return Mono.error(ex);
        }
    }

    @Override
    public Flux<BidHistory> list() {
        return Flux.from(this.collection.find()).flatMap(doc -> {
            try {
                return Flux.just(mapper.readValue(doc.toJson(), BidHistory.class));
            } catch (IOException ex) {
                return Flux.error(ex);
            }
        });
    }

    @Override
    public Mono<BidHistory> findById(String id) {
        return Mono
                .from(this.collection.find(eq("id", id)))
                .then(doc -> {
                            try {
                                return Mono.just(mapper.readValue(doc.toJson(), BidHistory.class));
                            } catch (IOException ex) {
                                return Mono.error(ex);
                            }
                        }
                );
    }
}