package io.bpoller.unilend.service;

import io.bpoller.unilend.model.Bid;
import io.bpoller.unilend.model.BidHistory;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Flux.from;
import static reactor.core.publisher.SchedulerGroup.async;
import static reactor.core.publisher.SchedulerGroup.io;

@Service
public class BidHistoryRetriever {

    private Logger logger = getLogger(getClass());

    @Autowired
    BidHistoryRetriever(Publisher<String> ongoingProjectsTopic, Subscriber<BidHistory> bidHistoryTopic) {
        from(ongoingProjectsTopic)
                .dispatchOn(async())
                .flatMap(this::mapper, 8)
                .subscribeWith(bidHistoryTopic);
    }

    private Publisher<BidHistory> mapper(String projectId) {
        return Mono.fromCallable(() -> retrieveHistory(projectId))
                .publishOn(io());
    }

    public BidHistory retrieveHistory(String projectId) throws InterruptedException, IOException {

        Map<String, String> params = new HashMap<>();
        params.put("id", projectId);
        params.put("tri", "ordre");
        params.put("direction", "1");

        Document doc = Jsoup.connect("https://www.unilend.fr/ajax/displayAll").data(params).post();
        Elements bidsRows = doc.select("tr");

        List<Bid> bids = bidsRows.stream().skip(1).map(this::toBid).collect(toList());
        return new BidHistory(projectId, bids);
    }

    private Bid toBid(Element element) {
        Elements tds = element.select("td");
        String sequence = tds.get(0).text();
        String interest = tds.get(1).text();
        int amount = Integer.parseInt(tds.get(2).text().replace("€","").replace(" ",""));
        return new Bid(sequence, amount, interest);
    }
}