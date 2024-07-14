package com.dillonbrothers.bjsa_tool.service;

import java.math.BigInteger;

import org.springframework.stereotype.Service;

import com.dillonbrothers.bjsa_tool.model.EvDifferentialRequest;
import com.dillonbrothers.bjsa_tool.model.PlayerAction;
import com.dillonbrothers.results_response.ExpectedValueResponse;
import com.dillonbrothers.results_response.ExpectedValueResult;


@Service
public class BjsaService {

    private EvDifferentialRequest request;

    
    public ExpectedValueResponse calculateExpectedValue
        (String dealerUpCardString, BigInteger executionTimes, PlayerAction deviation, String startingHand) {
        char dealerUpCardChar = determineDealerUpCardChar(dealerUpCardString);

        request = new EvDifferentialRequest(startingHand, dealerUpCardChar, executionTimes, 20, deviation, false);
        ExpectedValueResult basicStrategyResult = request.calculateExpectedValue();
        System.out.println("Previous call finished");

        System.out.println("Calling EVDifferentialRequest");
        request = new EvDifferentialRequest(startingHand, dealerUpCardChar, executionTimes, 20, deviation, true);
        ExpectedValueResult experimentalResult = request.calculateExpectedValue();

        return new ExpectedValueResponse(basicStrategyResult, experimentalResult);
    } //calculateExpectedValue


    private char determineDealerUpCardChar(String dealerUpCardString) {
        if (dealerUpCardString.trim().equals("10")) {
            return 't';
        } else {
            return dealerUpCardString.trim().charAt(0);
        } //if
    } //determineDealerUpCardChar

} //BjsaService
