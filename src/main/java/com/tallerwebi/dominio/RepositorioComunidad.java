package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioComunidad {
    void guardarMensajeDeLaComunidad(String mensaje, Long idUsuario);
    List<Mensaje> obtenerMensajesDeComunidad();
    void guardarNuevaComunidad(Comunidad comunidad);
    List<Comunidad> obtenerComunidades();
    Comunidad obtenerComunidad(Long id);
    String obtenerTokenDelUsuario(String user);

}
