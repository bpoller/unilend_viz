package io.bpoller.unilend.service;

import io.bpoller.unilend.model.Bid;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static reactor.core.publisher.Flux.from;
import static reactor.core.publisher.SchedulerGroup.async;
import static reactor.core.publisher.SchedulerGroup.io;

@Service
public class BidHistoryRetriever {

    @Autowired
    BidHistoryRetriever(Publisher<String> ongoingProjectsTopic, Subscriber<Bid> bidHistoryTopic) {
        from(ongoingProjectsTopic)
                .dispatchOn(async())
                .flatMap(this::mapper, 8)
                .subscribeWith(bidHistoryTopic);
    }

    private Publisher<Bid> mapper(String projectId) {
        try {
            return retrieveHistory(projectId).publishOn(io());
        } catch (Exception e) {
            return Flux.error(e);
        }
    }

    public Flux<Bid> retrieveHistory(String projectId) throws InterruptedException, IOException {

        Map<String, String> params = new HashMap<>();
        params.put("id", projectId);
        params.put("tri", "ordre");
        params.put("direction", "1");

        Document doc = Jsoup.connect("https://www.unilend.fr/ajax/displayAll").data(params).post();
        Elements bidsRows = doc.select("tr");

        return Flux.fromStream(bidsRows.stream().skip(1).map((bid)->toBid(bid, projectId)));
    }


    private Bid toBid(Element element, String projectId) {
        Elements tds = element.select("td");
        String sequence = tds.get(0).text();
        String interest = tds.get(1).text();
        int amount = Integer.parseInt(tds.get(2).text().replace("€", "").replace(" ", ""));
        return new Bid(projectId,sequence, amount, interest);
    }
}