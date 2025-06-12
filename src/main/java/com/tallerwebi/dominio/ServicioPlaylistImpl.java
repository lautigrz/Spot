package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.CancionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ServicioPlaylistImpl implements ServicioPlaylist {

    private RepositorioPlaylist repositorioPlaylist;
    private RepositorioCancion repositorioCancion;
    @Autowired
    public ServicioPlaylistImpl(RepositorioPlaylist repositorioPlaylist, RepositorioCancion repositorioCancion) {
        this.repositorioPlaylist = repositorioPlaylist;
        this.repositorioCancion = repositorioCancion;
    }

    @Override
    public void agregarCancionALaPlaylist(Long idPlaylist,String idSpotify, String uri) {
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



    @Override
    public void crearNuevaPlaylistConCanciones(Comunidad comunidad, List<CancionDto> canciones) {

        Set<Cancion> cancionesLista = new HashSet<>();

        for (CancionDto cancionDto : canciones) {
            Cancion cancion = repositorioCancion.buscarCancionPorElIdDeSpotify(cancionDto.getSpotifyId());

            if (cancion == null) {
                cancion = new Cancion();
                cancion.setSpotifyId(cancionDto.getSpotifyId());
                cancion.setUri(cancionDto.getUri());
                cancion.setTitulo(cancionDto.getTitulo());
                cancion.setArtista(cancionDto.getArtista());
                cancion.setUrlImagen(cancionDto.getUrlImagen());

                repositorioCancion.guardarCancion(cancion);
            }

            cancionesLista.add(cancion);
        }

        repositorioPlaylist.crearNuevaPlaylistConCanciones(comunidad, cancionesLista);

    }

}
