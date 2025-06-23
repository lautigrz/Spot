package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioPreescucha {
    boolean yaComproPreescucha(String albumId, Usuario usuario);
    void comprarPreescucha(String albumId, Usuario usuario);
    List<String> obtenerAlbumesComprados(Usuario usuario);
}
