package com.polymorphia.www;

import java.io.*;

public class Sauvegarde {
    private static final String FICHIER_SAUVEGARDE = "polymorphia_save.dat";

    /**
     * Sauvegarde le joueur dans un fichier
     * @param joueur Le joueur à sauvegarder
     * @return true si la sauvegarde a réussi, false sinon
     */
    public static boolean sauvegarder(Joueur joueur) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FICHIER_SAUVEGARDE))) {
            oos.writeObject(joueur);
            System.out.println("✅ Partie sauvegardée avec succès !");
            return true;
        } catch (IOException e) {
            System.out.println("❌ Erreur lors de la sauvegarde : " + e.getMessage());
            return false;
        }
    }

    /**
     * Charge un joueur depuis le fichier de sauvegarde
     * @return Le joueur chargé, ou null si le chargement a échoué
     */
    public static Joueur charger() {
        File fichier = new File(FICHIER_SAUVEGARDE);
        if (!fichier.exists()) {
            System.out.println("❌ Aucune sauvegarde trouvée !");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(FICHIER_SAUVEGARDE))) {
            Joueur joueur = (Joueur) ois.readObject();
            System.out.println("✅ Partie chargée avec succès !");
            System.out.println("   Bienvenue de retour, " + joueur.getNom() + " !");
            return joueur;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ Erreur lors du chargement : " + e.getMessage());
            return null;
        }
    }

    /**
     * Vérifie si une sauvegarde existe
     * @return true si une sauvegarde existe, false sinon
     */
    public static boolean sauvegardeExiste() {
        return new File(FICHIER_SAUVEGARDE).exists();
    }

    /**
     * Supprime la sauvegarde existante
     * @return true si la suppression a réussi, false sinon
     */
    public static boolean supprimerSauvegarde() {
        File fichier = new File(FICHIER_SAUVEGARDE);
        if (fichier.exists()) {
            return fichier.delete();
        }
        return false;
    }
}
