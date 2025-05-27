package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioComunidad {
    void guardarMensajeDeLaComunidad(String mensaje, Long idUsuario);
    List<Mensaje> obtenerMensajesDeComunidad();

}
