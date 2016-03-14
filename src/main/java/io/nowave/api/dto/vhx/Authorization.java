package io.nowave.api.dto.vhx;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Authorization {

    private String token;

    private Player player;

    @JsonProperty("expires_at")
    private String expiresAt;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    private String newPlayer;

    private String videoId;

    private Authorization() {
    }

    public String getToken() {
        return token;
    }

    public Player getPlayer() {
        return player;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getNewPlayer() {
        return newPlayer;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public void setNewPlayer(String newPlayer) {
        this.newPlayer = newPlayer;
    }
}
