package com.tallerwebi.dominio;

public interface RepositorioMensaje {
    Mensaje obtenerMensaje(Long id);
    void eliminarMensaje(Long idMensaje);
}
