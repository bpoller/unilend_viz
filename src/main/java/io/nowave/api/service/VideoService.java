package io.nowave.api.service;

import io.nowave.api.dto.vhx.Authorization;
import io.nowave.api.dto.vhx.Trailer;
import io.nowave.api.model.User;

import java.util.Optional;

public interface VideoService {
    /**
     * Retrieves an authorization token for viewing a video. The authorization has a default expiration time of 1 hour.
     * An authorization is given for a user that has subscribed to a subscription and a video that is part of a
     * collection that is part of that subscription.
     *
     * @param user       The user for whom the authorization is asked
     * @param videoTitle The video's title. Note, search is fuzzy.
     * @return An authorization, including an embeddable iFrame.
     */
    Optional<Authorization> getVideoAccessAuthorization(User user, String videoTitle);

    /**
     * Retrieves a trailer embeddable frame. For a trailer to be visible, it must be declared as the
     * trailer of a collection in VHX.
     * @param videoTitle The video's title. Note, search is fuzzy.
     * @return A trailer, including en embeddable iFrame.
     */
    Optional<Trailer> getTrailerFor(String videoTitle);
}