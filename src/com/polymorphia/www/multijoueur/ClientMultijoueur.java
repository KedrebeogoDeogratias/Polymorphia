package com.polymorphia.www.multijoueur;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * Client pour se connecter au serveur multijoueur
 */
public class ClientMultijoueur implements Runnable {
    private Socket socket;
    private PrintWriter sortie;
    private BufferedReader entree;
    private String nomJoueur;
    private boolean connecte;
    private List<String> joueursEnLigne;
    private BlockingQueue<Message> messagesRecus;
    private Consumer<Message> gestionnaireDegats;
    private boolean enCombat;
    private boolean monTour;
    private String adversaireActuel;
    private Message dernierDefi;
    
    public ClientMultijoueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
        this.joueursEnLigne = new CopyOnWriteArrayList<>();
        this.messagesRecus = new LinkedBlockingQueue<>();
        this.connecte = false;
        this.enCombat = false;
        this.monTour = false;
    }
    
    public boolean connecter(String hote, int port) {
        try {
            socket = new Socket(hote, port);
            sortie = new PrintWriter(socket.getOutputStream(), true);
            entree = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            connecte = true;
            
            // Envoyer le message de connexion
            Message connexion = new Message(Message.Type.CONNEXION, nomJoueur, "Connexion");
            sortie.println(connexion.toJson());
            
            // Démarrer le thread de réception
            new Thread(this).start();
            
            System.out.println("✅ Connecté au serveur " + hote + ":" + port);
            return true;
        } catch (IOException e) {
            System.out.println("❌ Impossible de se connecter au serveur : " + e.getMessage());
            return false;
        }
    }
    
    public void deconnecter() {
        connecte = false;
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("🔴 Déconnecté du serveur");
    }
    
    @Override
    public void run() {
        try {
            String ligne;
            while (connecte && (ligne = entree.readLine()) != null) {
                Message message = Message.fromJson(ligne);
                if (message != null) {
                    traiterMessage(message);
                }
            }
        } catch (IOException e) {
            if (connecte) {
                System.out.println("❌ Connexion perdue avec le serveur");
            }
        } finally {
            connecte = false;
        }
    }
    
    private void traiterMessage(Message message) {
        switch (message.getType()) {
            case LISTE_JOUEURS:
                joueursEnLigne.clear();
                if (message.getContenu() != null && !message.getContenu().isEmpty()) {
                    joueursEnLigne.addAll(Arrays.asList(message.getContenu().split(",")));
                }
                break;
                
            case CONNEXION:
                System.out.println("🟢 " + message.getContenu());
                break;
                
            case DECONNEXION:
                System.out.println("🔴 " + message.getContenu());
                break;
                
            case CHAT:
                System.out.println("💬 [" + message.getExpediteur() + "] : " + message.getContenu());
                break;
                
            case DEFI:
                System.out.println("\n⚔️  " + message.getExpediteur() + " vous défie en duel !");
                dernierDefi = message;
                break;
                
            case REPONSE_DEFI:
                if (message.getContenu().equals("REFUSE")) {
                    System.out.println("❌ " + message.getExpediteur() + " a refusé votre défi.");
                }
                break;
                
            case DEBUT_COMBAT:
                enCombat = true;
                adversaireActuel = message.getContenu().replace("Combat contre ", "");
                System.out.println("\n🎮 COMBAT MULTIJOUEUR !");
                System.out.println("⚔️  " + message.getContenu());
                break;
                
            case TOUR_JOUEUR:
                if (message.getContenu().equals("C'est votre tour !")) {
                    monTour = true;
                    System.out.println("\n👉 C'est votre tour !");
                } else {
                    monTour = false;
                    System.out.println("⏳ " + message.getContenu());
                }
                break;
                
            case DEGATS:
                System.out.println("💥 " + message.getExpediteur() + " vous inflige " + message.getValeur() + " dégâts !");
                if (gestionnaireDegats != null) {
                    gestionnaireDegats.accept(message);
                }
                break;
                
            case FIN_COMBAT:
                enCombat = false;
                monTour = false;
                adversaireActuel = null;
                System.out.println("\n🏁 " + message.getContenu());
                break;
                
            default:
                messagesRecus.offer(message);
                break;
        }
    }
    
    public void envoyerChat(String contenu) {
        if (connecte) {
            Message message = new Message(Message.Type.CHAT, nomJoueur, contenu);
            sortie.println(message.toJson());
        }
    }
    
    public void defierJoueur(String adversaire) {
        if (connecte) {
            Message message = new Message(Message.Type.DEFI, nomJoueur, adversaire, "Défi", 0);
            sortie.println(message.toJson());
            System.out.println("⚔️  Défi envoyé à " + adversaire + " !");
        }
    }
    
    public void repondreDefi(boolean accepter) {
        if (connecte && dernierDefi != null) {
            String reponse = accepter ? "ACCEPTE" : "REFUSE";
            Message message = new Message(Message.Type.REPONSE_DEFI, nomJoueur, 
                dernierDefi.getExpediteur(), reponse, 0);
            sortie.println(message.toJson());
            if (accepter) {
                adversaireActuel = dernierDefi.getExpediteur();
            }
            dernierDefi = null;
        }
    }
    
    public void envoyerActionCombat(String action, int degats) {
        if (connecte && enCombat && monTour) {
            Message message = new Message(Message.Type.ACTION_COMBAT, nomJoueur, 
                adversaireActuel, action, degats);
            sortie.println(message.toJson());
            monTour = false;
        }
    }
    
    public void envoyerFinCombat(boolean victoire) {
        if (connecte && enCombat) {
            String gagnant = victoire ? nomJoueur : adversaireActuel;
            Message message = new Message(Message.Type.FIN_COMBAT, nomJoueur, 
                adversaireActuel, gagnant, 0);
            sortie.println(message.toJson());
            enCombat = false;
            monTour = false;
        }
    }
    
    public void setGestionnaireDegats(Consumer<Message> gestionnaire) {
        this.gestionnaireDegats = gestionnaire;
    }
    
    // Getters
    public boolean estConnecte() { return connecte; }
    public boolean estEnCombat() { return enCombat; }
    public boolean estMonTour() { return monTour; }
    public String getAdversaireActuel() { return adversaireActuel; }
    public List<String> getJoueursEnLigne() { return joueursEnLigne; }
    public String getNomJoueur() { return nomJoueur; }
    public Message getDernierDefi() { return dernierDefi; }
}
