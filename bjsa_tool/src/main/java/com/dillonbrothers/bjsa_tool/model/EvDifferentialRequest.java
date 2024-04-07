package com.dillonbrothers.bjsa_tool.model;

import java.math.BigInteger;

public class EvDifferentialRequest {
	
	private String dealerUpCard;
	private BigInteger executionTimes;
	
	private PlayerAction action;
	
	//comma separated format (A, 2, 3, ... 10), 2 values passed
	private String hand;

}
