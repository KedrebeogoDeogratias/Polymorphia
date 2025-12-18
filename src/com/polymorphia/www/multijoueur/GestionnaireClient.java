package com.polymorphia.www.multijoueur;

import java.io.*;
import java.net.*;

/**
 * Gère la connexion d'un client côté serveur
 */
public class GestionnaireClient implements Runnable {
    private Socket socket;
    private ServeurMultijoueur serveur;
    private PrintWriter sortie;
    private BufferedReader entree;
    private String nomJoueur;
    private boolean connecte;
    
    public GestionnaireClient(Socket socket, ServeurMultijoueur serveur) {
        this.socket = socket;
        this.serveur = serveur;
        this.connecte = true;
        
        try {
            this.sortie = new PrintWriter(socket.getOutputStream(), true);
            this.entree = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        try {
            // Premier message = nom du joueur
            String premierMessage = entree.readLine();
            if (premierMessage != null) {
                Message connexion = Message.fromJson(premierMessage);
                if (connexion != null && connexion.getType() == Message.Type.CONNEXION) {
                    this.nomJoueur = connexion.getExpediteur();
                    serveur.enregistrerClient(nomJoueur, this);
                }
            }
            
            // Boucle de lecture des messages
            String ligne;
            while (connecte && (ligne = entree.readLine()) != null) {
                Message message = Message.fromJson(ligne);
                if (message != null) {
                    serveur.traiterMessage(nomJoueur, message);
                }
            }
        } catch (IOException e) {
            // Connexion perdue
        } finally {
            deconnecter();
        }
    }
    
    public void envoyerMessage(String message) {
        if (sortie != null && connecte) {
            sortie.println(message);
        }
    }
    
    public void deconnecter() {
        connecte = false;
        if (nomJoueur != null) {
            serveur.deconnecterClient(nomJoueur);
        }
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getNomJoueur() {
        return nomJoueur;
    }
}
