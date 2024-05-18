package com.dillonbrothers.bjsa_tool.model;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

import com.dillonbrothers.constants.CardConstants;
import com.dillonbrothers.pojos.Card;
import com.dillonbrothers.pojos.Hand;

public class EvDifferentialRequest {
	
	private char dealerUpCard;
	private BigInteger executionTimes;
	
	private PlayerAction playerAction;
	private DealerAction dealerAction;
	
	//comma separated format (A, 2, 3, ... 10), 2 values passed
	private String startingHand;

	private ArrayList<Hand> playerHands;
    private Hand dealerHand;

    private int bet, winnings, losses;
    private int handsWon, handsLost;

    private SecureRandom rng;


	public EvDifferentialRequest(String startingHand, char dealerUpCard, BigInteger executionTimes) {
		rng = null;
        try {
            rng = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        } //try

		this.executionTimes = executionTimes;


		String[] cardStrings = startingHand.split(",");
		char firstCard = cardStrings[0].trim().charAt(0);
		char secondCard = cardStrings[1].trim().charAt(0);
		Hand playerHandToAdd = new Hand();
		Hand dealerHandToAdd = new Hand();


		for (int i = 0; i < CardConstants.CARDS.length; i++) {
			if (CardConstants.CARDS[i].getCardType() == firstCard) {
				playerHandToAdd.addCardToHand(CardConstants.CARDS[i]);
			} //if

			if (CardConstants.CARDS[i].getCardType() == secondCard) {
				playerHandToAdd.addCardToHand(CardConstants.CARDS[i]);
			} //if

			if (CardConstants.CARDS[i].getCardType() == dealerUpCard) {
				dealerHandToAdd.addCardToHand(CardConstants.CARDS[i]);
			} //if
		} //for

		playerHands.add(playerHandToAdd);


        winnings = 0;
        losses = 0;
        handsWon = 0;
        handsLost = 0;
	} //constructor


	public void calculateExpectedValue() {
		BigInteger currentIteration = new BigInteger("0");
        while (executionTimes.compareTo(currentIteration) > 0) {
            boolean playerBusted = false;
            performDeviation();

            for (Hand currentHand : playerHands) {
                if (currentHand.hasBlackJack()) {
                    winnings = winnings + (this.bet * (3 / 2));
                    handsWon++;
                } else {
                    while (playerAction != PlayerAction.STAND && !playerBusted) {
						preformBasicStrategy(currentHand);
						if (currentHand.getValue() > 21) {
							playerBusted = true;
							losses = losses + bet;
							handsLost++;
						} //if
					} //while

					while (dealerHand.getValue() < 16) {
						dealerHand.addCardToHand(generateRandomCard());
					} //while
                } //if
            } //for
            
            currentIteration.add(BigInteger.ONE);
        } //while
    } //calculateExpectedValue


    private Card generateRandomCard() {
        int randIndex = (int)(rng.nextInt(CardConstants.CARDS.length));
        Card randCard = CardConstants.CARDS[randIndex];

        return randCard;
    } //generateRandomCard


    private void performDeviation() {

    } //performDeviation


    private void preformBasicStrategy(Hand currentHand) {
        
	} //calculateExpectedValue

} //EvDifferentialRequest