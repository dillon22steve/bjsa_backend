package com.dillonbrothers.bjsa_tool.service;

import java.math.BigInteger;

import org.springframework.stereotype.Service;

import com.dillonbrothers.bjsa_tool.model.EvDifferentialRequest;
import com.dillonbrothers.bjsa_tool.model.PlayerAction;
import com.dillonbrothers.results_response.ExpectedValueResponse;


@Service
public class BjsaService {

    private EvDifferentialRequest request;

    
    public ExpectedValueResponse calculateExpectedValue
        (String dealerUpCardString, BigInteger executionTimes, PlayerAction deviation, String startingHand) {
        char dealerUpCardChar = determineDealerUpCardChar(dealerUpCardString);
        request = new EvDifferentialRequest(startingHand, dealerUpCardChar, executionTimes, 20, deviation);
        return request.calculateExpectedValue();
    }


    private char determineDealerUpCardChar(String dealerUpCardString) {
        if (dealerUpCardString.trim().equals("10")) {
            return 't';
        } else {
            return dealerUpCardString.trim().charAt(0);
        } //if
    } //determineDealerUpCardChar

} //BjsaService
