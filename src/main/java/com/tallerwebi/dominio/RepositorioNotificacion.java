package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioNotificacion {
    List<Notificacion> obtenerNotificacionesPorUsuario(Long idUsuario);
    void guardarNotificacion(String mensaje, Usuario usuario);
    void cambiarEstadoNotificacion(List<Long> idsNotificaciones);
    Boolean elUsuarioTieneNotificaciones(Long idUsuario);
}
