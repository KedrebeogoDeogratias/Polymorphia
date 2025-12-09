package com.polymorphia.www;

public class Arme extends Objet {
    private int pointsAttaque;

    public Arme(String nom, int prix, int pointsAttaque) {
        super(nom, prix);
        this.pointsAttaque = pointsAttaque;
    }

    public int getPointsAttaque() { return pointsAttaque; }
}
