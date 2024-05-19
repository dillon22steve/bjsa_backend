package com.dillonbrothers.bjsa_tool.model;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

import com.dillonbrothers.constants.CardConstants;
import com.dillonbrothers.pojos.Card;
import com.dillonbrothers.pojos.Hand;
import com.dillonbrothers.results_response.ExpectedValueResponse;

public class EvDifferentialRequest {
	
	private char dealerUpCard;
	private int dealerUpCardValue;

	private BigInteger executionTimes;
	
	private PlayerAction playerAction;
	private DealerAction dealerAction;
	
	//comma separated format (A, 2, 3, ... 10), 2 values passed
	private String startingHand;

	private ArrayList<Hand> playerHands;
    private Hand dealerHand;

    private int bet, winnings, losses, origBet;
    private ExpectedValueResponse response;

    private SecureRandom rng;


	public EvDifferentialRequest(String startingHand, char dealerUpCard, BigInteger executionTimes, int origBet) {
		rng = null;
        try {
            rng = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        } //try

		this.executionTimes = executionTimes;

		this.startingHand = startingHand;
		this.dealerUpCard = dealerUpCard;

		createPlayerAndDealerHands();

        this.winnings = 0;
        this.losses = 0;

		this.origBet = origBet;
		this.bet = origBet;

        response = new ExpectedValueResponse();
	} //constructor


	private void createPlayerAndDealerHands() {
		//Converting the String parameter into 
		String[] cardStrings = startingHand.split(",");
		char firstCard, secondCard;
		if (cardStrings[0].trim().equals("10")) {
			firstCard = 't';
		} else {
			firstCard = cardStrings[0].trim().charAt(0);
		} //if

		if (cardStrings[1].trim().equals("10")) {
			secondCard = 't';
		} else {
			secondCard = cardStrings[1].trim().charAt(0);
		} //if


		Card playerFirstCard = null;
		Card playerSecondCard = null;
		Card dealerCard = null;


		for (int i = 0; i < CardConstants.CARDS.length; i++) {
			if (CardConstants.CARDS[i].getCardType() == firstCard) {
				playerFirstCard = CardConstants.CARDS[i];
			} //if

			if (CardConstants.CARDS[i].getCardType() == secondCard) {
				playerSecondCard = CardConstants.CARDS[i];
			} //if

			if (CardConstants.CARDS[i].getCardType() == dealerUpCard) {
				dealerCard = CardConstants.CARDS[i];
				dealerUpCardValue = dealerCard.getValue();
			} //if
		} //for

		Hand playerHandToAdd = new Hand(playerFirstCard, playerSecondCard);
		this.dealerHand = new Hand(dealerCard);

		playerHands.add(playerHandToAdd);
	}


	public ExpectedValueResponse calculateExpectedValue() {
		BigInteger currentIteration = new BigInteger("0");
        while (executionTimes.compareTo(currentIteration) > 0) {
            boolean playerBusted = false;
            performDeviation();

            for (Hand currentHand : playerHands) {
                if (currentHand.hasBlackJack()) {
                    winnings = winnings + (this.bet * (3 / 2));
                    response.incrementHandsWon();
                } else {

					//Perform basic strategy until the player busts or stands.
                    while (playerAction != PlayerAction.STAND && !playerBusted) {
						preformBasicStrategy(currentHand);
						if (currentHand.getValue() > 21) {
							playerBusted = true;
							losses = losses + bet;
							response.incrementHandsLost();
						} //if
					} //while

					while (dealerHand.getValue() < 16) {
						dealerHand.addCardToHand(generateRandomCard());
					} //while
                } //if
            } //for
            
            currentIteration.add(BigInteger.ONE);
        } //while

		//calculate expected value

		return response;
    } //calculateExpectedValue


    private Card generateRandomCard() {
        int randIndex = (int)(rng.nextInt(CardConstants.CARDS.length));
        Card randCard = CardConstants.CARDS[randIndex];

        return randCard;
    } //generateRandomCard


    private void performDeviation() {
		
    } //performDeviation


    private void preformBasicStrategy(Hand currentHand) {
		boolean shouldSplitHand = checkForSplitHand(currentHand);

		if (!shouldSplitHand) {
			if (currentHand.hasAce()) {
				performBasicStrategySoftTotal(currentHand);
			} else {
				performBasicStrategyHardTotal(currentHand);
			} //if
		} else {
			playerAction = PlayerAction.SPLIT;
			Card firstCard = currentHand.getCards().get(0);
			Card secondCard = generateRandomCard();
			Hand splitHand = new Hand(firstCard, secondCard);
			playerHands.add(splitHand);
		} //if
	} //calculateExpectedValue


	private void performBasicStrategyHardTotal(Hand currentHand) {
		int handValue = currentHand.getValue();

		if (handValue >= 17) {
			playerAction = PlayerAction.STAND;
		} else if (handValue > 12) {
			if (dealerUpCardValue > 6) {
				playerAction = PlayerAction.HIT;
			} else {
				playerAction = PlayerAction.STAND;
			} //if
		} else if (handValue == 12) {
			if (dealerUpCardValue > 6 || dealerUpCardValue < 4) {
				playerAction = PlayerAction.HIT;
			} else {
				playerAction = PlayerAction.STAND;
			} //if
		} else if (handValue == 11) {
			playerAction = PlayerAction.DOUBLE;
		} else if (handValue == 10) {
			if (dealerUpCardValue > 9) {
				playerAction = PlayerAction.HIT;
			} else {
				playerAction = PlayerAction.DOUBLE;
			} //if
		} else if (handValue == 9) {
			if (dealerUpCardValue > 6 || dealerUpCardValue == 2) {
				playerAction = PlayerAction.HIT;
			} else {
				playerAction = PlayerAction.DOUBLE;
			} //if
		} else {
			playerAction = PlayerAction.HIT;
		} //if


		switch (playerAction) {
			case PlayerAction.HIT:
				hit(currentHand);
				break;
			case PlayerAction.DOUBLE:
				bet = bet * 2;
				hit(currentHand);
				playerAction = PlayerAction.STAND;
				break;
		} //switch
	} //performBasicStrategyHardTotal


	private void performBasicStrategySoftTotal(Hand currentHand) {
		int softTotal = 0;
		for (Card currentCard : currentHand.getCards()) {
			if (currentCard.getCardType() != 'A') {
				softTotal += currentCard.getValue();
			} //if
		} //for

		if (softTotal == 9) {
			playerAction = PlayerAction.STAND;
		} else if (softTotal == 8) {
			if (dealerUpCardValue < 6 || dealerUpCardValue > 6) {
				playerAction = PlayerAction.STAND;
			} else {
				playerAction = PlayerAction.DOUBLE;
			} //if
		} else if (softTotal == 7) {
			if (dealerUpCardValue > 8) {
				playerAction = PlayerAction.HIT;
			} else if (dealerUpCardValue > 6) {
				playerAction = PlayerAction.STAND;
			} else {
				playerAction = PlayerAction.DOUBLE;
			} //if
		} else if (softTotal == 6) {
			if (dealerUpCardValue > 6 || dealerUpCardValue == 2) {
				playerAction = PlayerAction.HIT;
			} else {
				playerAction = PlayerAction.DOUBLE;
			} //if
		} else if (softTotal == 5 || softTotal == 4) {
			if (dealerUpCardValue > 6 || dealerUpCardValue < 4) {
				playerAction = PlayerAction.HIT;
			} else {
				playerAction = PlayerAction.DOUBLE;
			} //if
		} else {
			if (dealerUpCardValue > 6 || dealerUpCardValue < 5) {
				playerAction = PlayerAction.HIT;
			} else {
				playerAction = PlayerAction.DOUBLE;
			} //if
		} //if

		
		switch (playerAction) {
			case PlayerAction.HIT:
				hit(currentHand);
				break;
			case PlayerAction.DOUBLE:
				bet = bet * 2;
				hit(currentHand);
				playerAction = PlayerAction.STAND;
				break;
		} //switch
	} //performBasicStrategySoftTotal


	private boolean checkForSplitHand(Hand currentHand) {
		if (currentHand.getCards().size() == 2) {
			Card cardOne = currentHand.getCards().get(0);
			Card cardTwo = currentHand.getCards().get(1);
			if (!currentHand.hasAce()) {
				if (cardOne.getCardType() == cardTwo.getCardType()) {
					switch (cardOne.getCardType()) {
						case '9':
							if (dealerUpCardValue < 7 || dealerUpCardValue == 8 || dealerUpCardValue == 9) {
								return true;
							} //if
							break;
						case '8':
							playerAction = PlayerAction.SPLIT;
							return true;
						case '7':
							if (dealerUpCardValue < 8) {
								return true;
							} //if
							break;
						case '6':
							if (dealerUpCardValue < 7) {
								return true;
							} //if
							break;
						case '4':
							if (dealerUpCardValue == 5 || dealerUpCardValue == 6) {
								return true;
							} //if
							break;
						case '3':
							if (dealerUpCardValue < 8) {
								return true;
							} //if
							break;
						case '2':
							if (dealerUpCardValue < 8) {
								return true;
							} //if
							break;
					} //switch
				} //if
			} //if
		} //if

		return false;
	} //checkForSplitHand


	private void hit(Hand currentHand) {
		Card hitCard = generateRandomCard();
		currentHand.addCardToHand(hitCard);
	} //hit

} //EvDifferentialRequest