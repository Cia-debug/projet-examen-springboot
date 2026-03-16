-- ============================================================
--  SCRIPT COMPLET : Structure + Données de base + Tests Fallback
-- ============================================================


-- ======================== STRUCTURE ========================

CREATE TABLE t_candidat(
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    numero VARCHAR(20) NOT NULL
);

CREATE TABLE t_correcteur(
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL
);

CREATE TABLE t_matiere(
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL
);

CREATE TABLE t_resolution(
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL
);

CREATE TABLE t_operateur(
    id SERIAL PRIMARY KEY,
    operateur VARCHAR(2) NOT NULL
);

CREATE TABLE t_parametre(
    id SERIAL PRIMARY KEY,
    id_matiere INT NOT NULL,
    diff NUMERIC(5,2) NOT NULL,
    id_operateur INT NOT NULL,
    id_resolution INT NOT NULL,
    FOREIGN KEY (id_matiere) REFERENCES t_matiere(id),
    FOREIGN KEY (id_operateur) REFERENCES t_operateur(id),
    FOREIGN KEY (id_resolution) REFERENCES t_resolution(id)
);

CREATE TABLE t_note(
    id SERIAL PRIMARY KEY,
    id_candidat INT NOT NULL,
    id_correcteur INT NOT NULL,
    id_matiere INT NOT NULL,
    note NUMERIC(5,2) NOT NULL,
    FOREIGN KEY (id_candidat) REFERENCES t_candidat(id),
    FOREIGN KEY (id_correcteur) REFERENCES t_correcteur(id),
    FOREIGN KEY (id_matiere) REFERENCES t_matiere(id)
);

CREATE TABLE t_notefinale(
    id SERIAL PRIMARY KEY,
    id_candidat INT NOT NULL,
    id_matiere INT NOT NULL,
    note_finale NUMERIC(5,2) NOT NULL,
    FOREIGN KEY (id_candidat) REFERENCES t_candidat(id),
    FOREIGN KEY (id_matiere) REFERENCES t_matiere(id)
);


-- ======================== DONNÉES DE RÉFÉRENCE ========================

-- Candidats (id=1..4)
INSERT INTO t_candidat (nom, numero) VALUES
('Candidat1', 'C001'),
('Candidat2', 'C002'),
('Candidat3', 'C003'),
('Candidat4', 'C004');

-- Correcteurs (id=1..3)
INSERT INTO t_correcteur (nom) VALUES
('Correcteur1'),
('Correcteur2'),
('Correcteur3');

-- Matières (id=1..3)
INSERT INTO t_matiere (nom) VALUES
('JAVA'),
('PHP'),
('Informatique');

-- Résolutions (id=1..3)
INSERT INTO t_resolution (nom) VALUES
('Petit'),       -- id=1
('Grand'),       -- id=2
('Moyenne');      -- id=3

-- Opérateurs (id=1..4)
INSERT INTO t_operateur (operateur) VALUES
('<'),            -- id=1
('<='),           -- id=2
('>'),            -- id=3
('>=');            -- id=4


-- ======================== PARAMÈTRES PAR MATIÈRE ========================

INSERT INTO t_parametre (id_matiere, diff, id_operateur, id_resolution) VALUES
-- JAVA (id_matiere=1) : seuil = 7
(1, 7.00,  1, 2),   -- Règle 1 : diff < 7  → Grand (max)
(1, 7.00,  4, 3),   -- Règle 2 : diff >= 7 → Moyenne

-- PHP (id_matiere=2) : seuil = 2
(2, 2.00,  2, 1),   -- Règle 3 : diff <= 2 → Petit (min)
(2, 2.00,  3, 2),   -- Règle 4 : diff > 2  → Grand (max)

-- Informatique (id_matiere=3) : TROU entre 5 et 10 → pour tester le fallback
(3, 5.00,  1, 1),   -- Règle 5 : diff < 5  → Petit (min)
(3, 10.00, 3, 2);   -- Règle 6 : diff > 10 → Grand (max)


-- ======================== NOTES DES CANDIDATS ========================

INSERT INTO t_note (id_candidat, id_correcteur, id_matiere, note) VALUES

-- Candidat1 en JAVA (3 correcteurs)
(1, 1, 1, 15),
(1, 2, 1, 10),
(1, 3, 1, 12),

-- Candidat1 en PHP (2 correcteurs)
(1, 1, 2, 10),
(1, 2, 2, 10),

-- Candidat2 en JAVA (3 correcteurs)
(2, 1, 1, 8),
(2, 2, 1, 12),
(2, 3, 1, 13),

-- Candidat2 en PHP (2 correcteurs)
(2, 1, 2, 13),
(2, 2, 2, 11),

-- Candidat3 en Informatique (3 correcteurs) → TEST FALLBACK : limite proche
(3, 1, 3, 4),
(3, 2, 3, 8),
(3, 3, 3, 12),

-- Candidat4 en Informatique (3 correcteurs) → TEST FALLBACK : égalité
(4, 1, 3, 6.25),
(4, 2, 3, 10.00),
(4, 3, 3, 13.75);


-- ============================================================
--                  RÉSULTATS ATTENDUS
-- ============================================================
--
-- ┌──────────────┬──────────────┬───────────────────────────────────────────────────────────────────────────┬───────────────┐
-- │ Candidat     │ Matière      │ Calcul détaillé                                                         │ Note finale   │
-- ├──────────────┼──────────────┼───────────────────────────────────────────────────────────────────────────┼───────────────┤
-- │ Candidat1    │ JAVA         │ Notes: 15,10,12 → max=15, min=10, diff=5                                │               │
-- │              │              │ Règle: 5 < 7 → VRAI → Résolution "Grand" → max                         │   15.00       │
-- ├──────────────┼──────────────┼───────────────────────────────────────────────────────────────────────────┼───────────────┤
-- │ Candidat1    │ PHP          │ Notes: 10,10 → max=10, min=10, diff=0                                   │               │
-- │              │              │ Règle: 0 <= 2 → VRAI → Résolution "Petit" → min                         │   10.00       │
-- ├──────────────┼──────────────┼───────────────────────────────────────────────────────────────────────────┼───────────────┤
-- │ Candidat2    │ JAVA         │ Notes: 8,12,13 → max=13, min=8, diff=5                                  │               │
-- │              │              │ Règle: 5 < 7 → VRAI → Résolution "Grand" → max                         │   13.00       │
-- ├──────────────┼──────────────┼───────────────────────────────────────────────────────────────────────────┼───────────────┤
-- │ Candidat2    │ PHP          │ Notes: 13,11 → max=13, min=11, diff=2                                   │               │
-- │              │              │ Règle: 2 <= 2 → VRAI → Résolution "Petit" → min                         │   11.00       │
-- ├──────────────┼──────────────┼───────────────────────────────────────────────────────────────────────────┼───────────────┤
-- │ Candidat3    │ Informatique │ Notes: 4,8,12 → max=12, min=4, diff=8                                   │               │
-- │              │              │ Règle A: 8 < 5 → FAUX, Règle B: 8 > 10 → FAUX                          │               │
-- │              │              │ FALLBACK: |8-5|=3, |8-10|=2 → limite 10 plus proche → "Grand"           │   12.00       │
-- ├──────────────┼──────────────┼───────────────────────────────────────────────────────────────────────────┼───────────────┤
-- │ Candidat4    │ Informatique │ Notes: 6.25,10,13.75 → max=13.75, min=6.25, diff=7.50                   │               │
-- │              │              │ Règle A: 7.50 < 5 → FAUX, Règle B: 7.50 > 10 → FAUX                    │               │
-- │              │              │ FALLBACK: |7.50-5|=2.50, |7.50-10|=2.50 → ÉGALITÉ → plus petite note    │    6.25       │
-- └──────────────┴──────────────┴───────────────────────────────────────────────────────────────────────────┴───────────────┘
--
-- APPELS API :
-- POST /notes-finales/calculer?idCandidat=1&idMatiere=1  → 15.00
-- POST /notes-finales/calculer?idCandidat=1&idMatiere=2  → 10.00
-- POST /notes-finales/calculer?idCandidat=2&idMatiere=1  → 13.00
-- POST /notes-finales/calculer?idCandidat=2&idMatiere=2  → 11.00
-- POST /notes-finales/calculer?idCandidat=3&idMatiere=3  → 12.00  (fallback: limite proche)
-- POST /notes-finales/calculer?idCandidat=4&idMatiere=3  →  6.25  (fallback: égalité)
-- ============================================================
