package io.bpoller.unilend.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Flux.from;

@Service
public class ProjectRetriever {

    private Logger logger = getLogger(getClass());

    @Autowired
    ProjectRetriever(Publisher<Integer> triggerTopic, Subscriber<String> ongoingProjectsTopic) {
        from(triggerTopic)
                .flatMap(this::mapper,8)
                .subscribeWith(ongoingProjectsTopic);
    }

    private Publisher<String> mapper(Integer integer) {
        try {
            return Flux.fromStream(extractOngoingProjectIds());
        } catch (IOException e) {
            logger.error("Error while retrieving document", e);
            return Flux.empty();
        }
    }

    Stream<String> extractOngoingProjectIds() throws IOException {
        Document doc = Jsoup.connect("https://unilend.fr/").get();
        Elements projectIds = doc.select("tr.unProjet>td>strong:not(:contains(Terminé))");
        return projectIds.stream().map(Element::id).map(id -> id.replaceFirst("val", ""));
    }
}