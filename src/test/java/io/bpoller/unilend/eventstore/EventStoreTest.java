package io.bpoller.unilend.eventstore;

import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.reactivestreams.client.Success;
import io.bpoller.unilend.AppConfiguration;
import io.bpoller.unilend.TestAppConfig;
import io.bpoller.unilend.eventstore.project.ProjectEventStore;
import io.bpoller.unilend.model.project.ProjectCreatedEvent;
import io.bpoller.unilend.model.project.ProjectEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Flux;
import reactor.core.test.TestSubscriber;

import java.time.Duration;
import java.time.temporal.TemporalUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TestAppConfig.class, AppConfiguration.class})
public class EventStoreTest {

    @Autowired
    ProjectEventStore projectEventStore;

    @Autowired
    MongoDatabase db;


    @Before
    public void init() {

        TestSubscriber ts = new TestSubscriber<Success>();

        ts.bindTo(Flux
                .from(db.getCollection("ProjectEvents").drop())).await(Duration.ofSeconds(3));
    }

    @Test
    public void shouldCorrectlyStoreEventAndPublish() {

        ProjectCreatedEvent event = new ProjectCreatedEvent("123");
        TestSubscriber<Void> ts = new TestSubscriber<>();

        ts.bindTo(projectEventStore.store(event))
                .await(Duration.ofSeconds(5))
                .assertComplete();
    }

    @Test
    public void shouldPreventPublishingAnAlreadyStoredEvent() {

    }

}
