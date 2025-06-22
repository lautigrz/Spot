INSERT INTO Usuario(id, user, activo,token,refreshToken, urlFoto) VALUES(null,"d",true,"12","12","das");
INSERT INTO Comunidad(nombre, descripcion, urlFoto)
VALUES
    ('Rock', 'Género musical con guitarras eléctricas y batería', 'https://www.verneripohjola.com/wp-content/uploads/2023/03/1_rock.jpg'),
    ('Billie Eilish', 'Comunidad para fans de Billie Eilish y su estilo dark pop', 'https://www.nme.com/wp-content/uploads/2023/12/Billie-Eilish-1-1.jpg'),
    ('Taylor Swift', 'Para quienes siguen sus eras y letras íntimas', 'https://static-live.nmas.com.mx/nmas-news/2024-12/taylor-swift%20(1).jpg'),
    ('K-Pop', 'Fanáticos del pop coreano: BTS, BLACKPINK y más', 'https://th.bing.com/th/id/R.9be06181ff3ff7a36a05e3325b8f39a9?rik=Cfv%2bw%2bdInKY3GA&pid=ImgRaw&r=0'),
    ('Synthwave', 'Vibras retro, neón y nostalgia de los 80', 'https://th.bing.com/th/id/R.f856b3913f0eeb9fbeb4bba50a87b31f?rik=YtQ3wsjcR3lGBw&pid=ImgRaw&r=0'),
    ('Rap Argentino', 'Comunidad del freestyle y rap local', 'https://th.bing.com/th/id/OIP.LVS69fwUY2kx6l74ssW3GQHaHa?rs=1&pid=ImgDetMain'),
    ('Indie', 'Espacio para descubrir artistas alternativos y sonidos únicos', 'https://th.bing.com/th/id/OIP.zokFULBDfVkWCF6HuIfeoAHaFi?rs=1&pid=ImgDetMain');

INSERT INTO estado_de_animo (nombre, energy, danceability, valence, tempo)
VALUES
    ('Feliz', 0.8, 0.7, 0.9, 120.0),
    ('Triste', 0.2, 0.3, 0.2, 70.0),
    ('Enojado', 0.9, 0.4, 0.1, 140.0);
