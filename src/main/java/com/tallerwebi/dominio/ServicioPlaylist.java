package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioPlaylist {
    void agregarCancionALaPlaylist(Long idPlaylist,Cancion cancion);
    void eliminarCancionALaPlaylist(Long id,Cancion cancion);
    List<Cancion> obtenerCancionesDeLaPlaylist(Long id);

}
