package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Notificacion;
import com.tallerwebi.dominio.ServicioNotificacion;
import com.tallerwebi.presentacion.dto.NotificacionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/notificacion/leer")
    @ResponseBody
    public ResponseEntity<Void> cambiarEstadoNotificacion(@RequestBody List<Long> idNotificaciones) {

        servicioNotificacion.cambiarEstadoNotificacion(idNotificaciones);
        return ResponseEntity.ok().build();
    }
}
