package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.FavoritoDTO;

import java.util.List;

public interface ServicioFavorito {
    void agregarFavorito(String spotifyArtistId, Usuario usuario);
    List<FavoritoDTO> obtenerFavoritos(Usuario usuario);
    boolean yaEsFavorito(String spotifyArtistId, Usuario usuario);
}
