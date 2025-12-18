package com.polymorphia.www;

import com.polymorphia.www.multijoueur.GameMultijoueur;
import com.polymorphia.www.multijoueur.ServeurMultijoueur;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private Joueur joueur;
    private Marchand marchand;
    private Scanner scanner;
    private Random random;
    private String[] nomsMonstres = {"Gobelin", "Squelette", "Orc", "Troll", "Loup Sombre", "Araignée Géante", "Spectre", "Golem de Pierre"};

    public Game() {
        this.scanner = new Scanner(System.in);
        this.random = new Random();
        initialiserJeu();
    }

    private void initialiserJeu() {
        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║         BIENVENUE DANS POLYMORPHIA        ║");
        System.out.println("╚═══════════════════════════════════════════╝");
        System.out.println();

        Inventaire inventaire = new Inventaire(100);
        joueur = new Joueur("Javalt de Riv", 100, 15, 5, 10, inventaire);

        inventaire.ajouter(new Arme("Épée Rouillée", 0, 5));
        inventaire.ajouter(new Potion("Potion de Soin", 0, 30));

        marchand = new Marchand();
        marchand.ajouterAuStock(new Arme("Épée de Fer", 50, 12));
        marchand.ajouterAuStock(new Arme("Épée d'Argent", 120, 20));
        marchand.ajouterAuStock(new Arme("Excalibur", 500, 50));
        marchand.ajouterAuStock(new Armure("Armure de Cuir", 40, 8));
        marchand.ajouterAuStock(new Armure("Armure de Mailles", 100, 15));
        marchand.ajouterAuStock(new Armure("Armure de Plaques", 250, 25));
        marchand.ajouterAuStock(new Potion("Potion de Soin", 25, 30));
        marchand.ajouterAuStock(new Potion("Grande Potion", 50, 60));
        marchand.ajouterAuStock(new Potion("Élixir de Vie", 100, 100));
        marchand.ajouterAuStock(new Sort("Boule de Feu", 80, 25));
        marchand.ajouterAuStock(new Sort("Éclair", 60, 18));
        marchand.ajouterAuStock(new Sort("Météore", 200, 45));
        marchand.ajouterAuStock(new Materia("Materia de Force", 150, 10));
        marchand.ajouterAuStock(new Materia("Materia de Puissance", 300, 20));

        System.out.println("Vous incarnez " + joueur.getNom() + ", un aventurier en quête de gloire !");
        System.out.println("Vous possédez " + inventaire.getCoins() + " pièces d'or pour commencer.");
        System.out.println();
    }

    public void lancerJeu() {
        boolean jouer = true;

        while (jouer && joueur.estVivant()) {
            afficherMenuPrincipal();
            int choix = lireChoix();

            switch (choix) {
                case 1:
                    commercer();
                    break;
                case 2:
                    seDeplacer();
                    break;
                case 3:
                    sEquiper();
                    break;
                case 4:
                    joueur.utiliserMateria(scanner);
                    break;
                case 5:
                    joueur.utiliserPotion(scanner);
                    break;
                case 6:
                    joueur.afficherStatuts();
                    break;
                case 7:
                    joueur.getInventaire().afficher();
                    break;
                case 8:
                    sauvegarderPartie();
                    break;
                case 9:
                    chargerPartie();
                    break;
                case 10:
                    lancerModeMultijoueur();
                    break;
                case 11:
                    lancerServeur();
                    break;
                case 0:
                    jouer = false;
                    System.out.println("\nMerci d'avoir joué à Polymorphia !");
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        }

        if (!joueur.estVivant()) {
            System.out.println("\n╔═══════════════════════════════════════════╗");
            System.out.println("║              GAME OVER                     ║");
            System.out.println("║     " + joueur.getNom() + " est tombé au combat...    ║");
            System.out.println("╚═══════════════════════════════════════════╝");
        }

        scanner.close();
    }

    private void afficherMenuPrincipal() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("           MENU PRINCIPAL");
        System.out.println("═══════════════════════════════════════════");
        System.out.println("  Vie : " + joueur.getVie() + "/" + joueur.getVieMax() + " | Or : " + joueur.getInventaire().getCoins());
        System.out.println("═══════════════════════════════════════════");
        System.out.println("  1. Commercer avec le marchand");
        System.out.println("  2. Se déplacer (exploration)");
        System.out.println("  3. S'équiper");
        System.out.println("  4. Utiliser une Materia");
        System.out.println("  5. Utiliser une Potion");
        System.out.println("  6. Voir les statistiques");
        System.out.println("  7. Voir l'inventaire");
        System.out.println("  8. Sauvegarder la partie");
        System.out.println("  9. Charger une partie");
        System.out.println(" 10. Mode Multijoueur");
        System.out.println(" 11. Lancer un serveur");
        System.out.println("  0. Quitter le jeu");
        System.out.println("═══════════════════════════════════════════");
        System.out.print("Votre choix : ");
    }

    private int lireChoix() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }

    private void commercer() {
        System.out.println("\n🏪 Vous entrez dans la boutique du marchand...");
        marchand.commercer(joueur, scanner);
    }

    private void seDeplacer() {
        System.out.println("\n🚶 Vous vous aventurez dans les terres sauvages...");

        int chance = random.nextInt(100);

        if (chance < 70) {
            Monstre monstre = genererMonstre();
            System.out.println("\n⚔️  Un " + monstre.getNom() + " apparaît !");
            System.out.println("    Vie : " + monstre.getVie() + " | ATK : " + monstre.getAttaque() + " | DEF : " + monstre.getDefense());
            combat(monstre);
        } else {
            int tresor = random.nextInt(20) + 5;
            joueur.getInventaire().ajouterCoins(tresor);
            System.out.println("✨ Vous trouvez " + tresor + " pièces d'or sur le chemin !");
        }
    }

    private Monstre genererMonstre() {
        String nom = nomsMonstres[random.nextInt(nomsMonstres.length)];
        int niveau = random.nextInt(3) + 1;
        int vie = 30 + (niveau * 20) + random.nextInt(20);
        int attaque = 8 + (niveau * 5) + random.nextInt(5);
        int defense = 2 + (niveau * 3) + random.nextInt(3);
        return new Monstre(nom, vie, attaque, defense);
    }

    private void combat(Monstre monstre) {
        boolean enCombat = true;

        while (enCombat && joueur.estVivant() && monstre.estVivant()) {
            System.out.println("\n--- COMBAT ---");
            System.out.println(joueur.getNom() + " : " + joueur.getVie() + "/" + joueur.getVieMax() + " PV");
            System.out.println(monstre.getNom() + " : " + monstre.getVie() + " PV");
            System.out.println("--------------");
            System.out.println("1. Attaquer");
            System.out.println("2. Utiliser un Sort");
            System.out.println("3. Utiliser une Potion");
            System.out.println("4. Fuir");
            System.out.print("Votre action : ");

            int choix = lireChoix();

            switch (choix) {
                case 1:
                    joueur.attaquer(monstre);
                    break;
                case 2:
                    if (!joueur.utiliserSort(monstre, scanner)) {
                        continue;
                    }
                    break;
                case 3:
                    joueur.utiliserPotion(scanner);
                    break;
                case 4:
                    if (random.nextInt(100) < 50) {
                        System.out.println("🏃 Vous réussissez à fuir !");
                        enCombat = false;
                        continue;
                    } else {
                        System.out.println("❌ Vous n'arrivez pas à fuir !");
                    }
                    break;
                default:
                    System.out.println("Action invalide !");
                    continue;
            }

            if (monstre.estVivant() && enCombat) {
                monstre.attaquer(joueur);
            }
        }

        if (!monstre.estVivant()) {
            victoire(monstre);
        }
    }

    private void victoire(Monstre monstre) {
        System.out.println("\n🎉 Victoire ! Vous avez vaincu " + monstre.getNom() + " !");

        int recompenseOr = 20 + random.nextInt(30);
        joueur.getInventaire().ajouterCoins(recompenseOr);
        System.out.println("💰 Vous gagnez " + recompenseOr + " pièces d'or !");

        if (random.nextInt(100) < 40) {
            Objet butin = genererButin();
            joueur.getInventaire().ajouter(butin);
            System.out.println("🎁 Le monstre laisse tomber : " + butin.getNom() + " !");
        }
    }

    private Objet genererButin() {
        int type = random.nextInt(5);
        switch (type) {
            case 0:
                return new Arme("Dague du " + nomsMonstres[random.nextInt(nomsMonstres.length)], 30, 8 + random.nextInt(10));
            case 1:
                return new Armure("Bouclier Trouvé", 25, 5 + random.nextInt(8));
            case 2:
                return new Potion("Potion Mystérieuse", 15, 20 + random.nextInt(30));
            case 3:
                return new Sort("Sort Ancien", 40, 15 + random.nextInt(15));
            case 4:
                return new Materia("Fragment de Materia", 60, 3 + random.nextInt(5));
            default:
                return new Potion("Fiole de Soin", 10, 25);
        }
    }

    private void sEquiper() {
        System.out.println("\n=== ÉQUIPEMENT ===");
        System.out.println("1. Équiper une Arme");
        System.out.println("2. Équiper une Armure");
        System.out.println("0. Retour");
        System.out.print("Votre choix : ");

        int choix = lireChoix();

        switch (choix) {
            case 1:
                joueur.equiperArme(scanner);
                break;
            case 2:
                joueur.equiperArmure(scanner);
                break;
        }
    }

    private void sauvegarderPartie() {
        System.out.println("\n💾 SAUVEGARDE DE LA PARTIE");
        Sauvegarde.sauvegarder(joueur);
    }

    private void chargerPartie() {
        System.out.println("\n📂 CHARGEMENT D'UNE PARTIE");
        if (!Sauvegarde.sauvegardeExiste()) {
            System.out.println("❌ Aucune sauvegarde trouvée !");
            return;
        }
        System.out.println("⚠️  Attention : Charger une partie remplacera votre progression actuelle !");
        System.out.println("Voulez-vous continuer ? (1 = Oui, 0 = Non)");
        System.out.print("Votre choix : ");
        int confirmation = lireChoix();
        if (confirmation == 1) {
            Joueur joueurCharge = Sauvegarde.charger();
            if (joueurCharge != null) {
                this.joueur = joueurCharge;
                System.out.println("📊 Statistiques du personnage chargé :");
                joueur.afficherStatuts();
            }
        } else {
            System.out.println("Chargement annulé.");
        }
    }

    private void lancerModeMultijoueur() {
        System.out.println("\n🌐 MODE MULTIJOUEUR");
        GameMultijoueur gameMulti = new GameMultijoueur(joueur, scanner);
        gameMulti.demarrer();
    }

    private void lancerServeur() {
        System.out.println("\n🖥️  LANCER UN SERVEUR");
        System.out.print("Port du serveur (défaut: 8888) : ");
        scanner.nextLine(); // Vider le buffer
        String portStr = scanner.nextLine().trim();
        int port = portStr.isEmpty() ? 8888 : Integer.parseInt(portStr);
        
        ServeurMultijoueur serveur = new ServeurMultijoueur(port);
        serveur.demarrer();
        
        System.out.println("\n⚠️  Le serveur tourne en arrière-plan.");
        System.out.println("   D'autres joueurs peuvent maintenant se connecter !");
        System.out.println("   Vous pouvez continuer à jouer en solo ou rejoindre votre propre serveur.");
    }

    public static void main(String[] args) {
        Game jeu = new Game();
        jeu.lancerJeu();
    }
}
