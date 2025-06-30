package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioNotificacion {
    List<Notificacion> obtenerNotificacionesPorUsuario(Long idUsuario);
    void guardarNotificacion(Notificacion notificacion, Usuario usuario, Recomendacion recomendacion);
    void cambiarEstadoNotificacion(Long idNotificacion);

}
