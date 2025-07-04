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
    private ServicioUsuarioComunidad servicioUsuarioComunidad;
    private ServicioRecomedacionComunidad servicioRecomedacionComunidad;
    public ServicioNotificacionImpl(RepositorioNotificacion repositorioNotificacion,
                                    ServicioUsuarioComunidad servicioUsuarioComunidad,
                                    ServicioRecomedacionComunidad servicioRecomedacionComunidad, ServicioUsuario servicioUsuario) {
        this.repositorioNotificacion = repositorioNotificacion;
        this.servicioUsuario = servicioUsuario;
        this.servicioUsuarioComunidad = servicioUsuarioComunidad;
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

       UsuarioComunidad usuarioComunidad = servicioUsuarioComunidad.obtenerUsuarioEnComunidad(idUsuario,idComunidad);
        System.out.println("usuario:" + usuarioComunidad);
        if(usuarioComunidad == null) {

            throw new IllegalArgumentException("Comunidad o usuario no encontrado");

        }

        String mensaje = "Fuiste eliminado de la comunidad <strong>" + usuarioComunidad.getComunidad().getNombre() + "</strong> por el administrador.";

        repositorioNotificacion.guardarNotificacion(mensaje, usuarioComunidad.getUsuario());



    }

    @Override
    public void generarNotificacionDeMensajeEliminacionDeUsuarioDeLaComunidad(Long idUsuario, Long idComunidad) {
        UsuarioComunidad usuarioComunidad = servicioUsuarioComunidad.obtenerUsuarioEnComunidad(idUsuario,idComunidad);

        if(usuarioComunidad == null) {

            throw new IllegalArgumentException("Comunidad o usuario no encontrado");

        }

        String mensaje = "Su mensaje fue eliminado por un administrador de la comunidad <strong>" + usuarioComunidad.getComunidad().getNombre() + "</strong> por no cumplir las normas establecidas.";

        repositorioNotificacion.guardarNotificacion(mensaje, usuarioComunidad.getUsuario());
    }

    @Override
    public void generarNotificacionParaNuevoAdmin(Long idUsuario, Long idComunidad) {
        UsuarioComunidad usuarioComunidad = servicioUsuarioComunidad.obtenerUsuarioEnComunidad(idUsuario,idComunidad);

        if(usuarioComunidad == null) {

            throw new IllegalArgumentException("Comunidad o usuario no encontrado");

        }

        String mensaje = "Felicidades!, Ahora usted es nuevo administrador de la comunidad <strong>" + usuarioComunidad.getComunidad().getNombre() + ".";

        repositorioNotificacion.guardarNotificacion(mensaje, usuarioComunidad.getUsuario());

    }

    @Override
    public Boolean elUsuarioTieneNotificaciones(Long idUsuario) {
    return this.repositorioNotificacion.elUsuarioTieneNotificaciones(idUsuario);

    }

}
