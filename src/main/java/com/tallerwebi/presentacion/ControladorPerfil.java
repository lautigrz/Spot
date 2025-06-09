package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.ServicioFavorito;
import com.tallerwebi.dominio.ServicioPerfil;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import se.michaelthelin.spotify.model_objects.specification.User;

import javax.servlet.http.HttpSession;

@Controller
public class ControladorPerfil {


    private ServicioPerfil servicioPerfil;
    private ServicioFavorito servicioFavorito;
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ControladorPerfil(ServicioPerfil servicioPerfil) {
        this.servicioPerfil = servicioPerfil;
    }

    @Autowired
    public void setServicioFavorito(ServicioFavorito servicioFavorito) {
        this.servicioFavorito = servicioFavorito;
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) throws Exception {
        String token = (String) session.getAttribute("token");
        String refreshToken = (String) session.getAttribute("refreshToken");
        Long usuarioId = (Long) session.getAttribute("user");




        try {
            User user = servicioPerfil.obtenerPerfilUsuario(token, refreshToken);

            model.addAttribute("inicio", "Se inicio correctamente");
            model.addAttribute("nombre",user.getDisplayName());
            model.addAttribute("foto", user.getImages()[0].getUrl());
            model.addAttribute("seguidos", servicioPerfil.obtenerCantidadDeArtistaQueSigueElUsuario(token, refreshToken));
            model.addAttribute("mejores", servicioPerfil.obtenerMejoresArtistasDelUsuario(token, refreshToken));
            model.addAttribute("playlist", servicioPerfil.obtenerNombreDePlaylistDelUsuario(token, refreshToken));
            model.addAttribute("totalPlaylist", servicioPerfil.obtenerCantidadDePlaylistDelUsuario(token, refreshToken));
            model.addAttribute("escuchando", servicioPerfil.obtenerReproduccionActualDelUsuario(token, refreshToken));
            model.addAttribute("artista", servicioPerfil.obtenerReproduccionActualDelUsuario(token, refreshToken).getArtists()[0].getName());


           if (usuarioId != null) {
                Usuario usuario = repositorioUsuario.buscarUsuarioPorId(usuarioId);
                model.addAttribute("favoritos", servicioFavorito.obtenerFavoritos(usuario));
            }

        }catch (Exception e) {
            e.printStackTrace();

        }

        return "perfil";

    }


    @Autowired
    public void setRepositorioUsuario(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }
}
