package io.bpoller.unilend.model;


import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BidHistory {

    private String projectId;

    private Set<Bid> bids;

    private Date timestamp;

    public BidHistory(String projectId, Set<Bid> bids, Date timestamp) {
        this.projectId = projectId;
        this.bids = bids;
        this.timestamp = timestamp;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public String getProjectId() {
        return projectId;
    }

    public Map<String, Integer> reduceByInterestRate() {

        return bids.stream().collect(Collectors.groupingBy(Bid::getInterest,
                Collectors.reducing(0, Bid::getAmount, Integer::sum)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BidHistory)) return false;

        BidHistory that = (BidHistory) o;

        return getProjectId().equals(that.getProjectId());

    }

    @Override
    public int hashCode() {
        return getProjectId().hashCode();
    }
}