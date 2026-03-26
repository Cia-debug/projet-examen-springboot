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



