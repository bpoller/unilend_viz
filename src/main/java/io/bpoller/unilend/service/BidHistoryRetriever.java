package io.bpoller.unilend.service;

import io.bpoller.unilend.model.Bid;
import io.bpoller.unilend.model.BidHistory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class BidHistoryRetriever {

    Publisher<BidHistory> retrieveHistory(String projectId) {
        Map<String, String> params = new HashMap<>();
        params.put("id", projectId);
        params.put("tri", "ordre");
        params.put("direction", "1");

        try {
            Document doc = Jsoup.connect("https://www.unilend.fr/ajax/displayAll").data(params).post();
            Elements bidsRows = doc.select("tr");
            Set<Bid> bids = bidsRows.stream().skip(1).map(this::toBid).collect(toSet());
            return Mono.just(new BidHistory(projectId, bids, new Date()));
        } catch (IOException e) {
            return Mono.error(e);
        }
    }

    private Bid toBid(Element element) {
        Elements tds = element.select("td");
        String sequence = tds.get(0).text();
        String interest = tds.get(1).text().replace("%", "").replace(",", ".").trim();
        int amount = Integer.parseInt(tds.get(2).text().replace("€", "").replace(" ", ""));
        return new Bid(sequence, amount, interest, new Date());
    }
}