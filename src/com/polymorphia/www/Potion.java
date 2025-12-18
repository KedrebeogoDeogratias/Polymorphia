package com.polymorphia.www;

public class Potion extends Objet {
    private static final long serialVersionUID = 1L;
    private int pointsSoin;

    public Potion(String nom, int prix, int pointsSoin) {
        super(nom, prix);
        this.pointsSoin = pointsSoin;
    }

    public int getPointsSoin() { return pointsSoin; }
}

