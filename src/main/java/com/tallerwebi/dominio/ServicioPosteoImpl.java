package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServicioPosteoImpl implements ServicioPosteo {

    private final RepositorioPosteo repositorioPosteo;
    private final RepositorioFavorito repositorioFavorito;

    public ServicioPosteoImpl(RepositorioPosteo repositorioPosteo, RepositorioFavorito repositorioFavorito) {
        this.repositorioPosteo = repositorioPosteo;
        this.repositorioFavorito = repositorioFavorito;
    }


    @Override
    public void publicarPosteo(Artista artista, String contenido) {
        Post post = new Post();
        post.setContenido(contenido);
        post.setArtista(artista);
        post.setFecha(LocalDateTime.now());

        repositorioPosteo.guardar(post);
    }

    @Override
    public List<Post> obtenerPosteosDeArtistasFavoritos(Usuario usuario) {
        List<Favorito> favoritos = repositorioFavorito.obtenerFavoritosDeUsuario(usuario.getId());

        List<Long> idsLocales = favoritos.stream()
                .map(Favorito::getSpotifyArtistId)
                .filter(id -> id.startsWith("LOCAL_"))
                .map(id -> {
                    try {
                        return Long.parseLong(id.substring(6)); // Extrae el número
                    } catch (NumberFormatException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (idsLocales.isEmpty()) {
            return Collections.emptyList();
        }

        return repositorioPosteo.obtenerPostsDeArtistasFavoritos(idsLocales);
    }

    @Override
    public List<Post> obtenerPosteosDeArtista(Artista artista) {
       return repositorioPosteo.obtenerPostsPorArtista(artista.getId());
    }

    @Override
    public Post obtenerPosteoPorId(Long idPosteo) {
        return repositorioPosteo.obtenerPosteoPorId(idPosteo);

    }
}
