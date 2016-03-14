package io.nowave.api.dto.vhx;

import java.util.List;
import java.util.Optional;

public class VideoEmbed {

    List<VhxVideo> videos;

    private VideoEmbed() {
    }

    public List<VhxVideo> getVideos() {
        return videos;
    }

    public Optional<VhxVideo> video() {
        return videos.stream().findFirst();
    }

    public Optional<VhxVideo> trailer() {
        return videos.stream().skip(1).findFirst();
    }
}
