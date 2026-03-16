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
    operateur INT NOT NULL
);
ALTER TABLE t_operateur
ALTER COLUMN operateur TYPE VARCHAR(2);





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


-- CREATE TABLE t_noteFinale(
--     id SERIAL PRIMARY KEY,
--     id_candidat INT NOT NULL,
--     id_matiere INT NOT NULL,
--     note_finale NUMERIC(5,2) NOT NULL,
--     FOREIGN KEY (id_candidat) REFERENCES t_candidat(id),
--     FOREIGN KEY (id_matiere) REFERENCES t_matiere(id)
-- );

CREATE TABLE t_notefinale(
    id SERIAL PRIMARY KEY,
    id_candidat INT NOT NULL,
    id_matiere INT NOT NULL,
    note_finale NUMERIC(5,2) NOT NULL,
    FOREIGN KEY (id_candidat) REFERENCES t_candidat(id),
    FOREIGN KEY (id_matiere) REFERENCES t_matiere(id)
);


INSERT INTO t_candidat (nom, numero) VALUES
('Candidat1', 'C001'),
('Candidat2', 'C002');

INSERT INTO t_correcteur (nom) VALUES
('Correcteur3');
('Correcteur2'),
('Correcteur3');

INSERT INTO t_matiere (nom) VALUES
('JAVA'),
('PHP');

INSERT INTO t_resolution (nom) VALUES
('Petit'),
('Grand'),
('Moyenne');

INSERT INTO t_operateur (operateur) VALUES
('<'),
('<='),
('>'),
('>=');




-------------------------------------------------------
INSERT INTO t_parametre (id_matiere, diff, id_operateur, id_resolution) VALUES
(1, 7.00, 1, 2),
(1, 7.00, 4, 3),
(2, 2.00, 2, 1),
(2, 2.00, 3, 2);


INSERT INTO t_note (id_candidat, id_correcteur, id_matiere, note) VALUES
(1, 1, 1, 15),
(1, 2, 1, 10),
(1, 9, 1, 12),

(1, 1, 2, 10),
(1, 2, 2, 10),

(2, 1, 1, 8),
(2, 2, 1, 12),
(2, 9, 1, 13),

(2, 1, 2, 13),
(2, 2, 2, 11);

