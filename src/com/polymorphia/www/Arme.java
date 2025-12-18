package com.polymorphia.www;

public class Arme extends Objet {
    private static final long serialVersionUID = 1L;
    private int pointsAttaque;

    public Arme(String nom, int prix, int pointsAttaque) {
        super(nom, prix);
        this.pointsAttaque = pointsAttaque;
    }

    public int getPointsAttaque() { return pointsAttaque; }
}
