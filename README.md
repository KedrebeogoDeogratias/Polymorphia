# 🎮 POLYMORPHIA

Un jeu de rôle en console développé en Java, avec un mode solo et un mode multijoueur !

---

## 👨‍💻 Développeurs

| Nom | Rôle |
|-----|------|
| **ALLA Emmanuel Odilon** | Développeur |
| **KEDREBEOGO Wendzoodo Deogratias** | Développeur |

---

## 📋 Prérequis

- **Java 21** ou supérieur
- **Eclipse IDE** (recommandé) ou tout autre IDE Java

---

## 🚀 Lancer le jeu

### Avec Eclipse

1. Ouvrir le projet dans Eclipse
2. Clic droit sur `Game.java` → **Run As** → **Java Application**

### En ligne de commande

```bash
cd Polymorphia
javac -d bin src/com/polymorphia/www/*.java src/com/polymorphia/www/multijoueur/*.java
java --module-path bin -m Polymorphia/com.polymorphia.www.Game
```

---

## 🎯 Comment jouer

### Mode Solo

Au lancement, vous incarnez **Javalt de Riv**, un aventurier en quête de gloire !

#### Menu Principal

| Option | Action |
|--------|--------|
| 1 | Commercer avec le marchand |
| 2 | Se déplacer (exploration) |
| 3 | S'équiper (armes/armures) |
| 4 | Utiliser une Materia |
| 5 | Utiliser une Potion |
| 6 | Voir les statistiques |
| 7 | Voir l'inventaire |
| 8 | Sauvegarder la partie |
| 9 | Charger une partie |
| 10 | Mode Multijoueur |
| 11 | Lancer un serveur |
| 0 | Quitter le jeu |

#### Système de combat

Lors de l'exploration, vous pouvez rencontrer des monstres. En combat, vous pouvez :
- **Attaquer** - Utiliser votre arme
- **Lancer un sort** - Utiliser vos sorts magiques
- **Utiliser une potion** - Récupérer des points de vie
- **Fuir** - 50% de chance de réussir

#### Marchand

Achetez des équipements pour devenir plus fort :
- ⚔️ **Armes** - Augmentent vos dégâts
- 🛡️ **Armures** - Augmentent votre défense
- 🧪 **Potions** - Restaurent vos points de vie
- ✨ **Sorts** - Attaques magiques puissantes
- 💎 **Materias** - Bonus d'attaque permanent

---

## 🌐 Mode Multijoueur

### Étape 1 : Lancer un serveur

Un joueur doit héberger la partie :

1. Choisir l'option **11. Lancer un serveur**
2. Entrer le port (par défaut : `8888`)
3. Noter son adresse IP (commande `ipconfig` sur Windows)

### Étape 2 : Rejoindre le serveur

Les autres joueurs rejoignent :

1. Choisir l'option **10. Mode Multijoueur**
2. Entrer l'**adresse IP** du serveur (ex: `192.168.1.42`)
3. Entrer le **port** (ex: `8888`)

### Étape 3 : Jouer ensemble

Une fois connecté :

| Option | Action |
|--------|--------|
| 1 | Voir les joueurs en ligne |
| 2 | Défier un joueur |
| 3 | Répondre à un défi |
| 4 | Envoyer un message (chat) |
| 5 | Voir mes statistiques |
| 0 | Retour au menu principal |

### Combat PvP

Les combats se font au tour par tour :
- Attaquez votre adversaire
- Utilisez des sorts
- Utilisez des potions pour vous soigner
- Le premier à 0 PV perd !

---

## 💾 Sauvegarde

Le jeu supporte la sauvegarde automatique via sérialisation Java :

- **Sauvegarder** : Option 8 → Crée le fichier `polymorphia_save.dat`
- **Charger** : Option 9 → Restaure votre progression

---

## 📁 Structure du projet

```
Polymorphia/
├── src/
│   └── com/polymorphia/www/
│       ├── Game.java          # Point d'entrée du jeu
│       ├── Joueur.java        # Classe du joueur
│       ├── Monstre.java       # Classe des monstres
│       ├── Inventaire.java    # Gestion de l'inventaire
│       ├── Marchand.java      # Système de commerce
│       ├── Sauvegarde.java    # Système de sauvegarde
│       ├── Objet.java         # Classe abstraite des objets
│       ├── Arme.java          # Armes
│       ├── Armure.java        # Armures
│       ├── Potion.java        # Potions
│       ├── Sort.java          # Sorts
│       ├── Materia.java       # Materias
│       └── multijoueur/
│           ├── ServeurMultijoueur.java  # Serveur TCP
│           ├── ClientMultijoueur.java   # Client TCP
│           ├── GestionnaireClient.java  # Gestion des connexions
│           ├── GameMultijoueur.java     # Logique multijoueur
│           └── Message.java             # Messages réseau
└── README.md
```

---

## 🎮 Captures d'écran

```
╔═══════════════════════════════════════════╗
║         BIENVENUE DANS POLYMORPHIA        ║
╚═══════════════════════════════════════════╝

Vous incarnez Javalt de Riv, un aventurier en quête de gloire !

═══════════════════════════════════════════
           MENU PRINCIPAL
═══════════════════════════════════════════
  Vie : 100/100 | Or : 100
═══════════════════════════════════════════
  1. Commercer avec le marchand
  2. Se déplacer (exploration)
  ...
```

---

## 📜 Licence

Projet académique - Tous droits réservés © 2025

**ALLA Emmanuel Odilon** & **KEDREBEOGO Wendzoodo Deogratias**
