package io.bpoller.unilend.repository;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveRepository<T> {

    Mono<Void> insert(Publisher<T> elements);

    Flux<T> list();

    Mono<T> findById(String id);
}