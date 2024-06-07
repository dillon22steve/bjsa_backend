package com.dillonbrothers.bjsa_tool.model;

import java.math.BigInteger;

public class ExperimentalRequest {
    public ExperimentalRequest() {

    }

    private String dealerUpCard;
    private BigInteger executionTimes;
    private PlayerAction action;
    private String hand;

    
    public String getDealerUpCard() {
        return dealerUpCard;
    }
    public void setDealerUpCard(String dealerUpCard) {
        this.dealerUpCard = dealerUpCard;
    }
    public BigInteger getExecutionTimes() {
        return executionTimes;
    }
    public void setExecutionTimes(BigInteger executionTimes) {
        this.executionTimes = executionTimes;
    }
    public PlayerAction getAction() {
        return action;
    }
    public void setAction(PlayerAction action) {
        this.action = action;
    }
    public String getHand() {
        return hand;
    }
    public void setHand(String hand) {
        this.hand = hand;
    }


    

}