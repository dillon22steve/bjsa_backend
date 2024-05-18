package com.dillonbrothers.pojos;

public class Card {

    private char cardType;
    private int value;


    public Card(char cardType, int value) {
        this.cardType = cardType;
        this.value = value;
    }


    public char getCardType() {
        return cardType;
    }

    public void setCardType(char cardType) {
        this.cardType = cardType;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
