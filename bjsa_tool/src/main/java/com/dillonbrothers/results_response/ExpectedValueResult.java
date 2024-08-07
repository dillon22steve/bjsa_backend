package com.dillonbrothers.results_response;

import java.text.DecimalFormat;

public class ExpectedValueResult {

    private int handsWon;
    private int handsLost;

    private String expectedValue;


    public ExpectedValueResult() {
        this.handsWon = 0;
        this.handsLost = 0;
        this.expectedValue = "";
    } //constructor

    public ExpectedValueResult(int handsWon, int handsLost, int moneyWon, int moneyLost) {
        int calculation = (handsWon * moneyWon) - (handsLost * moneyLost);
        this.handsWon = handsWon;
        this.handsLost = handsLost;

        DecimalFormat df = new DecimalFormat("###,###,###");
        expectedValue = df.format(calculation);
    }


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
