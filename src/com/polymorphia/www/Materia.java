package com.polymorphia.www;

public class Materia extends Objet {
    private int bonus;

    public Materia(String nom, int prix, int bonus) {
        super(nom, prix);
        this.bonus = bonus;
    }

    public int getBonus() { return bonus; }
}
