package io.bpoller.unilend.repository;

import io.bpoller.unilend.model.BidHistory;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveRepository<T> {

    Mono<Void> insert(T element);

    Flux<T> list();

    Mono<T> findById(String id);
}