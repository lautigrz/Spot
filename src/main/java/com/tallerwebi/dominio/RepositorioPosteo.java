package com.tallerwebi.dominio;

import java.util.List;
import java.util.Optional;

public interface RepositorioPosteo {
    void guardar(Post post);
    List<Post> obtenerPostsDeArtistasFavoritos(List<Long> idsLocales);
    List<Post> obtenerPostsPorArtista(Long artistaId);

    Post obtenerPosteoPorId(Long idPosteo);
}
