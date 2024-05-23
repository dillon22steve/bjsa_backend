package com.dillonbrothers.results_response;

public class ExpectedValueResponse {

    private ExpectedValueResult basicStrategy;

    private ExpectedValueResult experimentalStrategy;

    private String expectedValue;



    public ExpectedValueResponse() {

    } //constructor


    public ExpectedValueResponse(ExpectedValueResult basicStrategy, ExpectedValueResult experimentalStrategy) {
        this.basicStrategy = basicStrategy;
        this.experimentalStrategy = experimentalStrategy;

        //Still Figure out the experimental strategy part

    } //constructor



    public ExpectedValueResult getBasicStrategy() {
        return basicStrategy;
    }


    public void setBasicStrategy(ExpectedValueResult basicStrategy) {
        this.basicStrategy = basicStrategy;
    }


    public ExpectedValueResult getExperimentalStrategy() {
        return experimentalStrategy;
    }


    public void setExperimentalStrategy(ExpectedValueResult experimentalStrategy) {
        this.experimentalStrategy = experimentalStrategy;
    }


    public String getExpectedValue() {
        return expectedValue;
    }


    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }
    
} //Response
