package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.CancionDto;

import java.util.List;

public interface ServicioPlaylist {
    void agregarCancionALaPlaylist(Long idPlaylist,String idSpotify, String uri);
    void eliminarCancionALaPlaylist(Long idPlaylist, Long idCancion) throws Exception;
    List<CancionDto>  obtenerCancionesDeLaPlaylist(Long id);
    List<Playlist> obtenerPlaylistsRelacionadasAUnaComunidad(Long idComuniadad);
    void crearNuevaPlaylistConCanciones(Comunidad comunidad, List<CancionDto> canciones, String nombre, String urlImagen);
}
