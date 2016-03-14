package io.nowave.api.controller;


import io.nowave.api.dto.vhx.Authorization;
import io.nowave.api.dto.vhx.Trailer;
import io.nowave.api.model.User;
import io.nowave.api.repository.UserRepository;
import io.nowave.api.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.*;

@RestController
public class VideoController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoService videoService;

    @RequestMapping("/api/video/{videoTitle}")
    public ResponseEntity<Authorization> getVideoEmbed(@PathVariable String videoTitle, @AuthenticationPrincipal String userId) {
        return ofNullable(userRepository.findOne(userId))
                .filter(User::hasSubscribed)
                .map(user -> videoService.getVideoAccessAuthorization(user, videoTitle))
                .map(authorizationOptional-> authorizationOptional
                                                .map(authorization -> new ResponseEntity<>(authorization, OK))
                                                .orElse(new ResponseEntity<>(NOT_FOUND)) )
                .orElse(new ResponseEntity<>(FORBIDDEN));
    }

    @RequestMapping("/api/video/{videoTitle}/trailer")
    public ResponseEntity<Trailer> getVideoEmbed(@PathVariable String videoTitle) {
        return    videoService
                .getTrailerFor(videoTitle)
                .map(trailer->new ResponseEntity<>(trailer,OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }
}