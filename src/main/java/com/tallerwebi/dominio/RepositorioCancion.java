package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioCancion {
    void guardarCancion(Cancion cancion);
    Cancion obtenerPorId(Long id);
    List<Cancion> obtenerTodas();
    void eliminarCancion(Long id);
    Cancion buscarCancionPorElIdDeSpotify(String sporifyId);
}

