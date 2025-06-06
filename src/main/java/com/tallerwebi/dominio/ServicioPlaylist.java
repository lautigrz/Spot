package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioPlaylist {
    void agregarCancionALaPlaylist(Long idPlaylist,Long idSpotify, String uri);
    void eliminarCancionALaPlaylist(Long idPlaylist, Long idCancion) throws Exception;
    List<Cancion> obtenerCancionesDeLaPlaylist(Long id);

}
