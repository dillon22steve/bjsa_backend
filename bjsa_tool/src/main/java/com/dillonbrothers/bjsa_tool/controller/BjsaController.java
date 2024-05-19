package com.dillonbrothers.bjsa_tool.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dillonbrothers.bjsa_tool.service.BjsaService;
import com.dillonbrothers.results_response.ExpectedValueResponse;


@RestController
public class BjsaController {

    private BjsaService service;

    public BjsaController() {

    } //constructor

    // @GetMapping(path = "/v1/run-experimental-strategy")
    // public ExpectedValueResponse calculateExpectedValue() {
        
    // }

}
