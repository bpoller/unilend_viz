package io.nowave.api.dto;

public class ClientConfig {

    private String stripeKey;

    private String mixpanelKey;

    private ClientConfig() {
    }

    public ClientConfig(String stripeKey, String mixpanelKey) {

        this.stripeKey = stripeKey;
        this.mixpanelKey = mixpanelKey;
    }

    public String getStripeKey() {
        return stripeKey;
    }

    public String getMixpanelKey() {
        return mixpanelKey;
    }
}
