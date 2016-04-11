package io.bpoller.unilend.model.project;


public class ProjectCreatedEvent extends ProjectEvent {

    public ProjectCreatedEvent(String aggregateId) {
        super(aggregateId);
    }

    @Override
    protected String generateEventId(String aggregateId) {
        return aggregateId;
    }
}