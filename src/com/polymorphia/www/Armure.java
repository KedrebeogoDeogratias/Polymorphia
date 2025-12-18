package com.polymorphia.www;

public class Armure extends Objet {
    private static final long serialVersionUID = 1L;
    private int pointsDefense;

    public Armure(String nom, int prix, int pointsDefense) {
        super(nom, prix);
        this.pointsDefense = pointsDefense;
    }

    public int getPointsDefense() { return pointsDefense; }
}
