package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.CancionDto;

import java.util.List;

public interface ServicioPlaylist {
    void agregarCancionALaPlaylist(Long idPlaylist,String idSpotify, String uri);
    void eliminarCancionALaPlaylist(Long idPlaylist, Long idCancion) throws Exception;
    List<Cancion> obtenerCancionesDeLaPlaylist(Long id);
    void crearNuevaPlaylistConCanciones(Comunidad comunidad, List<CancionDto> canciones);
}
