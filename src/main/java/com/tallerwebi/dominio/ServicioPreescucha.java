package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioPreescucha {
    boolean yaComproPreescucha(String albumId, Usuario usuario);
    void comprarPreescucha(String albumId, Usuario usuario);
    List<String> obtenerAlbumesComprados(Usuario usuario);
    void comprarPreescuchaLocal(int preescuchaId, Usuario usuario);
    void crearPreescuchaLocal(Double precio, String titulo, String preescuchaFotoUrl, String rutaAudio, Artista artista);
    boolean yaComproPreescuchaLocal(int preescuchaId, Usuario usuario);
    List<Preescucha> obtenerPreescuchasCompradasLocalmente(Usuario usuario);
}