package io.bpoller.unilend.service;


import io.bpoller.unilend.repository.BidHistoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import reactor.core.publisher.Flux;

import java.io.IOException;

import static org.mockito.MockitoAnnotations.initMocks;

public class ProjectRetrieverTest {

    @Mock
    private BidHistoryRetriever bidHistoryRetriever;

    @Mock
    private DisplayDude displayDude;

    @Mock
    private BidHistoryRepository bidHistoryRepository;

    private ProjectRetriever projectRetriever;

    @Before
    public void init() {
        initMocks(this);
        projectRetriever = new ProjectRetriever(bidHistoryRetriever, displayDude, bidHistoryRepository);
    }

    @Test
    public void shouldRetrieveIds() throws IOException {
        Flux
                .from(projectRetriever.extractOngoingProjectIds(12L))
                .consume(System.out::println);
    }
}