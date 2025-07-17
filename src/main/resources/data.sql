INSERT INTO Usuario(id, user, activo,token,refreshToken, urlFoto) VALUES(null,"d",true,"12","12","das");
INSERT INTO Usuario(id, user, activo, token, refreshToken, urlFoto) VALUES (null, "usuario1", true, "token1", "refresh1", "urlFoto1");
INSERT INTO Usuario(id, user, activo, token, refreshToken, urlFoto) VALUES (null, "usuario2", true, "token2", "refresh2", "urlFoto2");
INSERT INTO Usuario(id, user, activo, token, refreshToken, urlFoto) VALUES (null, "usuario3", true, "token3", "refresh3", "urlFoto3");

INSERT INTO Usuario(spotifyID) VALUES ('ikw8wcspnw2mbtrr6th0py82m');

INSERT INTO Comunidad(nombre, descripcion, urlFoto, urlPortada, artista)
VALUES
    ('Rock', 'Género musical con guitarras eléctricas y batería',
     'https://www.verneripohjola.com/wp-content/uploads/2023/03/1_rock.jpg',
     'https://www.verneripohjola.com/wp-content/uploads/2023/03/1_rock.jpg',
     'Rock'),

    ('Billie Eilish', 'Comunidad para fans de Billie Eilish y su estilo dark pop',
     'https://www.nme.com/wp-content/uploads/2023/12/Billie-Eilish-1-1.jpg',
     'https://www.nme.com/wp-content/uploads/2023/12/Billie-Eilish-1-1.jpg',
     'Billie Eilish'),

    ('Taylor Swift', 'Para quienes siguen sus eras y letras íntimas',
     'https://static-live.nmas.com.mx/nmas-news/2024-12/taylor-swift%20(1).jpg',
     'https://static-live.nmas.com.mx/nmas-news/2024-12/taylor-swift%20(1).jpg',
     'Taylor Swift'),

    ('K-Pop', 'Fanáticos del pop coreano: BTS, BLACKPINK y más',
     'https://th.bing.com/th/id/R.9be06181ff3ff7a36a05e3325b8f39a9?rik=Cfv%2bw%2bdInKY3GA&pid=ImgRaw&r=0',
     'https://th.bing.com/th/id/R.9be06181ff3ff7a36a05e3325b8f39a9?rik=Cfv%2bw%2bdInKY3GA&pid=ImgRaw&r=0',
     'Kpop'),

    ('Synthwave', 'Vibras retro, neón y nostalgia de los 80',
     'https://th.bing.com/th/id/R.f856b3913f0eeb9fbeb4bba50a87b31f?rik=YtQ3wsjcR3lGBw&pid=ImgRaw&r=0',
     'https://th.bing.com/th/id/R.f856b3913f0eeb9fbeb4bba50a87b31f?rik=YtQ3wsjcR3lGBw&pid=ImgRaw&r=0',
     'Synthwave'),

    ('Rap Argentino', 'Comunidad del freestyle y rap local',
     'https://th.bing.com/th/id/OIP.LVS69fwUY2kx6l74ssW3GQHaHa?rs=1&pid=ImgDetMain',
     'https://th.bing.com/th/id/OIP.LVS69fwUY2kx6l74ssW3GQHaHa?rs=1&pid=ImgDetMain',
     'Rap Argentino'),

    ('Indie', 'Espacio para descubrir artistas alternativos y sonidos únicos',
     'https://th.bing.com/th/id/OIP.zokFULBDfVkWCF6HuIfeoAHaFi?rs=1&pid=ImgDetMain',
     'https://th.bing.com/th/id/OIP.zokFULBDfVkWCF6HuIfeoAHaFi?rs=1&pid=ImgDetMain',
     'Indie'),

    ('Bad Bunny', 'Trap latino, reggaetón y los vibes de Benito',
     'https://okdiario.com/img/2023/05/18/bad-bunny.-.jpg',
     'https://okdiario.com/img/2023/05/18/bad-bunny.-.jpg',
     'Bad Bunny'),

    ('Lo-fi', 'Beats chill para relajarte, estudiar o flotar',
     'https://static.vecteezy.com/system/resources/previews/022/508/213/large_2x/lofi-art-style-8k-image-landscape-generative-ai-free-photo.jpeg',
     'https://static.vecteezy.com/system/resources/previews/022/508/213/large_2x/lofi-art-style-8k-image-landscape-generative-ai-free-photo.jpeg',
     'Lo-fi'),

    ('Harry Styles', 'Charla, moda y música con estilo británico',
     'https://www.billboard.com/wp-content/uploads/2022/08/Harry-Styles-2022-billboard-1548.jpg',
     'https://www.billboard.com/wp-content/uploads/2022/08/Harry-Styles-2022-billboard-1548.jpg',
     'Harry Styles');


INSERT INTO estado_de_animo (nombre, energy, danceability, valence, tempo)
VALUES
    ('Feliz', 0.8, 0.7, 0.9, 120.0),
    ('Triste', 0.2, 0.3, 0.2, 70.0),
    ('Enojado', 0.9, 0.4, 0.1, 140.0);
INSERT INTO artista (nombre, email, password, fotoPerfil, portada)
VALUES ('Pedrito', 'messi@gmail.com', '123', 'https://i.pinimg.com/736x/4f/c2/23/4fc223a0208622fb42f8a2b5de3b3740.jpg','https://i.pinimg.com/736x/4f/c2/23/4fc223a0208622fb42f8a2b5de3b3740.jpg'),
       ('Luna Vega', 'luna.vega@email.com', '123', 'https://th.bing.com/th/id/R.d59599278978c2afd4e7529e8381571c?rik=MGENXIuIfylsCA&pid=ImgRaw&r=0', 'https://th.bing.com/th/id/R.d59599278978c2afd4e7529e8381571c?rik=MGENXIuIfylsCA&pid=ImgRaw&r=0'),
       ('Noah Díaz', 'noah.diaz@email.com', '123', 'https://tse3.mm.bing.net/th/id/OIP.jOD09W-y10JM7vizUL8yjwAAAA?rs=1&pid=ImgDetMain&o=7&rm=3', 'https://tse3.mm.bing.net/th/id/OIP.jOD09W-y10JM7vizUL8yjwAAAA?rs=1&pid=ImgDetMain&o=7&rm=3'),
       ('Mía Torres', 'mia.torres@email.com', '123', 'https://i.pinimg.com/736x/4f/c2/23/4fc223a0208622fb42f8a2b5de3b3740.jpg', 'https://i.pinimg.com/736x/4f/c2/23/4fc223a0208622fb42f8a2b5de3b3740.jpg'),
       ('Thiago Cruz', 'thiago.cruz@email.com', '123', 'https://i.pinimg.com/200x150/09/90/fe/0990fe16f61df266c4fc0923bff98c3b.jpg', 'https://i.pinimg.com/736x/35/f8/61/35f8617482bcb0b95a13d219a5d7e040.jpg'),
       ('Sofía Ríos', 'sofia.rios@email.com', '123', 'https://i.pinimg.com/200x150/09/90/fe/0990fe16f61df266c4fc0923bff98c3b.jpg', 'https://i.pinimg.com/736x/8c/e2/55/8ce25584ec9e34cd1f982878728fd3f1.jpg'),
       ('Benjamín Vidal', 'benja.vidal@email.com', '123', 'https://i.pinimg.com/originals/f0/0d/ae/f00dae40fa38727e2ab1eaa4760f2e6c.jpg', 'https://i.pinimg.com/736x/38/58/56/385856ca15cb33d39cb928f1a3bc9f99.jpg'),
       ('Valentina Paz', 'valen.paz@email.com', '123', 'https://i.pinimg.com/originals/f0/0d/ae/f00dae40fa38727e2ab1eaa4760f2e6c.jpg', 'https://i.pinimg.com/736x/f3/e4/16/f3e41619437d3a3e03f3f38037b4523d.jpg');
INSERT INTO preescucha
(fechaEscucha, precio, preescuchaFotoUrl, rutaAudio, titulo, artista_id)
VALUES
    (

        NOW(),
        100,
        '/spring/uploads/portadas-preescucha/3dee863d-b484-47c9-a96f-9d1372b369a3-messi-scaled.jpg',
        '/spring/uploads/preescuchas-audio/33bdefd5-edec-4328-a83a-f9c505e0df76-incredimod-downtown-estela-320kbps-8980277.mp3',
        'prueba',
     1
    );

INSERT Favorito(spotifyArtistId, usuario_id) VALUES ('LOCAL_1',5);
       INSERT Favorito(spotifyArtistId, usuario_id) VALUES ('LOCAL_2',5);
       INSERT Favorito(spotifyArtistId, usuario_id) VALUES ('LOCAL_3',5);
        INSERT Favorito(spotifyArtistId, usuario_id) VALUES ('LOCAL_4',5);


INSERT INTO Post (contenido, fecha, artista_id) VALUES
('¡Ya pueden escuchar mi nuevo EP completo! 🔥', '2025-07-15 10:30:00', 1),
('Hoy grabo voces para un tema que me emociona mucho 🎤', '2025-07-14 09:20:00', 2),
('¿Qué opinan del último lanzamiento? Los leo 👇', '2025-07-14 20:05:00', 3),
('Ensayando para la gira. ¡Nos vemos pronto! 🚌🎸', '2025-07-13 17:45:00', 4),
('Nuevo beat en camino 🎧', '2025-07-13 12:00:00', 5),
('Recuerdo de un show que me marcó para siempre 💜', '2025-07-12 18:30:00', 3),
('Se viene un tema con un feat que no se imaginan 😏', '2025-07-12 21:15:00', 3),
('Hoy soltamos otra preescucha solo para fans 🔐', '2025-07-11 14:50:00', 4),
('Gracias por llenar el Luna Park 💫', '2025-07-11 19:00:00', 4),
('¿A qué ciudad quieren que vayamos primero?', '2025-07-10 11:25:00', 3),
('Compuse esto en una noche de lluvia 🌧️🎶', '2025-07-09 23:10:00', 4),
('¡Ya está disponible la preescucha exclusiva! 🚨', '2025-07-09 10:00:00', 1),
('Día de mezcla en el estudio 🎚️', '2025-07-08 16:40:00', 2),
('Gracias por tanto cariño 💌', '2025-07-07 08:30:00', 2),
('Estoy viviendo un sueño. Gracias por acompañarme 🥹', '2025-07-06 20:45:00', 1),
('Pronto anuncio fechas en el sur 🇦🇷', '2025-07-06 13:05:00', 2),
('Les dejo esta letra que escribí anoche 💭', '2025-07-05 21:30:00', 3),
('Volviendo al estudio después de una mini pausa ❤️‍🔥', '2025-07-05 17:15:00', 4),
('¿Team baladas o team beats?', '2025-07-04 12:50:00', 5),
('¡Mi primer vinilo ya está a la venta! 🎵🖤', '2025-07-04 19:40:00', 3),
('Subí un nuevo demo a la preescucha 👂', '2025-07-03 15:20:00', 2),
('Miren lo que pasó en el backstage del video 🤫', '2025-07-02 18:30:00', 1),
('Gracias por apoyar cada paso que doy 🚀', '2025-07-02 09:10:00', 2),
('Se filtró un pedazo del track... ¿Lo escucharon? 😳', '2025-07-01 22:50:00', 3),
('Hoy grabé con cuerdas en vivo por primera vez 🎻', '2025-07-01 14:30:00', 4),
('Nuevo fondo de pantalla para fans. Link en bio ✨', '2025-06-30 20:00:00', 5),
('La preescucha tuvo más de 5k reproducciones en un día 🔥🔥', '2025-06-30 10:25:00', 3),
('Mi mamá escuchó el nuevo tema y lloró 🥲', '2025-06-29 16:15:00', 4),
('¿Se imaginan una colaboración con [otro artista]? 🤝', '2025-06-29 11:40:00', 1),
('El amor que me dan es mi motor. Gracias 🙌', '2025-06-28 19:55:00', 2),
('Feliz de anunciarles mi nuevo álbum 🥁', '2025-06-28 13:00:00', 3);
