# Explication des Classes et de leurs Fonctions (Projet Examen)

Ce document explique le rôle de toutes les classes présentes dans ce projet Spring Boot. Le projet suit une architecture classique en couches MVC : Entités (Modèles), Repositories (Accès aux données), Services (Logique métier) et Controllers (Exposition Web/API).

## 1. Couche Modèle (Entités)
Les entités (dans le package `model.entity`) représentent les tables de la base de données. Elles sont annotées avec `@Entity` ou équivalent et mappées aux tables (ex: `t_candidat`, `t_note`, etc.).

*   **`Candidat`** : Représente un étudiant passant l'examen. Contient les informations de base du candidat.
*   **`Correcteur`** : Représente un professeur ou examinateur qui note les copies.
*   **`Matiere`** : Représente une matière de l'examen (ex. Java, SQL, C#).
*   **`Note`** : Représente une note individuelle attribuée par **un** correcteur à un candidat pour une matière donnée.
*   **`NoteFinale`** : Représente la note finale définitive d'un candidat pour une matière, après application des règles d'écart et de résolution entre les différentes notes des correcteurs.
*   **`Operateur`** : Représente les opérateurs mathématiques de comparaison (ex. `<`, `>`, `<=`, `>=`) utilisés dans les règles de calcul de la note finale.
*   **`Resolution`** : Représente l'action de résolution à appliquer lorsque des correcteurs ont un écart de notes (ex: prendre la note "plus_petit", "plus_grand", ou faire la "moyenne").
*   **`Parametre`** : Représente une règle de délibération. Il lie une matière à une tolérance d'écart de notes (`diff`), en utilisant un `Operateur` et en définissant une `Resolution`.

## 2. Couche Repository
Les classes de ce package (ex: `CandidatRepository`, `NoteFinaleRepository`) sont des interfaces étendant généralement `JpaRepository` ou `CrudRepository`.

*   **Leur fonction** : Elles fournissent toutes les méthodes par défaut pour interagir avec la base de données PostgreSQL (CRUD complet : Création, Lecture, Mise à jour, Suppression).
*   Elles contiennent également des requêtes spécifiques, comme trouver toutes les notes d'un candidat pour une matière précise (`findByCandidatIdAndMatiereId`).

## 3. Couche Service (Interfaces et Implémentations)
Les services (dans `service` et `service.impl`) contiennent la logique métier de l'application.

*   **Services Classiques (`CandidatServiceImpl`, `CorrecteurServiceImpl`, `MatiereServiceImpl`, `NoteServiceImpl`, `ParametreServiceImpl`, etc.)** :
    *   **Fonction** : Servent d'intermédiaire entre les contrôleurs et les repositories. Ils gèrent la logique basique d'ajout, de mise à jour, de liste et de récupération par ID.
*   **`NoteFinaleServiceImpl`** : C'est le cœur de l'application contenant la logique la plus complexe.
    *   **Fonction `calculerNoteFinale(Long idCandidat, Long idMatiere)`** :
        1.  Récupère toutes les notes attribuées à un candidat pour une matière.
        2.  Calcule la différence (l'écart) entre la note la plus haute et la plus basse.
        3.  Consulte la liste des règles de configuration (`Parametre`) de cette matière.
        4.  Vérifie quelles règles correspondent à l'écart constaté grâce aux opérateurs. S'il y a plusieurs règles applicables, sélectionne la règle dont la limite est la plus proche de l'écart réel. S'il y a égalité parfaite de distance, sélectionne par sécurité la note la plus basse.
        5.  Applique la `Resolution` choisie (prendre la plus petite, la plus grande, ou la moyenne des notes).
        6.  Sauvegarde le résultat final dans la base de données (`t_notefinale`).

## 4. Couche Controller
Les contrôleurs gèrent les requêtes HTTP (Web ou API).

*   **`ExamenApplication`** : La classe principale annotée `@SpringBootApplication` qui permet de démarrer l'application.
*   **`CandidatController`, `CorrecteurController`, `MatiereController`, `NoteController`, `ParametreController`** :
    *   **Fonctions** : Reçoivent les actions de l'utilisateur depuis les vues HTML/JSP ou requêtes d'API, font appel aux Services correspondants, puis renvoient ou redirigent vers la vue appropriée ou du JSON en injectant les modèles de données (Modèles Thymeleaf ou JSP).
*   **`NoteFinaleController`** :
    *   **Fonction** : Contient le point d'entrée pour déclencher le calcul complexe des notes finales pour les étudiants et renvoyer le résultat à l'interface d'affichage finale de la délibération.
