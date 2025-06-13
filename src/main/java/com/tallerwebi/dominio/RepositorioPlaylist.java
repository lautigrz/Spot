package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.CancionDto;

import java.util.List;
import java.util.Set;

public interface RepositorioPlaylist {
    void agregarPlaylist(Playlist playlist, List<Cancion> canciones);
    void agregarCancionALaPlaylist(Long id,Cancion cancion);
    //falta test
    void eliminarCancionALaPlaylist(Long id,Cancion cancion);
    List<Playlist> obtenerPlaylistsRelacionadasAUnaComunidad(Long idComuniadad);
    List<Cancion> obtenerCancionesDeLaPlaylist(Long id);
    Playlist obtenerPlaylist(Long idPlaylist);
    void crearNuevaPlaylistConCanciones(Comunidad comunidad, Set<Cancion> canciones,String nombre, String urlImagen);
}
