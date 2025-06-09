package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioFavorito {
    void agregarFavorito(Favorito favorito);
    List<Favorito> obtenerFavoritosDeUsuario(Long idUsuario);
    boolean yaEsFavorito(String spotifyArtistId, Long idUsuario);
}
