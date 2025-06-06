package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioPlaylistImpl implements ServicioPlaylist {

    private RepositorioPlaylist repositorioPlaylist;
    private RepositorioCancion repositorioCancion;
    @Autowired
    public ServicioPlaylistImpl(RepositorioPlaylist repositorioPlaylist, RepositorioCancion repositorioCancion) {
        this.repositorioPlaylist = repositorioPlaylist;
        this.repositorioCancion = repositorioCancion;
    }

    @Override
    public void agregarCancionALaPlaylist(Long idPlaylist,Long idSpotify, String uri) {
        Cancion cancion = repositorioCancion.buscarCancionPorElIdDeSpotify(idSpotify);

        if(cancion == null) {
            cancion = new Cancion();
            cancion.setSpotifyId(idSpotify);
            cancion.setUri(uri);
            repositorioCancion.guardarCancion(cancion);
        }

        repositorioPlaylist.agregarCancionALaPlaylist(idPlaylist, cancion);
    }

    @Override
    public void eliminarCancionALaPlaylist(Long idPlaylist, Long idCancion) {
        Cancion cancion = repositorioCancion.obtenerPorId(idCancion);

        if (cancion == null) {
            throw new IllegalArgumentException("La canción con id " + idCancion + " no existe.");
        }

        repositorioPlaylist.eliminarCancionALaPlaylist(idPlaylist, cancion);
    }

    @Override
    public List<Cancion> obtenerCancionesDeLaPlaylist(Long id) {
        return repositorioPlaylist.obtenerCancionesDeLaPlaylist(id);
    }
}
