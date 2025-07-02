package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioPosteo {
    void publicarPosteo(Artista artista, String contenido);
    List<Post> obtenerPosteosDeArtistasFavoritos(Usuario usuario);
    List<Post> obtenerPosteosDeArtista(Artista artista);
}
