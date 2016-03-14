package io.nowave.api.dto;

import io.nowave.api.model.User;

public class Signup {
    private String stripeToken;
    private User user;

    public Signup(){

    }

    public String getStripeToken() {
        return stripeToken;
    }

    public void setStripeToken(String stripeToken) {
        this.stripeToken = stripeToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
