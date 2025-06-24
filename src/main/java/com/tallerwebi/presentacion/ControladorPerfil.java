package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.ServicioFavorito;
import com.tallerwebi.dominio.ServicioPerfil;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se.michaelthelin.spotify.model_objects.specification.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ControladorPerfil {


    private ServicioPerfil servicioPerfil;
    private ServicioEstadoDeAnimo servicioEstadoDeAnimo;
    private ServicioRecomendaciones servicioRecomendaciones;

    private ServicioFavorito servicioFavorito;
    private ServicioPreescucha servicioPreescucha;
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ControladorPerfil(ServicioPerfil servicioPerfil, ServicioEstadoDeAnimo servicioEstadoDeAnimo, ServicioRecomendaciones servicioRecomendaciones) {
        this.servicioPerfil = servicioPerfil;
        this.servicioEstadoDeAnimo = servicioEstadoDeAnimo;
        this.servicioRecomendaciones = servicioRecomendaciones;
    }

    @Autowired
    public void setServicioFavorito(ServicioFavorito servicioFavorito) {
        this.servicioFavorito = servicioFavorito;
    }

    @Autowired
    public void setServicioPreescucha(ServicioPreescucha servicioPreescucha) {
        this.servicioPreescucha = servicioPreescucha;
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) throws Exception {
        String token = (String) session.getAttribute("token");
        String refreshToken = (String) session.getAttribute("refreshToken");
        Long usuarioId = (Long) session.getAttribute("user");
        Usuario usuario = repositorioUsuario.buscarUsuarioPorId(usuarioId);




        try {
            User user = servicioPerfil.obtenerPerfilUsuario(token);

            model.addAttribute("inicio", "Se inicio correctamente");
            model.addAttribute("nombre",user.getDisplayName());
            model.addAttribute("foto", user.getImages()[0].getUrl());
            model.addAttribute("seguidos", servicioPerfil.obtenerCantidadDeArtistaQueSigueElUsuario(token));
            model.addAttribute("mejores", servicioPerfil.obtenerMejoresArtistasDelUsuario(token));
            model.addAttribute("playlist", servicioPerfil.obtenerNombreDePlaylistDelUsuario(token));
            model.addAttribute("totalPlaylist", servicioPerfil.obtenerCantidadDePlaylistDelUsuario(token));
            model.addAttribute("escuchando", servicioPerfil.obtenerReproduccionActualDelUsuario(token));
            // DEVUELVE UN NULO NOSE QUE ONDA model.addAttribute("artista", servicioPerfil.obtenerReproduccionActualDelUsuario(token, refreshToken).getArtists()[0].getName());
            model.addAttribute("listaDeEstadosDeAnimo", servicioEstadoDeAnimo.obtenerTodosLosEstadosDeAnimo());
            if (servicioPerfil.obtenerEstadoDeAnimoDelUsuario(token) != null){
                model.addAttribute("estadoDeAnimoActual", servicioPerfil.obtenerEstadoDeAnimoDelUsuario(token));
            }

           if (usuarioId != null) {
                model.addAttribute("favoritos", servicioFavorito.obtenerFavoritos(usuario));
            }

            if (!model.containsAttribute("recomendaciones")) {
                model.addAttribute("recomendaciones", new ArrayList<Track>());
            }

            List<String> albumsId = servicioPreescucha.obtenerAlbumesComprados(usuario);
            List<Album> albumesComprados = servicioPerfil.obtenerAlbumesDePreescuchaCompradosPorElUsuario(albumsId, token);
            model.addAttribute("albumesCompradosDetalle", albumesComprados);

        }catch (Exception e) {
            e.printStackTrace();

        }

        return "perfil";
    }

    @PostMapping("/actualizar-estado")
    public String actualizarEstadoDeAnimo(@RequestParam("estadoDeAnimoID") Long estadoDeAnimoID, HttpSession session, RedirectAttributes redirectAttributes) throws Exception{
        String token = (String) session.getAttribute("token");
        String refreshToken = (String) session.getAttribute("refreshToken");

        try{
            EstadoDeAnimo estado = servicioEstadoDeAnimo.obtenerEstadoDeAnimoPorId(estadoDeAnimoID);
            servicioPerfil.actualizarEstadoDeAnimoUsuario(token, estado);


        } catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/perfil";
    }

    @PostMapping("/generar-recomendaciones")
    public String generarRecomendaciones(HttpSession session, RedirectAttributes redirectAttributes) throws Exception{
        String token = (String) session.getAttribute("token");
        String refreshToken = (String) session.getAttribute("refreshToken");
        System.out.println("CHECK CONTROLLER");
        try{
            List<Track> recomendaciones = servicioRecomendaciones.generarRecomendaciones(token);
            redirectAttributes.addFlashAttribute("recomendaciones", recomendaciones);

        } catch (Exception e){
            e.printStackTrace();
        }

        return "redirect:/perfil";
    }


    @Autowired
    public void setRepositorioUsuario(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }
}
