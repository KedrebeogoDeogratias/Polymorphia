package com.polymorphia.www;

import java.io.Serializable;

public abstract class Objet implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String nom;
    protected int prix;

    public Objet(String nom, int prix) {
        this.nom = nom;
        this.prix = prix;
    }

    public String getNom() { return nom; }
    public int getPrix() { return prix; }
}
