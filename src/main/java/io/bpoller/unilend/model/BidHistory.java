package io.bpoller.unilend.model;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BidHistory {

    private String projectId;

    private List<Bid> bids;

    public BidHistory(String projectId, List<Bid> bids) {
        this.projectId = projectId;
        this.bids = bids;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public String getProjectId() {
        return projectId;
    }

    public Map<String, Integer> reduceByInterestRate() {

        return bids.stream().collect(Collectors.groupingBy(Bid::getInterest,
                Collectors.reducing(0, Bid::getAmount, Integer::sum)));

    }
}