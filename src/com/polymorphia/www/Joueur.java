package com.polymorphia.www;

import java.io.Serializable;
import java.util.List;
import java.util.Scanner;

public class Joueur implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nom;
    private int vie;
    private int vieMax;
    private int attaque;
    private int defense;
    private int competence;
    private Inventaire inventaire;
    private Arme armeEquipee;
    private Armure armureEquipee;
    private int bonusMateria;

    public Joueur(String nom, int vie, int attaque, int defense, int competence, Inventaire inventaire) {
        this.nom = nom;
        this.vie = vie;
        this.vieMax = vie;
        this.attaque = attaque;
        this.defense = defense;
        this.competence = competence;
        this.inventaire = inventaire;
        this.armeEquipee = null;
        this.armureEquipee = null;
        this.bonusMateria = 0;
    }

    public void attaquer(Monstre monstre) {
        int totalAttaque = this.attaque + bonusMateria;
        if (armeEquipee != null) {
            totalAttaque += armeEquipee.getPointsAttaque();
        }
        int degats = totalAttaque - monstre.getDefense();
        if (degats < 1) degats = 1;
        monstre.recevoirDegats(degats);
        System.out.println(this.nom + " attaque " + monstre.getNom() + " et inflige " + degats + " dégâts !");
    }

    public void utiliserPotion(Scanner scanner) {
        List<Potion> potions = inventaire.getPotions();
        if (potions.isEmpty()) {
            System.out.println("Vous n'avez aucune potion !");
            return;
        }
        System.out.println("\n=== POTIONS DISPONIBLES ===");
        for (int i = 0; i < potions.size(); i++) {
            Potion p = potions.get(i);
            System.out.println((i + 1) + ". " + p.getNom() + " (SOIN: " + p.getPointsSoin() + ")");
        }
        System.out.println("0. Annuler");
        System.out.print("Votre choix : ");
        int choix = scanner.nextInt();
        if (choix > 0 && choix <= potions.size()) {
            Potion potion = potions.get(choix - 1);
            int soin = potion.getPointsSoin();
            this.vie += soin;
            if (this.vie > this.vieMax) this.vie = this.vieMax;
            inventaire.retirer(potion);
            System.out.println("Vous utilisez " + potion.getNom() + " et récupérez " + soin + " PV !");
            System.out.println("Vie actuelle : " + this.vie + "/" + this.vieMax);
        }
    }

    public boolean utiliserSort(Monstre monstre, Scanner scanner) {
        List<Sort> sorts = inventaire.getSorts();
        if (sorts.isEmpty()) {
            System.out.println("Vous n'avez aucun sort !");
            return false;
        }
        System.out.println("\n=== SORTS DISPONIBLES ===");
        for (int i = 0; i < sorts.size(); i++) {
            Sort s = sorts.get(i);
            System.out.println((i + 1) + ". " + s.getNom() + " (ATK: " + s.getPointsAttaque() + ")");
        }
        System.out.println("0. Annuler");
        System.out.print("Votre choix : ");
        int choix = scanner.nextInt();
        if (choix > 0 && choix <= sorts.size()) {
            Sort sort = sorts.get(choix - 1);
            int degats = sort.getPointsAttaque() + competence - monstre.getDefense();
            if (degats < 1) degats = 1;
            monstre.recevoirDegats(degats);
            System.out.println("Vous lancez " + sort.getNom() + " sur " + monstre.getNom() + " et infligez " + degats + " dégâts !");
            return true;
        }
        return false;
    }

    public void equiperArme(Scanner scanner) {
        List<Arme> armes = inventaire.getArmes();
        if (armes.isEmpty()) {
            System.out.println("Vous n'avez aucune arme !");
            return;
        }
        System.out.println("\n=== ARMES DISPONIBLES ===");
        for (int i = 0; i < armes.size(); i++) {
            Arme a = armes.get(i);
            String equipee = (a == armeEquipee) ? " [ÉQUIPÉE]" : "";
            System.out.println((i + 1) + ". " + a.getNom() + " (ATK: " + a.getPointsAttaque() + ")" + equipee);
        }
        System.out.println("0. Annuler");
        System.out.print("Votre choix : ");
        int choix = scanner.nextInt();
        if (choix > 0 && choix <= armes.size()) {
            armeEquipee = armes.get(choix - 1);
            System.out.println("Vous équipez " + armeEquipee.getNom() + " !");
        }
    }

    public void equiperArmure(Scanner scanner) {
        List<Armure> armures = inventaire.getArmures();
        if (armures.isEmpty()) {
            System.out.println("Vous n'avez aucune armure !");
            return;
        }
        System.out.println("\n=== ARMURES DISPONIBLES ===");
        for (int i = 0; i < armures.size(); i++) {
            Armure a = armures.get(i);
            String equipee = (a == armureEquipee) ? " [ÉQUIPÉE]" : "";
            System.out.println((i + 1) + ". " + a.getNom() + " (DEF: " + a.getPointsDefense() + ")" + equipee);
        }
        System.out.println("0. Annuler");
        System.out.print("Votre choix : ");
        int choix = scanner.nextInt();
        if (choix > 0 && choix <= armures.size()) {
            armureEquipee = armures.get(choix - 1);
            System.out.println("Vous équipez " + armureEquipee.getNom() + " !");
        }
    }

    public void utiliserMateria(Scanner scanner) {
        List<Materia> materias = inventaire.getMaterias();
        if (materias.isEmpty()) {
            System.out.println("Vous n'avez aucune materia !");
            return;
        }
        System.out.println("\n=== MATERIAS DISPONIBLES ===");
        for (int i = 0; i < materias.size(); i++) {
            Materia m = materias.get(i);
            System.out.println((i + 1) + ". " + m.getNom() + " (BONUS: " + m.getBonus() + ")");
        }
        System.out.println("0. Annuler");
        System.out.print("Votre choix : ");
        int choix = scanner.nextInt();
        if (choix > 0 && choix <= materias.size()) {
            Materia materia = materias.get(choix - 1);
            this.bonusMateria += materia.getBonus();
            inventaire.retirer(materia);
            System.out.println("Vous absorbez " + materia.getNom() + " ! Bonus d'attaque permanent : +" + materia.getBonus());
        }
    }

    public void recevoirDegats(int degats) {
        this.vie -= degats;
        if (this.vie < 0) this.vie = 0;
    }

    public boolean estVivant() {
        return this.vie > 0;
    }

    public int getDefense() {
        int totalDefense = this.defense;
        if (armureEquipee != null) {
            totalDefense += armureEquipee.getPointsDefense();
        }
        return totalDefense;
    }

    public void afficherStatuts() {
        System.out.println("\n=== " + nom.toUpperCase() + " ===");
        System.out.println("Vie : " + vie + "/" + vieMax);
        int totalAttaque = attaque + bonusMateria + (armeEquipee != null ? armeEquipee.getPointsAttaque() : 0);
        System.out.println("Attaque : " + totalAttaque);
        System.out.println("Défense : " + getDefense());
        System.out.println("Compétence : " + competence);
        System.out.println("Pièces d'or : " + inventaire.getCoins());
        if (armeEquipee != null) {
            System.out.println("Arme équipée : " + armeEquipee.getNom());
        }
        if (armureEquipee != null) {
            System.out.println("Armure équipée : " + armureEquipee.getNom());
        }
    }

    public String getNom() { return nom; }
    public int getVie() { return vie; }
    public int getVieMax() { return vieMax; }
    public int getAttaque() { return attaque; }
    public int getCompetence() { return competence; }
    public Inventaire getInventaire() { return inventaire; }
    public int getBonusMateria() { return bonusMateria; }
    public Arme getArmeEquipee() { return armeEquipee; }
    public Armure getArmureEquipee() { return armureEquipee; }
}
