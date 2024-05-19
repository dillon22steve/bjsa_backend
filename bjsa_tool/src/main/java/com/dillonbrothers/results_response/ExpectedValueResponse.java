package com.dillonbrothers.results_response;

public class ExpectedValueResponse {

    private int handsWon;
    private int handsLost;
    private String expectedValue;


    public ExpectedValueResponse() {
        this.handsWon = 0;
        this.handsLost = 0;
        this.expectedValue = "";
    } //constructor


    public int getHandsWon() {
        return this.handsWon;
    } //getHandsWon

    public void incrementHandsWon() {
        handsWon = handsWon + 1;
    } //incrementHandsWon


    public int getHandsLost() {
        return this.handsLost;
    } //getHandsLost

    public void incrementHandsLost() {
        handsLost = handsLost + 1;
    } //incrementHandsLost


    public String getExpectedValue() {
        return this.expectedValue;
    } //getExpectedValue
    
} //ExpectedValueResponse
