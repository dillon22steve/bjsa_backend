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

		String startingHand = "4, 8";
		char dealerUpCard = '3';
		BigInteger numExecutions = new BigInteger("5");

		
		EvDifferentialRequest request = new EvDifferentialRequest(
			startingHand, dealerUpCard, numExecutions, 20, PlayerAction.STAND, true);

		ExpectedValueResult experimentalStrategyResult = request.calculateExpectedValue();


		System.out.println();
		System.out.println("Basic Strategy simulation");
		System.out.println();


		request = new EvDifferentialRequest(
			startingHand, dealerUpCard, new BigInteger("5"), 20, PlayerAction.STAND, false);

		ExpectedValueResult basicStrategyResult = request.calculateExpectedValue();


		System.out.println("Results");
		System.out.println("Experimental: " + experimentalStrategyResult.getHandsWon() + 
			" won vs " + experimentalStrategyResult.getHandsLost() + " lost");
		System.out.println("Basic:" + basicStrategyResult.getHandsWon() + " won vs " + basicStrategyResult.getHandsLost() + " lost");
	} //main

} //BjsaToolApplication
