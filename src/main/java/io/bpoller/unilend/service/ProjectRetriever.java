package io.bpoller.unilend.service;

import io.bpoller.unilend.model.BidHistory;
import io.bpoller.unilend.repository.BidHistoryRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.TopicProcessor;

import java.io.IOException;

@Service
public class ProjectRetriever {

    @Autowired
    public ProjectRetriever(BidHistoryRetriever bidHistoryRetriever, DisplayDude displayDude, BidHistoryRepository bidHistoryRepository) {

        TopicProcessor<BidHistory> bidHistoriesTopic = TopicProcessor.create();
        bidHistoriesTopic.consume(displayDude::show);
        bidHistoriesTopic.flatMap(bidHistoryRepository::insert).subscribe();

        Flux
                .interval(5000)
                .flatMap(this::extractOngoingProjectIds)
                .flatMap(bidHistoryRetriever::retrieveHistory)
                .subscribeWith(bidHistoriesTopic);
    }

    Publisher<String> extractOngoingProjectIds(Long sequence) {
        try {
            Document doc = Jsoup.connect("https://unilend.fr/").get();
            Elements projectIds = doc.select("tr.unProjet>td>strong:not(:contains(Terminé))");

            return Flux.fromIterable(projectIds)
                    .map(Element::id)
                    .map(id -> id.replaceFirst("val", ""));

        } catch (IOException e) {
            return Flux.error(e);
        }
    }
}