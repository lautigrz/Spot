package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.NotificacionDto;

import java.util.List;

public interface ServicioNotificacion {
    List<NotificacionDto> obtenerNotificacionesPorUsuario(Long idUsuario);
    void generarNotificacionSobreRecomendacion(Long idUsuario, Long idRecomendacion, Boolean estado);
    void cambiarEstadoNotificacion(List<Long> idsNotificaciones);
    void generarNotificacionDeEliminacionDeUsuarioDeLaComunidad(Long idUsuario, Long idComunidad);
    void generarNotificacionDeMensajeEliminacionDeUsuarioDeLaComunidad(Long idUsuario, Long idComunidad);
    void generarNotificacionParaNuevoAdmin(Long idUsuario, Long idComunidad);
    Boolean elUsuarioTieneNotificaciones(Long idUsuario);

}
