package com.tallerwebi.dominio;

import java.util.Optional;

public interface RepositorioArtista {
    Optional<Artista> buscarPorEmail(String email);
    void guardar(Artista artista);
    Artista buscarPorNombre(String nombre);
    Artista buscarPorId(Long id);
}
