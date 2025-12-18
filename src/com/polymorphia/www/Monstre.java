package com.polymorphia.www;

public class Monstre {
    private String nom;
    private int vie;
    private int attaque;
    private int defense;

    public Monstre(String nom, int vie, int attaque, int defense) {
        this.nom = nom;
        this.vie = vie;
        this.attaque = attaque;
        this.defense = defense;
    }

    public void attaquer(Joueur joueur) {
        int degats = this.attaque - joueur.getDefense();
        if (degats < 1) degats = 1;
        joueur.recevoirDegats(degats);
        System.out.println(this.nom + " attaque " + joueur.getNom() + " et inflige " + degats + " dégâts !");
    }

    public void recevoirDegats(int degats) {
        this.vie -= degats;
        if (this.vie < 0) this.vie = 0;
    }

    public boolean estVivant() {
        return this.vie > 0;
    }

    public String getNom() { return nom; }
    public int getVie() { return vie; }
    public int getAttaque() { return attaque; }
    public int getDefense() { return defense; }
}
