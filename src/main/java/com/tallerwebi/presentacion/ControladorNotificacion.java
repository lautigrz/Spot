package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Notificacion;
import com.tallerwebi.dominio.ServicioNotificacion;
import com.tallerwebi.presentacion.dto.NotificacionDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ControladorNotificacion {
    private final ServicioNotificacion servicioNotificacion;

    public ControladorNotificacion(ServicioNotificacion servicioNotificacion) {
        this.servicioNotificacion = servicioNotificacion;
    }

    @GetMapping("/notificaciones/{idUsuario}")
    @ResponseBody
    public List<NotificacionDto> obtenerNotificacionesPorUsuario(@PathVariable Long idUsuario) {
        return servicioNotificacion.obtenerNotificacionesPorUsuario(idUsuario);
    }


    @GetMapping("/notificacion/leido/{idNotificacion}")
    public void cambiarEstadoNotificacion(@PathVariable Long idNotificacion) {
        servicioNotificacion.cambiarEstadoNotificacion(idNotificacion);
    }
}
