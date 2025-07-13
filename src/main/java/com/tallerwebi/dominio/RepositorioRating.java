package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioRating {

    void guardarRating(Rating rating);
    List<Rating> obtenerRating(Usuario usuario);

}
