package io.nowave.api.model;

import java.time.LocalDate;

public abstract class NoWaveSubscription {

    private String planId;
    private Long amount;
    private LocalDate startDate;
    private String customerId;

    public NoWaveSubscription(String planId, Long amount){
        this.planId = planId;
        this.amount = amount;
        this.startDate = LocalDate.now();
    }


    public String getPlanId() {
        return planId;
    }

    public Long getAmount() {
        return amount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}