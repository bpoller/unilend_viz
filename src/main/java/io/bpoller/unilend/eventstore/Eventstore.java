package io.bpoller.unilend.eventstore;

import io.bpoller.unilend.model.CQRSEvent;
import org.reactivestreams.Publisher;

import java.io.Serializable;

public interface Eventstore<I extends Serializable, E extends CQRSEvent<I>> extends Publisher<E> {

    Publisher<Void> store(E event);

    Publisher<E> replay(I aggregatId);

    Publisher<Void> snapshot(I aggregateId);
}