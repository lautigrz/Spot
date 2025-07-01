package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.NotificacionDto;

import java.util.List;

public interface ServicioNotificacion {
    List<NotificacionDto> obtenerNotificacionesPorUsuario(Long idUsuario);
    void generarNotificacion(Long idUsuario, Long idRecomendacion, Boolean estado);
    void cambiarEstadoNotificacion(List<Long> idsNotificaciones);
    Boolean elUsuarioTieneNotificaciones(Long idUsuario);
}
