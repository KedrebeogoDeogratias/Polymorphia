package com.polymorphia.www;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Marchand {
    private List<Objet> stock;

    public Marchand() {
        this.stock = new ArrayList<>();
    }

    public void ajouterAuStock(Objet objet) {
        stock.add(objet);
    }

    public void vendre(Joueur joueur, Objet objet) {
        if (joueur.getInventaire().getCoins() >= objet.getPrix()) {
            joueur.getInventaire().retirerCoins(objet.getPrix());
            joueur.getInventaire().ajouter(objet);
            stock.remove(objet);
            System.out.println("Vous avez acheté " + objet.getNom() + " pour " + objet.getPrix() + " pièces d'or !");
        } else {
            System.out.println("Vous n'avez pas assez de pièces d'or !");
        }
    }

    public void afficherStock() {
        System.out.println("\n=== STOCK DU MARCHAND ===");
        if (stock.isEmpty()) {
            System.out.println("Le marchand n'a plus rien à vendre...");
            return;
        }
        for (int i = 0; i < stock.size(); i++) {
            Objet o = stock.get(i);
            String details = "";
            if (o instanceof Arme) {
                details = " (ATK: " + ((Arme) o).getPointsAttaque() + ")";
            } else if (o instanceof Armure) {
                details = " (DEF: " + ((Armure) o).getPointsDefense() + ")";
            } else if (o instanceof Potion) {
                details = " (SOIN: " + ((Potion) o).getPointsSoin() + ")";
            } else if (o instanceof Sort) {
                details = " (ATK: " + ((Sort) o).getPointsAttaque() + ")";
            } else if (o instanceof Materia) {
                details = " (BONUS: " + ((Materia) o).getBonus() + ")";
            }
            System.out.println((i + 1) + ". " + o.getNom() + details + " - " + o.getPrix() + " pièces d'or");
        }
    }

    public void commercer(Joueur joueur, Scanner scanner) {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\nVos pièces d'or : " + joueur.getInventaire().getCoins());
            afficherStock();
            if (stock.isEmpty()) {
                return;
            }
            System.out.println("0. Quitter le commerce");
            System.out.print("Que voulez-vous acheter ? ");
            int choix = scanner.nextInt();
            if (choix == 0) {
                continuer = false;
                System.out.println("À bientôt, aventurier !");
            } else if (choix > 0 && choix <= stock.size()) {
                vendre(joueur, stock.get(choix - 1));
            } else {
                System.out.println("Choix invalide !");
            }
        }
    }

    public List<Objet> getStock() { return stock; }
}
