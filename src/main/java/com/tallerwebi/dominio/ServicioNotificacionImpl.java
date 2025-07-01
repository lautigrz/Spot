package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.NotificacionDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ServicioNotificacionImpl implements ServicioNotificacion {

    private RepositorioNotificacion repositorioNotificacion;
    private ServicioUsuario servicioUsuario;
    private ServicioRecomedacionComunidad servicioRecomedacionComunidad;
    public ServicioNotificacionImpl(RepositorioNotificacion repositorioNotificacion,
                                    ServicioUsuario servicioUsuario,
                                    ServicioRecomedacionComunidad servicioRecomedacionComunidad) {
        this.repositorioNotificacion = repositorioNotificacion;
        this.servicioUsuario = servicioUsuario;
        this.servicioRecomedacionComunidad = servicioRecomedacionComunidad;
    }


    @Override
    public List<NotificacionDto> obtenerNotificacionesPorUsuario(Long idUsuario) {

        List<NotificacionDto > notificaciones = new ArrayList<NotificacionDto>();

        for(Notificacion notificacion : repositorioNotificacion.obtenerNotificacionesPorUsuario(idUsuario)) {
            NotificacionDto notificacionDto = new NotificacionDto();
            notificacionDto.setId(notificacion.getId());
            notificacionDto.setMensaje(notificacion.getMensaje());
            notificacionDto.setLeido(notificacion.getLeido());
            notificacionDto.setNombreUsuario(notificacion.getUsuario().getUser());
            notificaciones.add(notificacionDto);
        }

        return notificaciones;
    }

    @Override
    public void generarNotificacion(Long idUsuario, Long idRecomendacion, Boolean estado) {
        String mensaje = "";
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        Recomendacion recomendacion = servicioRecomedacionComunidad.obtenerRecomendacionPorId(idRecomendacion);

        if(estado){
           mensaje = "Tu recomendación de la cancion '" + recomendacion.getCancion().getTitulo() + "' ha sido aceptada.";
        }else{
            mensaje = "Tu recomendación de la cancion '" + recomendacion.getCancion().getTitulo() + "' no ha sido aceptada.";
        }

        Notificacion notificacion = new Notificacion();
        notificacion.setLeido(false);
        notificacion.setMensaje(mensaje);
        notificacion.setRecomendacion(recomendacion);

        repositorioNotificacion.guardarNotificacion(notificacion, usuario, recomendacion);
    }

    @Override
    public void cambiarEstadoNotificacion(List<Long> idsNotificacion) {
        this.repositorioNotificacion.cambiarEstadoNotificacion(idsNotificacion);
    }

    @Override
    public Boolean elUsuarioTieneNotificaciones(Long idUsuario) {
    return this.repositorioNotificacion.elUsuarioTieneNotificaciones(idUsuario);

    }
}
