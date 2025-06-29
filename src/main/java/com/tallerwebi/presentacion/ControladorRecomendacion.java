package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioAdmin;
import com.tallerwebi.dominio.ServicioRecomedacionComunidad;
import com.tallerwebi.presentacion.dto.CancionDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ControladorRecomendacion {


    private ServicioRecomedacionComunidad servicioRecomedacionComunidad;
    public ControladorRecomendacion(ServicioRecomedacionComunidad servicioRecomedacionComunidad) {

        this.servicioRecomedacionComunidad = servicioRecomedacionComunidad;
    }

    @PostMapping("/recomendar/{idComunidad}/{idUsuario}")
    @ResponseBody
    public String cancionesRecomendadas(@PathVariable Long idComunidad,
                                        @PathVariable Long idUsuario,
                                        @RequestBody List<CancionDto> cancionDto) {

        servicioRecomedacionComunidad.agregarRecomendacion(cancionDto,idUsuario, idComunidad);

        return "/spring/comunidad/" + idComunidad;
    }

}
