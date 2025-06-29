package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.CancionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public List<CancionDto> obtenerCancionesDeLaPlaylist(Long id) {
        List<Cancion> canciones = repositorioPlaylist.obtenerCancionesDeLaPlaylist(id);
        return canciones.stream()
                .map(c -> {
                    CancionDto dto = new CancionDto();
                    dto.setId(c.getId());
                    dto.setTitulo(c.getTitulo());
                    dto.setArtista(c.getArtista());
                    dto.setUrlImagen(c.getUrlImagen());
                    return dto;
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<Playlist> obtenerPlaylistsRelacionadasAUnaComunidad(Long idComuniadad) {
        return repositorioPlaylist.obtenerPlaylistsRelacionadasAUnaComunidad(idComuniadad);
    }


    @Override
    public void crearNuevaPlaylistConCanciones(Comunidad comunidad, List<CancionDto> canciones,String nombre, String urlImagen) {

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

        repositorioPlaylist.crearNuevaPlaylistConCanciones(comunidad, cancionesLista, nombre, urlImagen);

    }

}
