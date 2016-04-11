package io.bpoller.unilend.model.project;


import io.bpoller.unilend.model.CQRSEvent;

public abstract class ProjectEvent extends CQRSEvent<String>{

    public ProjectEvent(String aggregateId) {
        super(aggregateId);
    }
}