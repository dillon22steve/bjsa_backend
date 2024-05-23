package com.dillonbrothers.bjsa_tool.model;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

import com.dillonbrothers.constants.CardConstants;
import com.dillonbrothers.pojos.Card;
import com.dillonbrothers.pojos.Hand;
import com.dillonbrothers.results_response.ExpectedValueResult;

public class EvDifferentialRequest {
	
	private char dealerUpCard;
	private int dealerUpCardValue;

	private BigInteger executionTimes;
	
	private PlayerAction deviation;
	private PlayerAction playerAction;

	private boolean isExperimentalStrategy;
	
	//comma separated format (A, 2, 3, ... 10), 2 values passed
	private String startingHand;

	private ArrayList<Hand> playerHands;
	private ArrayList<Hand> playerStartingHands;

    private Hand dealerHand;
	private Hand dealerStartingHand;

    private int bet, winnings, losses, origBet;
	private int handsWon, handsLost;
    private ExpectedValueResult response;

    private SecureRandom rng;


	public EvDifferentialRequest(String startingHand, char dealerUpCard, BigInteger executionTimes, 
		int origBet, PlayerAction deviation, boolean isExperimentalStrategy) {

		rng = null;
        try {
            rng = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        } //try

		this.executionTimes = executionTimes;

		this.startingHand = startingHand;
		this.dealerUpCard = dealerUpCard;

		this.deviation = deviation;

		this.isExperimentalStrategy = isExperimentalStrategy;

        this.winnings = 0;
        this.losses = 0;

		this.handsLost = 0;
		this.handsWon = 0;

		this.origBet = origBet;
		this.bet = origBet;

		//This is just an arbitrary start point so that the while loop in the expected value will run.
		//The action will be updated then to account for whatever the player's hand is and dealer upcard is.
		this.playerAction = PlayerAction.HIT;

		createPlayerAndDealerHands();
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


		//Loop through the card constants and find the one that has the provided character.
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
		this.dealerHand.addCardToHand(generateRandomCard());

		dealerStartingHand = dealerHand;

		playerHands = new ArrayList<Hand>();
		playerHands.add(playerHandToAdd);
	} //createPlayerAndDealerHands



	public ExpectedValueResult calculateExpectedValue() {
        while (executionTimes.compareTo(BigInteger.ZERO) >= 0) {
			System.out.println("Next iteration");
			System.out.println();
			
            boolean playerBusted = false;
			boolean dealerBusted = false;

			if (isExperimentalStrategy) {
				performDeviation();
			} //if

            for (Hand currentHand : playerHands) {
				System.out.print("Beginning Current hand: ");
				for (Card card : currentHand.getCards()) {
					System.out.print(card.getCardType() + ", ");
				}
				System.out.print("\n");

                if (currentHand.hasBlackJack()) {
                    winnings = winnings + (bet * (3 / 2));
                    handsWon++;
                } else {

					//Perform basic strategy until the player busts or stands.
                    while (playerAction != PlayerAction.STAND && !playerBusted) {
						preformBasicStrategy(currentHand);
						if (currentHand.getValue() > 21) {
							System.out.println("Player lost");
							playerBusted = true;
							losses = losses + bet;
							handsLost++;
						} //if
					} //while


					while (dealerHand.getValue() < 16) {
						dealerHand.addCardToHand(generateRandomCard());
					} //while
					if (dealerHand.getValue() > 21) {
						dealerBusted = true;
					} //if


					System.out.print("Final player current hand: ");
					for (Card card : currentHand.getCards()) {
						System.out.print(card.getCardType() + ", ");
					} //for
					System.out.print("\n");

					System.out.print("Dealer final hand: ");
					for (Card card : dealerHand.getCards()) {
						System.out.print(card.getCardType() + ", ");
					}
					System.out.print("\n");
					

					//Check to see if the player won or lost the hand.
					if (!playerBusted) {
						if (dealerBusted) {
							System.out.println("Player won");
							winnings = winnings + bet;
							handsWon++;
						} else {
							if (currentHand.getValue() > dealerHand.getValue()) {
								System.out.println("Player won");
								winnings = winnings + bet;
								handsWon++;
							} else {
								System.out.println("Player lost");
								losses = losses + bet;
								handsLost++;
							} //if
						} //if
					} //if
                } //if

				//reset these variables for the next hand.
				bet = origBet;
				playerBusted = false;
				dealerBusted = false;

				//Resetting this so the basic strategy while loop above will run on the next iteration.
				//If it is still set to stand it will not run.
				playerAction = PlayerAction.HIT;
				System.out.println();
            } //for
            
			executionTimes = executionTimes.subtract(BigInteger.ONE);
			resetHands();
        } //while

		response = new ExpectedValueResult(handsWon, handsLost, winnings, losses);

		return response;
    } //calculateExpectedValue



    private Card generateRandomCard() {
		//The (.length - 1) + 1 here is so that it doesn't generate the ace with a value of 1.
        int randIndex = (int)(rng.nextInt(CardConstants.CARDS.length - 1)) + 1;
        Card randCard = CardConstants.CARDS[randIndex];

        return randCard;
    } //generateRandomCard



    private void performDeviation() {
		System.out.println("Performing deviation");
		switch (deviation) {
			case PlayerAction.HIT:
				hit(playerHands.get(0));
				break;
			case PlayerAction.DOUBLE:
				doubleBetAndStand(playerHands.get(0));
				break;
			case PlayerAction.STAND:
				playerAction = PlayerAction.STAND;
				break;
			case PlayerAction.SPLIT:
				split(playerHands.get(0));
				break;
		} //switch

		//Will be used later to reset the player's hands.
		playerStartingHands = new ArrayList<Hand>();
		for (Hand hand : playerHands) {
			Hand handToAdd = new Hand();
			for (Card card : hand.getCards()) {
				System.out.print(card.getCardType() + ", ");
				handToAdd.addCardToHand(card);
			}
			playerStartingHands.add(handToAdd);
			System.out.print("\n");
		} //for
		System.out.println();
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
			split(currentHand);
		} //if
	} //calculateExpectedValue


	private void performBasicStrategyHardTotal(Hand currentHand) {
		int handValue = currentHand.getValue();
		System.out.print("Performing basic strategy hard total on handValue: ");
		for (Card  card : currentHand.getCards()) {
			System.out.print(card.getCardType() + ", ");
		}
		System.out.print(" and dealerUpCard: " + dealerUpCardValue + "\n");

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

		System.out.print("New hand after " + playerAction + ": ");


		switch (playerAction) {
			case PlayerAction.HIT:
				hit(currentHand);
				break;
			case PlayerAction.DOUBLE:
				doubleBetAndStand(currentHand);
				break;
		} //switch

		for (Card  card : currentHand.getCards()) {
			System.out.print(card.getCardType() + ", ");
		} //for
		System.out.print("\n");
		System.out.println();
	} //performBasicStrategyHardTotal


	private void performBasicStrategySoftTotal(Hand currentHand) {
		int softTotal = 0;
		for (Card currentCard : currentHand.getCards()) {
			if (currentCard.getCardType() != 'A') {
				softTotal += currentCard.getValue();
			} //if
		} //for
		System.out.print("Performing basic strategy soft total on soft total: " + softTotal);
		System.out.print(" and dealerUpCard: " + dealerUpCardValue + "\n");

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


		System.out.print("New hand after " + playerAction + ": ");

		
		switch (playerAction) {
			case PlayerAction.HIT:
				hit(currentHand);
				break;
			case PlayerAction.DOUBLE:
				doubleBetAndStand(currentHand);
				break;
		} //switch

		for (Card  card : currentHand.getCards()) {
			System.out.print(card.getCardType() + ", ");
		} //for
		System.out.print("\n");
		System.out.println();
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


	private void split(Hand currentHand) {
		Card splitCard = playerHands.get(0).getCards().get(0);
		Card newCard = generateRandomCard();
		Hand newHand = new Hand(splitCard, newCard);
		playerHands.add(newHand);

		newCard = generateRandomCard();
		currentHand.getCards().remove(1);
		currentHand.addCardToHand(newCard);
	} //split


	private void doubleBetAndStand(Hand currentHand) {
		bet = bet * 2;
		hit(currentHand);
		playerAction = PlayerAction.STAND;
	} //doubleAndStand


	private void resetHands() {
		playerHands.clear();
		Hand playerHand = new Hand(playerStartingHands.get(0).getCards().get(0), playerStartingHands.get(0).getCards().get(1));
		playerHands.add(playerHand);

		dealerHand = new Hand(dealerStartingHand.getCards().get(0));
		dealerHand.addCardToHand(generateRandomCard());
	} //resetHands

} //EvDifferentialRequest