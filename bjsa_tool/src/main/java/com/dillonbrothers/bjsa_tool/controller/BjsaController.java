package com.dillonbrothers.bjsa_tool.controller;

import java.math.BigInteger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.dillonbrothers.bjsa_tool.model.PlayerAction;
import com.dillonbrothers.bjsa_tool.service.BjsaService;
import com.dillonbrothers.results_response.ExpectedValueResponse;


@RestController
public class BjsaController {

    private BjsaService service;

    
    public BjsaController(BjsaService service) {
        this.service = service;
    } //constructor


    @GetMapping("/v1/run-experimental-strategy")
    public ExpectedValueResponse calculateExpectedValue
        (@PathVariable String dealerUpCard, @PathVariable BigInteger executionTimes, 
            @PathVariable PlayerAction deviation, @PathVariable String startingHand) {
        
        ExpectedValueResponse response = service.calculateExpectedValue(dealerUpCard, executionTimes, deviation, startingHand);
        return response;
    } //calculateExpectedValue

} //BjsaController
