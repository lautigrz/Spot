package com.tallerwebi.dominio;

import java.time.LocalDateTime;
import java.util.List;

public interface ServicioPreescucha {
    boolean yaComproPreescucha(String albumId, Usuario usuario);
    void comprarPreescucha(String albumId, Usuario usuario);
    List<String> obtenerAlbumesComprados(Usuario usuario);
    void comprarPreescuchaLocal(int preescuchaId, Usuario usuario);
    Long crearPreescuchaLocal(Preescucha preescucha);
    boolean yaComproPreescuchaLocal(int preescuchaId, Usuario usuario);
    List<Preescucha> obtenerPreescuchasCompradasLocalmente(Usuario usuario);
    Preescucha obtenerPreescuchaLocal(Long id);
    List<Preescucha> obtenerPreescuchasPorArtista(Long idArtista);
}