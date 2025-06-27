package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.FavoritoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicioFavoritoImpl implements ServicioFavorito {


    private RepositorioFavorito repositorioFavorito;
    private SpotifyApi spotifyApi;

    public ServicioFavoritoImpl(RepositorioFavorito repositorioFavorito, SpotifyApi spotifyApi) {
        this.repositorioFavorito = repositorioFavorito;
        this.spotifyApi = spotifyApi;
    }

    @Override
    public void agregarFavorito(String spotifyArtistId, Usuario usuario) {
        if (!repositorioFavorito.yaEsFavorito(spotifyArtistId, usuario.getId())) {
            Favorito favorito = new Favorito();
            favorito.setSpotifyArtistId(spotifyArtistId);
            favorito.setUsuario(usuario);
            repositorioFavorito.agregarFavorito(favorito);
        }
    }

    @Override
    public List<FavoritoDTO> obtenerFavoritos(Usuario usuario) {
        List<Favorito> favoritos = repositorioFavorito.obtenerFavoritosDeUsuario(usuario.getId());

        return favoritos.stream().map(fav -> {
            try {
                // Consultar Spotify para obtener datos del artista
                Artist artista = spotifyApi.getArtist(fav.getSpotifyArtistId()).build().execute();

                String nombre = artista.getName();
                String imagenUrl = artista.getImages().length > 0 ? artista.getImages()[0].getUrl() : "";

                return new FavoritoDTO(fav.getSpotifyArtistId(), nombre, imagenUrl);
            } catch (Exception e) {
                e.printStackTrace();
                // En caso de error, devolver con id y sin datos
                return new FavoritoDTO(fav.getSpotifyArtistId(), "Nombre no disponible", "");
            }
        }).collect(Collectors.toList());
    }

    @Override
    public boolean yaEsFavorito(String spotifyArtistId, Usuario usuario) {
        return repositorioFavorito.yaEsFavorito(spotifyArtistId, usuario.getId());
    }

}
