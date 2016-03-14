package io.nowave.api.controller;

import io.nowave.api.model.VideoEvent;
import io.nowave.api.repository.VideoEventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
public class AnalyticsController {

    private VideoEventsRepository repository;

    @Autowired
    public AnalyticsController(VideoEventsRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(path="/api/analytics", method = RequestMethod.POST)
    public ResponseEntity addAnalytics(@RequestBody final VideoEvent event, @AuthenticationPrincipal String userId) {
        event.setUserId(userId);
        this.repository.save(event);
        return new ResponseEntity<>(CREATED);
    }
}
