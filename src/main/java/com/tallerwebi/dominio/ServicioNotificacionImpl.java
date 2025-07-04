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
    private ServicioComunidad servicioComunidad;
    private ServicioRecomedacionComunidad servicioRecomedacionComunidad;
    public ServicioNotificacionImpl(RepositorioNotificacion repositorioNotificacion,
                                    ServicioUsuario servicioUsuario,
                                    ServicioRecomedacionComunidad servicioRecomedacionComunidad, ServicioComunidad servicioComunidad) {
        this.repositorioNotificacion = repositorioNotificacion;
        this.servicioUsuario = servicioUsuario;
        this.servicioComunidad = servicioComunidad;
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
    public void generarNotificacionSobreRecomendacion(Long idUsuario, Long idRecomendacion, Boolean estado) {
        String mensaje = "";
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        Recomendacion recomendacion = servicioRecomedacionComunidad.obtenerRecomendacionPorId(idRecomendacion);

        if(estado){
           mensaje = "Tu recomendación de la cancion <strong>" + recomendacion.getCancion().getTitulo() + "</strong> ha sido aceptada.";
        }else{
            mensaje = "Tu recomendación de la cancion <strong>" + recomendacion.getCancion().getTitulo() + "</strong> no ha sido aceptada.";
        }

        repositorioNotificacion.guardarNotificacion(mensaje, usuario);
    }

    @Override
    public void cambiarEstadoNotificacion(List<Long> idsNotificacion) {
        this.repositorioNotificacion.cambiarEstadoNotificacion(idsNotificacion);
    }

    @Override
    public void generarNotificacionDeEliminacionDeUsuarioDeLaComunidad(Long idUsuario, Long idComunidad) {

        Comunidad comunidad = servicioComunidad.obtenerComunidad(idComunidad);
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);

        if(comunidad == null && usuario == null) {

            throw new IllegalArgumentException("Comunidad o usuario no encontrado");

        }

        String mensaje = "Fuiste eliminado de la comunidad <strong>" + comunidad.getNombre() + "</strong> por el administrador.";

        repositorioNotificacion.guardarNotificacion(mensaje, usuario);



    }

    @Override
    public Boolean elUsuarioTieneNotificaciones(Long idUsuario) {
    return this.repositorioNotificacion.elUsuarioTieneNotificaciones(idUsuario);

    }

}
