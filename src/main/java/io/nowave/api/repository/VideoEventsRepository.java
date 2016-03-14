package io.nowave.api.repository;

import io.nowave.api.model.VideoEvent;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface VideoEventsRepository extends MongoRepository<VideoEvent, String> {
}