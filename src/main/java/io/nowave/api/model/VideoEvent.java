package io.nowave.api.model;

/**
 * Created by olivier on 11/03/16.
 */
public class VideoEvent {
    private String userId;
    private String videoId;
    private String event;

    private VideoEvent() {
    }

    public VideoEvent(String userId, String videoId, String event) {
        this.userId = userId;
        this.videoId = videoId;
        this.event = event;
    }

    public String getUserId() {
        return userId;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getEvent() {
        return event;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
