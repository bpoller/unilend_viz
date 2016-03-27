package io.bpoller.unilend.model;

public class Bid {

    private String sequence;

    private int amount;

    private String interest;

    private String projectId;

    private Bid() {
    }

    public Bid(String projectId, String sequence, int amount, String interest) {
        this.sequence = sequence;
        this.amount = amount;
        this.interest = interest;
        this.projectId = projectId;
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

    public String getProjectId() {
        return projectId;
    }
}