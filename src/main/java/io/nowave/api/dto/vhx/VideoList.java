package io.nowave.api.dto.vhx;


import com.fasterxml.jackson.annotation.JsonProperty;

public class VideoList {


    @JsonProperty("_embedded")
    VideoEmbed videoEmbed;

    private VideoList() {

    }

    public VideoEmbed getVideoEmbed() {
        return videoEmbed;
    }
}
