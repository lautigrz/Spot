package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioRating {

    void guardarRating(String token, String spotifyId, String titulo, String artista, String urlImagen, String uri, Integer puntaje) throws Exception;
    List<Rating> obtenerRating(String spotifyId);
}
