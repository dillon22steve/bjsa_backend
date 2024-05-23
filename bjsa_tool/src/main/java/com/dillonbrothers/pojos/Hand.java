package com.dillonbrothers.pojos;

import java.util.ArrayList;
import java.util.List;

import com.dillonbrothers.constants.CardConstants;

public class Hand {
    
    private List<Card> cards;
    
    private int value;

    private boolean hasBlackJack;
    
    private boolean hasAce;


    public Hand() {
        hasBlackJack = false;
        this.cards = new ArrayList<Card>();
    } //constructor

    public Hand(Card firstCard, Card secondCard) {
        if (firstCard.getCardType() == 'A' && secondCard.getCardType() == 'A') {
            //change this card from an ace with a value of 11 to an ace of value 1
            secondCard = CardConstants.CARDS[0];
        } //if

        if (firstCard.getCardType() == 'A' || secondCard.getCardType() == 'A') {
            hasAce = true;
        } //if

        cards = new ArrayList<Card>();

        cards.add(firstCard);
        cards.add(secondCard);

        value = firstCard.getValue() + secondCard.getValue();
        if (value == 21) {
            hasBlackJack = true;
        } //if
    } //constructor

    public Hand(Card firstCard) {
        if (firstCard.getCardType() == 'A') {
            hasAce = true;
        } //if

        cards = new ArrayList<Card>();
        cards.add(firstCard);
        
        value = firstCard.getValue();
    } //constructor


    public void addCardToHand(Card cardToAdd) {
        if (cardToAdd.getCardType() == 'A') {
            if (hasAce == true) {
                removeExistingAce();
            } //if

            hasAce = true;
        } //if

        value = value + cardToAdd.getValue();

        //If the value of the hand is greater than 21 but the hand has an ace. Find the ace of
        //value 11, remove it from the hand and add an ace of value 1.
        if (value > 21 && hasAce) {
            removeExistingAce();
        } //if

        cards.add(cardToAdd);

        if (cards.size() == 2 && value == 21) {
            hasBlackJack = true;
        } //if
    } //addCardToHand


    public void removeCardFromHand(int index) {
        this.value = value - (cards.get(index).getValue());
        cards.remove(index);
    } //removeCardFromHand


    private void removeExistingAce() {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getCardType() == 'A') {
                cards.remove(i);
                cards.add(CardConstants.CARDS[0]);

                value = value - 10;

                hasAce = false;

                break;
            } //if
        } //for
    } //removeExistingAce


    public List<Card> getCards() {
        return cards;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public boolean hasBlackJack() {
        return this.hasBlackJack;
    }
    public boolean hasAce() {
        return hasAce;
    }
    public void setHasAce(boolean hasAce) {
        this.hasAce = hasAce;
    }

}
