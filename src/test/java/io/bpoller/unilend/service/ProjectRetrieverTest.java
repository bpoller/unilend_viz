package io.bpoller.unilend.service;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.io.IOException;

import static org.mockito.MockitoAnnotations.initMocks;

public class ProjectRetrieverTest {

    @Mock
    private Publisher<Integer> publisher;

    @Mock
    private Subscriber<String> subscriber;

    private ProjectRetriever projectRetriever;

    @Before
    public void init() {
        initMocks(this);
        projectRetriever = new ProjectRetriever(publisher, subscriber);
    }

    @Test
    public void shouldRetrieveIds() throws IOException {
        projectRetriever.extractOngoingProjectIds().forEach(System.out::println);
    }
}
