package com.polymorphia.www;

public class Sort extends Objet {
    private int pointsAttaque;

    public Sort(String nom, int prix, int pointsAttaque) {
        super(nom, prix);
        this.pointsAttaque = pointsAttaque;
    }

    public int getPointsAttaque() { return pointsAttaque; }
}
