package com.polymorphia.www.multijoueur;

import java.io.Serializable;

/**
 * Représente un message échangé entre le serveur et les clients
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum Type {
        CONNEXION,          // Quand un joueur se connecte
        DECONNEXION,        // Quand un joueur se déconnecte
        CHAT,               // Message de chat
        ACTION_COMBAT,      // Action de combat (attaque, sort, etc.)
        ETAT_JOUEUR,        // État du joueur (vie, stats)
        DEBUT_COMBAT,       // Début d'un combat multijoueur
        FIN_COMBAT,         // Fin d'un combat
        LISTE_JOUEURS,      // Liste des joueurs connectés
        DEFI,               // Défi envoyé à un autre joueur
        REPONSE_DEFI,       // Réponse à un défi (accepté/refusé)
        TOUR_JOUEUR,        // Indique à qui c'est le tour
        DEGATS              // Dégâts infligés
    }
    
    private Type type;
    private String expediteur;
    private String destinataire;
    private String contenu;
    private int valeur;
    private long timestamp;
    
    public Message(Type type, String expediteur, String contenu) {
        this.type = type;
        this.expediteur = expediteur;
        this.contenu = contenu;
        this.destinataire = null;
        this.valeur = 0;
        this.timestamp = System.currentTimeMillis();
    }
    
    public Message(Type type, String expediteur, String destinataire, String contenu, int valeur) {
        this.type = type;
        this.expediteur = expediteur;
        this.destinataire = destinataire;
        this.contenu = contenu;
        this.valeur = valeur;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Sérialisation simple en texte
    public String toJson() {
        return String.format("%s|%s|%s|%s|%d|%d", 
            type.name(), 
            expediteur != null ? expediteur : "", 
            destinataire != null ? destinataire : "",
            contenu != null ? contenu : "",
            valeur,
            timestamp);
    }
    
    // Désérialisation depuis texte
    public static Message fromJson(String json) {
        String[] parts = json.split("\\|", 6);
        if (parts.length >= 4) {
            Type type = Type.valueOf(parts[0]);
            String expediteur = parts[1].isEmpty() ? null : parts[1];
            String destinataire = parts[2].isEmpty() ? null : parts[2];
            String contenu = parts[3];
            int valeur = parts.length > 4 ? Integer.parseInt(parts[4]) : 0;
            return new Message(type, expediteur, destinataire, contenu, valeur);
        }
        return null;
    }
    
    // Getters et Setters
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    public String getExpediteur() { return expediteur; }
    public void setExpediteur(String expediteur) { this.expediteur = expediteur; }
    public String getDestinataire() { return destinataire; }
    public void setDestinataire(String destinataire) { this.destinataire = destinataire; }
    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }
    public int getValeur() { return valeur; }
    public void setValeur(int valeur) { this.valeur = valeur; }
    public long getTimestamp() { return timestamp; }
}
