package com.tallerwebi.punta_a_punta;

import java.io.IOException;

public class ReiniciarDB {
    public static void limpiarBaseDeDatos() {
        try {
            String dbHost = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
            String dbPort = System.getenv("DB_PORT") != null ? System.getenv("DB_PORT") : "3306";
            String dbName = System.getenv("DB_NAME") != null ? System.getenv("DB_NAME") : "tallerwebi";
            String dbUser = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "user";
            String dbPassword = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "user";

            String sqlCommands =
                    "DELETE FROM Usuario;\n" +
                            "DELETE FROM Comunidad;\n" +
                            "DELETE FROM estado_de_animo;\n" +
                            "ALTER TABLE Usuario AUTO_INCREMENT = 1;\n" +
                            "ALTER TABLE Comunidad AUTO_INCREMENT = 1;\n" +
                            "ALTER TABLE estado_de_animo AUTO_INCREMENT = 1;\n" +
                            "INSERT INTO Usuario(id, user, activo, token, refreshToken, urlFoto) VALUES (null, 'd', true, '12', '12', 'das');\n" +
                            "INSERT INTO Usuario(id, user, activo, token, refreshToken, urlFoto) VALUES (null, 'usuario1', true, 'token1', 'refresh1', 'urlFoto1');\n" +
                            "INSERT INTO Usuario(id, user, activo, token, refreshToken, urlFoto) VALUES (null, 'usuario2', true, 'token2', 'refresh2', 'urlFoto2');\n" +
                            "INSERT INTO Usuario(id, user, activo, token, refreshToken, urlFoto) VALUES (null, 'usuario3', true, 'token3', 'refresh3', 'urlFoto3');\n" +
                            "INSERT INTO Comunidad(nombre, descripcion, urlFoto) VALUES\n" +
                            "  ('Rock', 'Género musical con guitarras eléctricas y batería', 'https://www.verneripohjola.com/wp-content/uploads/2023/03/1_rock.jpg'),\n" +
                            "  ('Billie Eilish', 'Comunidad para fans de Billie Eilish y su estilo dark pop', 'https://www.nme.com/wp-content/uploads/2023/12/Billie-Eilish-1-1.jpg'),\n" +
                            "  ('Taylor Swift', 'Para quienes siguen sus eras y letras íntimas', 'https://static-live.nmas.com.mx/nmas-news/2024-12/taylor-swift%20(1).jpg'),\n" +
                            "  ('K-Pop', 'Fanáticos del pop coreano: BTS, BLACKPINK y más', 'https://th.bing.com/th/id/R.9be06181ff3ff7a36a05e3325b8f39a9?rik=Cfv%2bw%2bdInKY3GA&pid=ImgRaw&r=0'),\n" +
                            "  ('Synthwave', 'Vibras retro, neón y nostalgia de los 80', 'https://th.bing.com/th/id/R.f856b3913f0eeb9fbeb4bba50a87b31f?rik=YtQ3wsjcR3lGBw&pid=ImgRaw&r=0'),\n" +
                            "  ('Rap Argentino', 'Comunidad del freestyle y rap local', 'https://th.bing.com/th/id/OIP.LVS69fwUY2kx6l74ssW3GQHaHa?rs=1&pid=ImgDetMain'),\n" +
                            "  ('Indie', 'Espacio para descubrir artistas alternativos y sonidos únicos', 'https://th.bing.com/th/id/OIP.zokFULBDfVkWCF6HuIfeoAHaFi?rs=1&pid=ImgDetMain');\n" +
                            "INSERT INTO estado_de_animo (nombre, energy, danceability, valence, tempo) VALUES\n" +
                            "  ('Feliz', 0.8, 0.7, 0.9, 120.0),\n" +
                            "  ('Triste', 0.2, 0.3, 0.2, 70.0),\n" +
                            "  ('Enojado', 0.9, 0.4, 0.1, 140.0);";

            String comando = String.format(
                    "docker exec tallerwebi-mysql mysql -h %s -P %s -u %s -p%s %s -e \"%s\"",
                    dbHost, dbPort, dbUser, dbPassword, dbName, sqlCommands
            );

            Process process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", comando});
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Base de datos limpiada exitosamente");
            } else {
                System.err.println("Error al limpiar la base de datos. Exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Error ejecutando script de limpieza: " + e.getMessage());
            e.printStackTrace();
        }
    }
}