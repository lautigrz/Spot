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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;


import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Controller
public class ControladorRating {

    private ServicioSpotify servicioSpotify;
    private ServicioRating servicioRating;
    private ServicioUsuario servicioUsuario;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioCancion repositorioCancion;

    @Autowired
    public ControladorRating(ServicioRating servicioRating, RepositorioUsuario repositorioUsuario, ServicioSpotify servicioSpotify, ServicioUsuario servicioUsuario, RepositorioCancion repositorioCancion) {
        this.servicioRating = servicioRating;
        this.repositorioUsuario = repositorioUsuario;
        this.servicioSpotify = servicioSpotify;
        this.servicioUsuario = servicioUsuario;
        this.repositorioCancion = repositorioCancion;
    }

    @GetMapping("/cancion/ratear")
    public String mostrarFormularioRating(){
        return "ratear-cancion";
    }

    @PostMapping("/cancion/ratear")
    public String guardarRating(
            @RequestParam String spotifyId,
            @RequestParam String titulo,
            @RequestParam String artista,
            @RequestParam String urlImagen,
            @RequestParam String uri,
            @RequestParam Integer puntaje,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) throws Exception {
        String token = (String) session.getAttribute("token");
        try {
            servicioRating.guardarRating(
                    token,
                    spotifyId,
                    titulo,
                    artista,
                    urlImagen,
                    uri,
                    puntaje
            );
            redirectAttributes.addFlashAttribute("exito", "¡Canción calificada!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
            return "redirect:/cancion/ratear";
        }

        return "redirect:/perfilMejorado";
    }


    @GetMapping("/cancion/buscar")
    public ResponseEntity<List<CancionDto>> buscarCanciones(@RequestParam String texto, HttpSession session) throws Exception, IOException, ParseException, SpotifyWebApiException {
        String token = (String) session.getAttribute("token");
        List<CancionDto> canciones = servicioSpotify.obtenerCancionesDeSpotify(token, texto);

        return ResponseEntity.ok(canciones);
    }


}
