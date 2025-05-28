package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioPlaylist {
    void agregarPlaylist(Playlist playlist, List<Cancion> canciones);
    void agregarCancionALaPlaylist(Long id,Cancion cancion);
    void eliminarCancionALaPlaylist(Long id,Cancion cancion);
    List<Cancion> obtenerCancionesDeLaPlaylist(Long id);
    Playlist obtenerPlaylist(Long idPlaylist);
}
