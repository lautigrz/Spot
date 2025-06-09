package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.ServicioFavorito;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import javax.servlet.http.HttpSession;

@Controller
public class ControladorArtista {
    private final ServicioFavorito servicioFavorito;
    private final SpotifyApi spotifyApi;
    private final RepositorioUsuario repositorioUsuario;


    public ControladorArtista(ServicioFavorito servicioFavorito, SpotifyApi spotifyApi, RepositorioUsuario repositorioUsuario) {
        this.servicioFavorito = servicioFavorito;
        this.spotifyApi = spotifyApi;
        this.repositorioUsuario = repositorioUsuario;
    }

    @GetMapping("/artistas/{id}")
    public String verArtista(@PathVariable String id, Model model, HttpSession session) {
        try{

            Artist artist = spotifyApi.getArtist(id).build().execute();

            model.addAttribute("artista", artist);

            Usuario usuario = (Usuario) session.getAttribute("usuario");
            boolean esFavorito = usuario!= null && servicioFavorito.yaEsFavorito(id,usuario);
            model.addAttribute("esFavorito", esFavorito);

            return "detalle-artista";
        } catch(Exception e){
            return "error";
        }
    }

    @PostMapping("/artistas/{id}/favorito")
    public String agregarAFavoritos(@PathVariable String id, HttpSession session) {
        Object usuarioIdObj = session.getAttribute("user");

        if (usuarioIdObj != null) {
            Long usuarioId = Long.valueOf(usuarioIdObj.toString());
            Usuario usuario = repositorioUsuario.buscarUsuarioPorId(usuarioId);
            servicioFavorito.agregarFavorito(id,usuario);
        }
        return "redirect:/perfil";

    }
}
