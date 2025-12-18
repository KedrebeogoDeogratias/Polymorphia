package com.polymorphia.www.multijoueur;

import com.polymorphia.www.*;
import java.util.*;

/**
 * Gère le mode multijoueur du jeu
 */
public class GameMultijoueur {
    private Joueur joueur;
    private ClientMultijoueur client;
    private Scanner scanner;
    private boolean enJeu;
    private int vieAdversaire;
    
    public GameMultijoueur(Joueur joueur, Scanner scanner) {
        this.joueur = joueur;
        this.scanner = scanner;
        this.enJeu = false;
        this.vieAdversaire = 100;
    }
    
    public void demarrer() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║        MODE MULTIJOUEUR                   ║");
        System.out.println("╚═══════════════════════════════════════════╝");
        
        System.out.print("Adresse du serveur (défaut: localhost) : ");
        String hote = scanner.nextLine().trim();
        if (hote.isEmpty()) {
            hote = "localhost";
        }
        
        System.out.print("Port du serveur (défaut: 8888) : ");
        String portStr = scanner.nextLine().trim();
        int port = portStr.isEmpty() ? 8888 : Integer.parseInt(portStr);
        
        client = new ClientMultijoueur(joueur.getNom());
        
        // Configurer le gestionnaire de dégâts
        client.setGestionnaireDegats(message -> {
            joueur.recevoirDegats(message.getValeur());
            System.out.println("❤️  Votre vie : " + joueur.getVie() + "/" + joueur.getVieMax());
            if (!joueur.estVivant()) {
                System.out.println("💀 Vous avez été vaincu !");
                client.envoyerFinCombat(false);
            }
        });
        
        if (client.connecter(hote, port)) {
            enJeu = true;
            boucleMultijoueur();
        }
    }
    
    private void boucleMultijoueur() {
        while (enJeu && client.estConnecte()) {
            if (client.estEnCombat()) {
                menuCombat();
            } else {
                afficherMenuMultijoueur();
                int choix = lireChoix();
                
                switch (choix) {
                    case 1:
                        afficherJoueursEnLigne();
                        break;
                    case 2:
                        defierJoueur();
                        break;
                    case 3:
                        repondreDefi();
                        break;
                    case 4:
                        envoyerChat();
                        break;
                    case 5:
                        joueur.afficherStatuts();
                        break;
                    case 0:
                        enJeu = false;
                        client.deconnecter();
                        break;
                    default:
                        System.out.println("Choix invalide !");
                }
            }
            
            // Petite pause pour éviter la surcharge CPU
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    private void afficherMenuMultijoueur() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("           MENU MULTIJOUEUR");
        System.out.println("═══════════════════════════════════════════");
        System.out.println("  Joueurs en ligne : " + client.getJoueursEnLigne().size());
        if (client.getDernierDefi() != null) {
            System.out.println("  ⚔️  Défi en attente de " + client.getDernierDefi().getExpediteur() + " !");
        }
        System.out.println("═══════════════════════════════════════════");
        System.out.println("  1. Voir les joueurs en ligne");
        System.out.println("  2. Défier un joueur");
        System.out.println("  3. Répondre à un défi");
        System.out.println("  4. Envoyer un message");
        System.out.println("  5. Voir mes statistiques");
        System.out.println("  0. Retour au menu principal");
        System.out.println("═══════════════════════════════════════════");
        System.out.print("Votre choix : ");
    }
    
    private void afficherJoueursEnLigne() {
        System.out.println("\n=== JOUEURS EN LIGNE ===");
        List<String> joueurs = client.getJoueursEnLigne();
        if (joueurs.isEmpty()) {
            System.out.println("Aucun joueur en ligne.");
        } else {
            for (int i = 0; i < joueurs.size(); i++) {
                String statut = joueurs.get(i).equals(joueur.getNom()) ? " (vous)" : "";
                System.out.println("  " + (i + 1) + ". " + joueurs.get(i) + statut);
            }
        }
    }
    
    private void defierJoueur() {
        List<String> joueurs = client.getJoueursEnLigne();
        List<String> autresJoueurs = new ArrayList<>();
        
        for (String j : joueurs) {
            if (!j.equals(joueur.getNom())) {
                autresJoueurs.add(j);
            }
        }
        
        if (autresJoueurs.isEmpty()) {
            System.out.println("❌ Aucun autre joueur en ligne !");
            return;
        }
        
        System.out.println("\n=== DÉFIER UN JOUEUR ===");
        for (int i = 0; i < autresJoueurs.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + autresJoueurs.get(i));
        }
        System.out.println("  0. Annuler");
        System.out.print("Votre choix : ");
        
        int choix = lireChoix();
        if (choix > 0 && choix <= autresJoueurs.size()) {
            client.defierJoueur(autresJoueurs.get(choix - 1));
        }
    }
    
    private void repondreDefi() {
        if (client.getDernierDefi() == null) {
            System.out.println("❌ Aucun défi en attente !");
            return;
        }
        
        System.out.println("\n⚔️  " + client.getDernierDefi().getExpediteur() + " vous défie !");
        System.out.println("  1. Accepter");
        System.out.println("  2. Refuser");
        System.out.print("Votre choix : ");
        
        int choix = lireChoix();
        client.repondreDefi(choix == 1);
        
        if (choix == 1) {
            vieAdversaire = 100; // Réinitialiser la vie de l'adversaire
        }
    }
    
    private void envoyerChat() {
        System.out.print("Message : ");
        scanner.nextLine(); // Vider le buffer
        String message = scanner.nextLine();
        if (!message.isEmpty()) {
            client.envoyerChat(message);
        }
    }
    
    private void menuCombat() {
        System.out.println("\n--- COMBAT MULTIJOUEUR ---");
        System.out.println(joueur.getNom() + " : " + joueur.getVie() + "/" + joueur.getVieMax() + " PV");
        System.out.println(client.getAdversaireActuel() + " : " + vieAdversaire + " PV (estimé)");
        System.out.println("--------------------------");
        
        if (client.estMonTour()) {
            System.out.println("👉 C'est votre tour !");
            System.out.println("1. Attaquer");
            System.out.println("2. Utiliser un Sort");
            System.out.println("3. Utiliser une Potion");
            System.out.println("4. Abandonner");
            System.out.print("Votre action : ");
            
            int choix = lireChoix();
            
            switch (choix) {
                case 1:
                    int degats = calculerDegatsAttaque();
                    client.envoyerActionCombat("Attaque", degats);
                    vieAdversaire -= degats;
                    System.out.println("⚔️  Vous attaquez et infligez " + degats + " dégâts !");
                    if (vieAdversaire <= 0) {
                        System.out.println("🎉 Vous avez vaincu " + client.getAdversaireActuel() + " !");
                        client.envoyerFinCombat(true);
                    }
                    break;
                case 2:
                    utiliserSortMultijoueur();
                    break;
                case 3:
                    joueur.utiliserPotion(scanner);
                    break;
                case 4:
                    System.out.println("🏳️  Vous abandonnez le combat !");
                    client.envoyerFinCombat(false);
                    break;
            }
        } else {
            System.out.println("⏳ En attente de l'adversaire...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    private void utiliserSortMultijoueur() {
        List<Sort> sorts = joueur.getInventaire().getSorts();
        if (sorts.isEmpty()) {
            System.out.println("Vous n'avez aucun sort !");
            return;
        }
        
        System.out.println("\n=== SORTS DISPONIBLES ===");
        for (int i = 0; i < sorts.size(); i++) {
            Sort s = sorts.get(i);
            System.out.println((i + 1) + ". " + s.getNom() + " (ATK: " + s.getPointsAttaque() + ")");
        }
        System.out.println("0. Annuler");
        System.out.print("Votre choix : ");
        
        int choix = lireChoix();
        if (choix > 0 && choix <= sorts.size()) {
            Sort sort = sorts.get(choix - 1);
            int degats = sort.getPointsAttaque() + joueur.getCompetence();
            client.envoyerActionCombat(sort.getNom(), degats);
            vieAdversaire -= degats;
            System.out.println("✨ Vous lancez " + sort.getNom() + " et infligez " + degats + " dégâts !");
            if (vieAdversaire <= 0) {
                System.out.println("🎉 Vous avez vaincu " + client.getAdversaireActuel() + " !");
                client.envoyerFinCombat(true);
            }
        }
    }
    
    private int calculerDegatsAttaque() {
        int degats = joueur.getAttaque() + joueur.getBonusMateria();
        Arme arme = joueur.getArmeEquipee();
        if (arme != null) {
            degats += arme.getPointsAttaque();
        }
        return degats;
    }
    
    private int lireChoix() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }
}
