package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioPreescucha {
    boolean yaComproPreescucha(String albumId, Usuario usuario);
    void comprarPreescucha(String albumId, Usuario usuario);
    List<String> obtenerAlbumesComprados(Usuario usuario);
    void guardarPreescuchaLocal(Preescucha preescucha);
    boolean yaComproPreescuchaLocal(int preescuchaId, Usuario usuario);
    void comprarPreescuchaLocal(int preescuchaId, Usuario usuario);
}
