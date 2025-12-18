package com.polymorphia.www;

import java.util.ArrayList;
import java.util.List;

public class Inventaire {
    private int coins;
    private List<Arme> armes;
    private List<Armure> armures;
    private List<Potion> potions;
    private List<Sort> sorts;
    private List<Materia> materias;

    public Inventaire(int coins) {
        this.coins = coins;
        this.armes = new ArrayList<>();
        this.armures = new ArrayList<>();
        this.potions = new ArrayList<>();
        this.sorts = new ArrayList<>();
        this.materias = new ArrayList<>();
    }

    public void ajouter(Objet objet) {
        if (objet instanceof Arme) {
            armes.add((Arme) objet);
        } else if (objet instanceof Armure) {
            armures.add((Armure) objet);
        } else if (objet instanceof Potion) {
            potions.add((Potion) objet);
        } else if (objet instanceof Sort) {
            sorts.add((Sort) objet);
        } else if (objet instanceof Materia) {
            materias.add((Materia) objet);
        }
    }

    public boolean retirer(Objet objet) {
        if (objet instanceof Arme) {
            return armes.remove(objet);
        } else if (objet instanceof Armure) {
            return armures.remove(objet);
        } else if (objet instanceof Potion) {
            return potions.remove(objet);
        } else if (objet instanceof Sort) {
            return sorts.remove(objet);
        } else if (objet instanceof Materia) {
            return materias.remove(objet);
        }
        return false;
    }

    public int getCoins() { return coins; }
    public void setCoins(int coins) { this.coins = coins; }
    public void ajouterCoins(int montant) { this.coins += montant; }
    public boolean retirerCoins(int montant) {
        if (this.coins >= montant) {
            this.coins -= montant;
            return true;
        }
        return false;
    }

    public List<Arme> getArmes() { return armes; }
    public List<Armure> getArmures() { return armures; }
    public List<Potion> getPotions() { return potions; }
    public List<Sort> getSorts() { return sorts; }
    public List<Materia> getMaterias() { return materias; }

    public void afficher() {
        System.out.println("=== INVENTAIRE ===");
        System.out.println("Pièces d'or : " + coins);
        System.out.println("\n-- Armes --");
        if (armes.isEmpty()) {
            System.out.println("  (vide)");
        } else {
            for (int i = 0; i < armes.size(); i++) {
                Arme a = armes.get(i);
                System.out.println("  " + (i + 1) + ". " + a.getNom() + " (ATK: " + a.getPointsAttaque() + ")");
            }
        }
        System.out.println("\n-- Armures --");
        if (armures.isEmpty()) {
            System.out.println("  (vide)");
        } else {
            for (int i = 0; i < armures.size(); i++) {
                Armure a = armures.get(i);
                System.out.println("  " + (i + 1) + ". " + a.getNom() + " (DEF: " + a.getPointsDefense() + ")");
            }
        }
        System.out.println("\n-- Potions --");
        if (potions.isEmpty()) {
            System.out.println("  (vide)");
        } else {
            for (int i = 0; i < potions.size(); i++) {
                Potion p = potions.get(i);
                System.out.println("  " + (i + 1) + ". " + p.getNom() + " (SOIN: " + p.getPointsSoin() + ")");
            }
        }
        System.out.println("\n-- Sorts --");
        if (sorts.isEmpty()) {
            System.out.println("  (vide)");
        } else {
            for (int i = 0; i < sorts.size(); i++) {
                Sort s = sorts.get(i);
                System.out.println("  " + (i + 1) + ". " + s.getNom() + " (ATK: " + s.getPointsAttaque() + ")");
            }
        }
        System.out.println("\n-- Materias --");
        if (materias.isEmpty()) {
            System.out.println("  (vide)");
        } else {
            for (int i = 0; i < materias.size(); i++) {
                Materia m = materias.get(i);
                System.out.println("  " + (i + 1) + ". " + m.getNom() + " (BONUS: " + m.getBonus() + ")");
            }
        }
    }
}
