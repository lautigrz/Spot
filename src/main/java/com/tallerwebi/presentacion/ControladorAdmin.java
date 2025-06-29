package com.tallerwebi.presentacion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallerwebi.dominio.*;
import com.tallerwebi.presentacion.dto.CancionDto;
import com.tallerwebi.presentacion.dto.RecomendacionDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ControladorAdmin {

    private ServicioAdmin servicioAdmin;
    private ServicioRecomedacionComunidad servicioRecomedacionComunidad;
    private ServicioPlaylist servicioPlaylist;
    private ServicioEvento servicioEvento;
    public ControladorAdmin(ServicioAdmin servicioAdmin, ServicioRecomedacionComunidad servicioRecomedacionComunidad,
                            ServicioPlaylist servicioPlaylist, ServicioEvento servicioEvento) {
        this.servicioAdmin = servicioAdmin;
        this.servicioEvento = servicioEvento;
        this.servicioPlaylist = servicioPlaylist;
        this.servicioRecomedacionComunidad = servicioRecomedacionComunidad;
    }

    @PostMapping("/eliminarMiembro/{idComunidad}/{idMiembro}")
    public String eliminarMiembroDeComunidad(@PathVariable Long idComunidad,@PathVariable Long idMiembro) {
        servicioAdmin.eliminarMiembroDeComunidad(idComunidad, idMiembro);
        return "redirect:/comunidad/" + idComunidad;
    }

    @PostMapping("/hacerAdmin/{idComunidad}/{idMiembro}")
    public String hacerAdminAUnMiembro(@PathVariable Long idComunidad, @PathVariable Long idMiembro) {
        servicioAdmin.hacerAdminAUnMiembro(idComunidad, idMiembro);
        return "redirect:/comunidad/" + idComunidad;
    }

    @PostMapping("/eliminar-reco/{idComunidad}/{idRecomendacion}")
    @ResponseBody
    public Map<String, Object> eliminarRecomendacion(@PathVariable Long idRecomendacion, @PathVariable Long idComunidad) {
        servicioRecomedacionComunidad.eliminarRecomendacion(idRecomendacion);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("mensaje", "Recomendación eliminada");
        response.put("idRecomendacion", idRecomendacion);
        return response;
    }
    @PostMapping("/aceptar-reco/{idComunidad}/{idRecomendacion}")
    @ResponseBody
    public Map<String, Object> aceptarRecomendacion(@PathVariable Long idComunidad, @PathVariable Long idRecomendacion) {
        Recomendacion recomendacion = servicioRecomedacionComunidad.aceptarRecomendacion(idRecomendacion);

        Long idPlaylist = servicioPlaylist
                .obtenerPlaylistsRelacionadasAUnaComunidad(idComunidad)
                .get(0)
                .getId();

        servicioPlaylist.agregarCancionALaPlaylist(
                idPlaylist,
                recomendacion.getCancion().getSpotifyId(),
                recomendacion.getCancion().getUri()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("idRecomendacion", idRecomendacion);
        return response;
    }

    @GetMapping("/crear-evento/{idComunidad}")
    public String crearEvento(@PathVariable Long idComunidad, Model model) {
        model.addAttribute("evento", new Evento());
        model.addAttribute("id", idComunidad);
        return "crear-evento";
    }

    @PostMapping("/crear-evento/{idComunidad}")
    public String crearEventoComunidad(@PathVariable Long idComunidad,@ModelAttribute Evento evento) {

        servicioEvento.publicarEvento(evento, idComunidad);

        return "redirect:/comunidad/" + idComunidad;
    }





}
