package io.bpoller.unilend.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public abstract class CQRSEvent<I extends Serializable> {

    @JsonProperty("_id")
    public final String eventId;

    public CQRSEvent(I aggregateId) {
        this.eventId = generateEventId(aggregateId);
    }

    /**
     * This method must be implemented by all sub classes. It generates an event
     * id that must be equal if two events of the same type with similar properties
     * shall be considered equal. The event id is used for implementing idem potent
     * behaviour of an event store.
     *
     * @param aggregateId
     * @return
     */
    protected abstract String generateEventId(I aggregateId);
}