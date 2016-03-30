package io.bpoller.unilend.model;

import java.util.Date;

public class Bid {

    private String sequence;

    private int amount;

    private String interest;

    private Date timestamp;

    private Bid() {
    }

    public Bid(String sequence, int amount, String interest, Date timestamp) {
        this.sequence = sequence;
        this.amount = amount;
        this.interest = interest;
        this.timestamp = timestamp;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bid)) return false;

        Bid bid = (Bid) o;

        return getSequence().equals(bid.getSequence());

    }

    @Override
    public int hashCode() {
        return getSequence().hashCode();
    }
}