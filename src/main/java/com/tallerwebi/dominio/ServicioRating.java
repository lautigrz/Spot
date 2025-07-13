package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioRating {

    void guardarRating(Long usuarioId, String spotifyId, Integer puntaje) throws Exception;
    List<Rating> obtenerRating(Long usuarioId);
}
