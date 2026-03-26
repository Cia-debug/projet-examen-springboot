-- Candidats
INSERT INTO t_candidat (nom, prenom, matricule) VALUES
('Jean',  'MAT001', 'MAT001'),
('Marie', 'MAT002', 'MAT002'),
('Paul',  'MAT003', 'MAT003');

-- Correcteurs
INSERT INTO t_correcteur (nom, prenom) VALUES
('Andry', 'Correcteur1'),
('Hery',  'Correcteur2'),
('Ony',   'Correcteur3');

-- Matières
INSERT INTO t_matiere (nom) VALUES
('JAVA'),
('PHP'),
('PYTHON');

-- Résolutions
INSERT INTO t_resolution (nom) VALUES
('Petit'),
('Grand'),
('Moyenne');

-- Opérateurs
INSERT INTO t_operateur (operateur) VALUES
('<'),
('<='),
('>'),
('>=');

INSERT INTO t_parametre (id_matiere, id_resolution, id_operateur, seuil) VALUES
(1,2,4,5),
(1,3,1,9);

INSERT INTO t_note (id_candidat, id_matiere, id_correcteur, note) VALUES
(1,1,1,10),
(1,1,2,18);

-- (1,1,1,10),
-- (1,1,2,17);

-- (1,1,1,10),
-- (1,1,2,16);