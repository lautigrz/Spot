INSERT INTO Usuario(id, user, activo,token,refreshToken, urlFoto) VALUES(null,"d",true,"12","12","das");
INSERT INTO Comunidad(nombre, descripcion, nombreCancion)
VALUES
    ('Rock', 'Género musical con guitarras eléctricas y batería', 'spotify:track:3jjujdWJ72nww5eGnfs2E7'),
    ('Rock', 'Género musical con guitarras eléctricas y batería', 'spotify:track:4stQ9ma0kqGifqLQQSgOGH');
INSERT INTO estado_de_animo (nombre, energy, danceability, valence, tempo)
VALUES
    ('Feliz', 0.8, 0.7, 0.9, 120.0),
    ('Triste', 0.2, 0.3, 0.2, 70.0),
    ('Enojado', 0.9, 0.4, 0.1, 140.0);
