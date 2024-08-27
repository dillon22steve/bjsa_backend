package com.dillonbrothers.bjsa_tool.controller;

import java.math.BigInteger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dillonbrothers.bjsa_tool.model.ExperimentalRequest;
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
        (@RequestParam String dealerUpCard, @RequestParam BigInteger executionTimes, 
            @RequestParam String action, @RequestParam String hand) {

        PlayerAction playerAction = PlayerAction.STAND;

        switch (action) {
            case "stand":
                break;
            case "double":
                playerAction = PlayerAction.DOUBLE;
                break;
            case "hit":
                playerAction = PlayerAction.HIT;
                break;
            case "split":
                playerAction = PlayerAction.SPLIT;
                break;
            default:
                return null;
        } //switch
        
        ExpectedValueResponse response = service.calculateExpectedValue(
            dealerUpCard, executionTimes, playerAction, hand);
        
        System.out.println(response.getExpectedValue());
        return response;
    } //calculateExpectedValue


    @PostMapping("/v1/run-experimental-strategy")
    public ExpectedValueResponse calculateExpectedValuePost (@RequestBody ExperimentalRequest request) {

        ExpectedValueResponse response = service.calculateExpectedValue(
            request.getDealerUpCard(), request.getExecutionTimes(), request.getAction(), request.getHand());
        
        System.out.println(response.getExpectedValue());
        return response;
    } //calculateExpectedValuePost

} //BjsaController
