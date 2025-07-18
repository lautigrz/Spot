package com.tallerwebi.dominio;

import java.util.List;
import java.util.Optional;

public interface RepositorioArtista {
    Optional<Artista> buscarPorEmail(String email);
    void guardar(Artista artista);
    Artista buscarPorNombre(String nombre);
    Artista buscarPorId(Long id);
    List<Preescucha> obtenerPreescuchasDeArtista(Long artistaId);
    List<Artista> buscarPorTexto(String texto);
}
