package com.dillonbrothers.bjsa_tool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.dillonbrothers.bjsa_tool.model.EvDifferentialRequest;
import com.dillonbrothers.bjsa_tool.model.PlayerAction;
import com.dillonbrothers.results_response.ExpectedValueResult;

import java.math.BigInteger;

@SpringBootApplication
public class BjsaToolApplication {

	public static void main(String[] args) {
		SpringApplication.run(BjsaToolApplication.class, args);
		
		EvDifferentialRequest request = new EvDifferentialRequest(
			"A, 3", '2', new BigInteger("5"), 20, PlayerAction.DOUBLE, true);

		ExpectedValueResult experimentalStrategyResponse = request.calculateExpectedValue();
		System.out.println(experimentalStrategyResponse.getHandsWon() + " won vs " + experimentalStrategyResponse.getHandsLost() + " lost");
	} //main

} //BjsaToolApplication
