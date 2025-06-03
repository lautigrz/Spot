package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioComunidad {
    void guardarMensajeDeLaComunidad(String mensaje, Long idUsuario, Long idComunidad);
    List<Mensaje> obtenerMensajesDeComunidad(Long id);
    void guardarNuevaComunidad(Comunidad comunidad);
    List<Comunidad> obtenerComunidades();
    Comunidad obtenerComunidad(Long id);
    String obtenerTokenDelUsuario(String user);

}
