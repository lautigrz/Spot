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
    private RepositorioArtista repositorioArtista;

    public ServicioFavoritoImpl(RepositorioFavorito repositorioFavorito, SpotifyApi spotifyApi, RepositorioArtista repositorioArtista) {
        this.repositorioFavorito = repositorioFavorito;
        this.spotifyApi = spotifyApi;
        this.repositorioArtista = repositorioArtista;
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
            String id = fav.getSpotifyArtistId();

            try {
                // Si el ID empieza con LOCAL_
                if (id.startsWith("LOCAL_")) {
                    Long idLocal = Long.parseLong(id.substring(6)); // Quita el prefijo
                    Artista artistaLocal = repositorioArtista.buscarPorId(idLocal);

                    if (artistaLocal != null) {
                        return new FavoritoDTO(
                                id,
                                artistaLocal.getNombre(),
                                artistaLocal.getFotoPerfil() != null ? artistaLocal.getFotoPerfil() : ""
                        );
                    } else {
                        return new FavoritoDTO(id, "Artista local no encontrado", "");
                    }
                }

                // Caso artista Spotify (id no numérico)
                Artist artista = spotifyApi.getArtist(id).build().execute();
                String nombre = artista.getName();
                String imagenUrl = artista.getImages().length > 0 ? artista.getImages()[0].getUrl() : "";

                return new FavoritoDTO(id, nombre, imagenUrl);

            } catch (Exception e) {
                e.printStackTrace();
                return new FavoritoDTO(id, "Nombre no disponible", "");
            }
        }).collect(Collectors.toList());
    }

    @Override
    public boolean yaEsFavorito(String spotifyArtistId, Usuario usuario) {
        return repositorioFavorito.yaEsFavorito(spotifyArtistId, usuario.getId());
    }

}
