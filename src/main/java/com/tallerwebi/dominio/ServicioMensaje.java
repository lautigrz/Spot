package com.tallerwebi.dominio;

public interface ServicioMensaje {
    Mensaje obtenerMensaje(Long id);
    void eliminarMensaje(Long idMensaje);
}
