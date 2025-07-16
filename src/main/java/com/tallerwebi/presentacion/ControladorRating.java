package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.presentacion.dto.CancionDto;
import com.tallerwebi.dominio.ServicioSpotify;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;


import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Controller
public class ControladorRating {

    private ServicioSpotify servicioSpotify;
    private ServicioRating servicioRating;
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ControladorRating(ServicioRating servicioRating, RepositorioUsuario repositorioUsuario, ServicioSpotify servicioSpotify) {
        this.servicioRating = servicioRating;
        this.repositorioUsuario = repositorioUsuario;
        this.servicioSpotify = servicioSpotify;
    }

    @GetMapping("/cancion/ratear")
    public String mostrarFormularioRating(){
        return "ratear-cancion";
    }

    @PostMapping("/cancion/ratear")
    public String guardarRating(@RequestParam String spotifyId,
                                @RequestParam Integer puntaje,
                                HttpSession session,
                                Model model) throws Exception {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        try {
            servicioRating.guardarRating(usuario.getId(), spotifyId, puntaje);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "ratear-cancion";
        }

        return "redirect:/perfil";
    }

    @GetMapping("/cancion/buscar")
    public ResponseEntity<List<CancionDto>> buscarCanciones(@RequestParam String texto, HttpSession session) throws Exception, IOException, ParseException, SpotifyWebApiException {
        String token = (String) session.getAttribute("token");
        List<CancionDto> canciones = servicioSpotify.obtenerCancionesDeSpotify(token, texto);

        return ResponseEntity.ok(canciones);
    }


}
