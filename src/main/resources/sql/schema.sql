-- ===== Création des tables =====

CREATE TABLE IF NOT EXISTS t_candidat(
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    numero VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS t_correcteur(
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS t_matiere(
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS t_resolution(
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS t_operateur(
    id SERIAL PRIMARY KEY,
    operateur VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS t_parametre(
    id SERIAL PRIMARY KEY,
    id_matiere INT NOT NULL,
    diff NUMERIC(5,2) NOT NULL,
    id_operateur INT NOT NULL,
    id_resolution INT NOT NULL,
    FOREIGN KEY (id_matiere) REFERENCES t_matiere(id),
    FOREIGN KEY (id_operateur) REFERENCES t_operateur(id),
    FOREIGN KEY (id_resolution) REFERENCES t_resolution(id)
);

CREATE TABLE IF NOT EXISTS t_note(
    id SERIAL PRIMARY KEY,
    id_candidat INT NOT NULL,
    id_correcteur INT NOT NULL,
    id_matiere INT NOT NULL,
    note NUMERIC(5,2) NOT NULL,
    FOREIGN KEY (id_candidat) REFERENCES t_candidat(id),
    FOREIGN KEY (id_correcteur) REFERENCES t_correcteur(id),
    FOREIGN KEY (id_matiere) REFERENCES t_matiere(id)
);

CREATE TABLE IF NOT EXISTS t_notefinale(
    id SERIAL PRIMARY KEY,
    id_candidat INT NOT NULL,
    id_matiere INT NOT NULL,
    note_finale NUMERIC(5,2) NOT NULL,
    FOREIGN KEY (id_candidat) REFERENCES t_candidat(id),
    FOREIGN KEY (id_matiere) REFERENCES t_matiere(id)
);

-- ===== Données de test =====

INSERT INTO t_candidat (nom, numero)
SELECT 'Rakoto Jean', 'C001'
WHERE NOT EXISTS (SELECT 1 FROM t_candidat WHERE numero = 'C001');

INSERT INTO t_correcteur (nom)
SELECT 'Prof Rakoto' WHERE NOT EXISTS (SELECT 1 FROM t_correcteur WHERE nom = 'Prof Rakoto');
INSERT INTO t_correcteur (nom)
SELECT 'Prof Rabe' WHERE NOT EXISTS (SELECT 1 FROM t_correcteur WHERE nom = 'Prof Rabe');
INSERT INTO t_correcteur (nom)
SELECT 'Prof Andria' WHERE NOT EXISTS (SELECT 1 FROM t_correcteur WHERE nom = 'Prof Andria');
INSERT INTO t_correcteur (nom)
SELECT 'Prof Ranaivo' WHERE NOT EXISTS (SELECT 1 FROM t_correcteur WHERE nom = 'Prof Ranaivo');
INSERT INTO t_correcteur (nom)
SELECT 'Prof Rasoa' WHERE NOT EXISTS (SELECT 1 FROM t_correcteur WHERE nom = 'Prof Rasoa');

INSERT INTO t_matiere (nom)
SELECT 'Mathematiques' WHERE NOT EXISTS (SELECT 1 FROM t_matiere WHERE nom = 'Mathematiques');
INSERT INTO t_matiere (nom)
SELECT 'Physique' WHERE NOT EXISTS (SELECT 1 FROM t_matiere WHERE nom = 'Physique');
INSERT INTO t_matiere (nom)
SELECT 'Chimie' WHERE NOT EXISTS (SELECT 1 FROM t_matiere WHERE nom = 'Chimie');
INSERT INTO t_matiere (nom)
SELECT 'Informatique' WHERE NOT EXISTS (SELECT 1 FROM t_matiere WHERE nom = 'Informatique');
INSERT INTO t_matiere (nom)
SELECT 'Anglais' WHERE NOT EXISTS (SELECT 1 FROM t_matiere WHERE nom = 'Anglais');

INSERT INTO t_resolution (nom)
SELECT 'Petit' WHERE NOT EXISTS (SELECT 1 FROM t_resolution WHERE nom = 'Petit');
INSERT INTO t_resolution (nom)
SELECT 'Grand' WHERE NOT EXISTS (SELECT 1 FROM t_resolution WHERE nom = 'Grand');
INSERT INTO t_resolution (nom)
SELECT 'Moyenne' WHERE NOT EXISTS (SELECT 1 FROM t_resolution WHERE nom = 'Moyenne');

INSERT INTO t_operateur (operateur)
SELECT '<' WHERE NOT EXISTS (SELECT 1 FROM t_operateur WHERE operateur = '<');
INSERT INTO t_operateur (operateur)
SELECT '<=' WHERE NOT EXISTS (SELECT 1 FROM t_operateur WHERE operateur = '<=');
INSERT INTO t_operateur (operateur)
SELECT '>' WHERE NOT EXISTS (SELECT 1 FROM t_operateur WHERE operateur = '>');
INSERT INTO t_operateur (operateur)
SELECT '>=' WHERE NOT EXISTS (SELECT 1 FROM t_operateur WHERE operateur = '>=');

INSERT INTO t_parametre (id_matiere, diff, id_operateur, id_resolution)
SELECT 1, 3.00, 1, 1
WHERE NOT EXISTS (SELECT 1 FROM t_parametre WHERE id_matiere = 1 AND diff = 3.00 AND id_operateur = 1);

INSERT INTO t_parametre (id_matiere, diff, id_operateur, id_resolution)
SELECT 1, 3.00, 3, 3
WHERE NOT EXISTS (SELECT 1 FROM t_parametre WHERE id_matiere = 1 AND diff = 3.00 AND id_operateur = 3);

INSERT INTO t_note (id_candidat, id_correcteur, id_matiere, note)
SELECT 1, 1, 1, 6
WHERE NOT EXISTS (SELECT 1 FROM t_note WHERE id_candidat = 1 AND id_correcteur = 1 AND id_matiere = 1);

INSERT INTO t_note (id_candidat, id_correcteur, id_matiere, note)
SELECT 1, 2, 1, 7
WHERE NOT EXISTS (SELECT 1 FROM t_note WHERE id_candidat = 1 AND id_correcteur = 2 AND id_matiere = 1);

INSERT INTO t_note (id_candidat, id_correcteur, id_matiere, note)
SELECT 1, 3, 1, 7
WHERE NOT EXISTS (SELECT 1 FROM t_note WHERE id_candidat = 1 AND id_correcteur = 3 AND id_matiere = 1);
