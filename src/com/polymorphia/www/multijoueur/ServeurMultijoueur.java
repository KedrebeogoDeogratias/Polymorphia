package com.polymorphia.www.multijoueur;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Serveur WebSocket simplifié pour le mode multijoueur
 */
public class ServeurMultijoueur implements Runnable {
    private ServerSocket serverSocket;
    private int port;
    private boolean enMarche;
    private Map<String, GestionnaireClient> clients;
    private Map<String, CombatMultijoueur> combatsEnCours;
    private ExecutorService executor;
    
    public ServeurMultijoueur(int port) {
        this.port = port;
        this.clients = new ConcurrentHashMap<>();
        this.combatsEnCours = new ConcurrentHashMap<>();
        this.executor = Executors.newCachedThreadPool();
    }
    
    public void demarrer() {
        enMarche = true;
        new Thread(this).start();
        System.out.println("🌐 Serveur Polymorphia démarré sur le port " + port);
    }
    
    public void arreter() {
        enMarche = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        System.out.println("🔴 Serveur arrêté");
    }
    
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while (enMarche) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    GestionnaireClient gestionnaire = new GestionnaireClient(clientSocket, this);
                    executor.execute(gestionnaire);
                } catch (SocketException e) {
                    if (enMarche) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void enregistrerClient(String nomJoueur, GestionnaireClient client) {
        clients.put(nomJoueur, client);
        diffuserMessage(new Message(Message.Type.CONNEXION, nomJoueur, nomJoueur + " a rejoint le serveur !"));
        envoyerListeJoueurs();
        System.out.println("✅ " + nomJoueur + " connecté. Joueurs en ligne : " + clients.size());
    }
    
    public void deconnecterClient(String nomJoueur) {
        clients.remove(nomJoueur);
        diffuserMessage(new Message(Message.Type.DECONNEXION, nomJoueur, nomJoueur + " a quitté le serveur."));
        envoyerListeJoueurs();
        System.out.println("❌ " + nomJoueur + " déconnecté. Joueurs en ligne : " + clients.size());
    }
    
    public void diffuserMessage(Message message) {
        String json = message.toJson();
        for (GestionnaireClient client : clients.values()) {
            client.envoyerMessage(json);
        }
    }
    
    public void envoyerMessagePrive(String destinataire, Message message) {
        GestionnaireClient client = clients.get(destinataire);
        if (client != null) {
            client.envoyerMessage(message.toJson());
        }
    }
    
    public void envoyerListeJoueurs() {
        String listeJoueurs = String.join(",", clients.keySet());
        Message message = new Message(Message.Type.LISTE_JOUEURS, "SERVEUR", listeJoueurs);
        diffuserMessage(message);
    }
    
    public void traiterMessage(String nomJoueur, Message message) {
        switch (message.getType()) {
            case CHAT:
                diffuserMessage(message);
                break;
            case DEFI:
                envoyerMessagePrive(message.getDestinataire(), message);
                break;
            case REPONSE_DEFI:
                if (message.getContenu().equals("ACCEPTE")) {
                    demarrerCombat(message.getExpediteur(), message.getDestinataire());
                } else {
                    envoyerMessagePrive(message.getDestinataire(), message);
                }
                break;
            case ACTION_COMBAT:
                traiterActionCombat(nomJoueur, message);
                break;
            default:
                break;
        }
    }
    
    private void demarrerCombat(String joueur1, String joueur2) {
        String idCombat = joueur1 + "_vs_" + joueur2;
        CombatMultijoueur combat = new CombatMultijoueur(joueur1, joueur2);
        combatsEnCours.put(idCombat, combat);
        
        Message debut = new Message(Message.Type.DEBUT_COMBAT, "SERVEUR", joueur1, 
            "Combat contre " + joueur2, 0);
        envoyerMessagePrive(joueur1, debut);
        
        debut = new Message(Message.Type.DEBUT_COMBAT, "SERVEUR", joueur2, 
            "Combat contre " + joueur1, 0);
        envoyerMessagePrive(joueur2, debut);
        
        // Premier tour
        Message tour = new Message(Message.Type.TOUR_JOUEUR, "SERVEUR", joueur1, "C'est votre tour !", 0);
        envoyerMessagePrive(joueur1, tour);
        envoyerMessagePrive(joueur2, new Message(Message.Type.TOUR_JOUEUR, "SERVEUR", joueur2, 
            "Tour de " + joueur1, 0));
    }
    
    private void traiterActionCombat(String nomJoueur, Message message) {
        // Trouver le combat en cours
        for (Map.Entry<String, CombatMultijoueur> entry : combatsEnCours.entrySet()) {
            CombatMultijoueur combat = entry.getValue();
            if (combat.estParticipant(nomJoueur)) {
                String adversaire = combat.getAdversaire(nomJoueur);
                
                // Transmettre l'action à l'adversaire
                Message action = new Message(Message.Type.DEGATS, nomJoueur, adversaire, 
                    message.getContenu(), message.getValeur());
                envoyerMessagePrive(adversaire, action);
                
                // Changer de tour
                combat.changerTour();
                String joueurActuel = combat.getJoueurActuel();
                envoyerMessagePrive(joueurActuel, 
                    new Message(Message.Type.TOUR_JOUEUR, "SERVEUR", joueurActuel, "C'est votre tour !", 0));
                envoyerMessagePrive(combat.getAdversaire(joueurActuel),
                    new Message(Message.Type.TOUR_JOUEUR, "SERVEUR", combat.getAdversaire(joueurActuel), 
                        "Tour de " + joueurActuel, 0));
                break;
            }
        }
    }
    
    public void terminerCombat(String joueur1, String joueur2, String gagnant) {
        String idCombat = joueur1 + "_vs_" + joueur2;
        combatsEnCours.remove(idCombat);
        
        Message fin = new Message(Message.Type.FIN_COMBAT, "SERVEUR", gagnant, 
            gagnant + " a gagné le combat !", 0);
        envoyerMessagePrive(joueur1, fin);
        envoyerMessagePrive(joueur2, fin);
    }
    
    public Set<String> getJoueursConnectes() {
        return clients.keySet();
    }
    
    public boolean estEnMarche() {
        return enMarche;
    }
    
    // Classe interne pour gérer les combats
    private static class CombatMultijoueur {
        private String joueur1;
        private String joueur2;
        private String joueurActuel;
        
        public CombatMultijoueur(String joueur1, String joueur2) {
            this.joueur1 = joueur1;
            this.joueur2 = joueur2;
            this.joueurActuel = joueur1;
        }
        
        public boolean estParticipant(String joueur) {
            return joueur.equals(joueur1) || joueur.equals(joueur2);
        }
        
        public String getAdversaire(String joueur) {
            return joueur.equals(joueur1) ? joueur2 : joueur1;
        }
        
        public void changerTour() {
            joueurActuel = joueurActuel.equals(joueur1) ? joueur2 : joueur1;
        }
        
        public String getJoueurActuel() {
            return joueurActuel;
        }
    }
    
    // Point d'entrée pour lancer le serveur seul
    public static void main(String[] args) {
        int port = 8888;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        ServeurMultijoueur serveur = new ServeurMultijoueur(port);
        serveur.demarrer();
        
        System.out.println("Appuyez sur Entrée pour arrêter le serveur...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        serveur.arreter();
    }
}
