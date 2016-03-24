package io.bpoller.unilend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Bid {

    private String sequence;

    private int amount;

    private String interest;


    private Bid() {
    }

    public Bid(String sequence, int amount, String interest) {
        this.sequence = sequence;
        this.amount = amount;
        this.interest = interest;
    }

    public String getSequence() {
        return sequence;
    }

    public int getAmount() {
        return amount;
    }

    public String getInterest() {
        return interest;
    }

}