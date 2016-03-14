package io.nowave.api.dto.vhx;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VhxCustomer {

    long id;
    String name;
    String email;
    @JsonProperty("created_at")
    String createdAt;
    @JsonProperty("updated_at")
    String updatedAt;

    private VhxCustomer() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}