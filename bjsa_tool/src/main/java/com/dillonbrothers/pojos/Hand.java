package com.dillonbrothers.pojos;

import java.util.List;

public class Hand {
    
    private List<Card> cards;
    private int value;

    private boolean hasBlackJack;


    public Hand() {
        hasBlackJack = false;
    }

    public Hand(Card firstCard, Card secondCard) {
        cards.add(firstCard);
        cards.add(secondCard);
        
        if (firstCard.getValue() + secondCard.getValue() == 21) {
            hasBlackJack = true;
        } else {
            hasBlackJack = false;
        }
    }

    public Hand(Card firstCard) {
        cards.add(firstCard);
        hasBlackJack = false;
    }


    public List<Card> getCards() {
        return cards;
    }
    public void addCardToHand(Card cardToAdd) {
        if (cards.size() == 2) {
            if (cards.get(0).getValue() + cards.get(1).getValue() == 21) {
                hasBlackJack = true;
            }
        }
        cards.add(cardToAdd);
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

}
