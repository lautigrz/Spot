package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.ServicioFavorito;
import com.tallerwebi.dominio.ServicioPerfil;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import com.tallerwebi.presentacion.dto.CancionDto;
import com.tallerwebi.presentacion.dto.UsuarioDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se.michaelthelin.spotify.model_objects.specification.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
public class ControladorPerfil {


    private ServicioPerfil servicioPerfil;
    private ServicioEstadoDeAnimo servicioEstadoDeAnimo;
    private ServicioRecomendaciones servicioRecomendaciones;
    private ServicioUsuario servicioUsuario;
    private ServicioFavorito servicioFavorito;
    private ServicioPreescucha servicioPreescucha;
    private ServicioReproduccion servicioReproduccion;
    private ServicioLike servicioLike;
    @Autowired
    public ControladorPerfil(ServicioPerfil servicioPerfil, ServicioEstadoDeAnimo servicioEstadoDeAnimo, ServicioRecomendaciones servicioRecomendaciones, ServicioUsuario servicioUsuario, ServicioReproduccion servicioReproduccion, ServicioLike servicioLike) {
        this.servicioPerfil = servicioPerfil;
        this.servicioEstadoDeAnimo = servicioEstadoDeAnimo;
        this.servicioReproduccion = servicioReproduccion;
        this.servicioRecomendaciones = servicioRecomendaciones;
        this.servicioUsuario = servicioUsuario;
        this.servicioLike = servicioLike;
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
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(usuarioId);




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
            model.addAttribute("listaDeEstadosDeAnimo", servicioEstadoDeAnimo.obtenerTodosLosEstadosDeAnimo());

            EstadoDeAnimo estado = servicioPerfil.obtenerEstadoDeAnimoDelUsuario(token);
            System.out.println("Estado desde servicioPerfil: " + (estado != null ? estado.getNombre() : "null"));

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

            Set<UsuarioDto> seguidos = servicioUsuario.obtenerSeguidos(usuarioId);
            Set<UsuarioDto> seguidores = servicioUsuario.obtenerSeguidores(usuarioId);

            if (seguidos == null) seguidos = Collections.emptySet();
            if (seguidores == null) seguidores = Collections.emptySet();

            model.addAttribute("misSeguidos", seguidos);
            model.addAttribute("misSeguidores", seguidores);

        }catch (Exception e) {
            e.printStackTrace();

        }

        return "perfil";
    }

    @PostMapping("/actualizar-estado")
    public String actualizarEstadoDeAnimo(@RequestParam("estadoDeAnimoId") Long estadoDeAnimoID, HttpSession session, RedirectAttributes redirectAttributes) throws Exception{
        String token = (String) session.getAttribute("token");
        String refreshToken = (String) session.getAttribute("refreshToken");

        try{
            EstadoDeAnimo estado = servicioEstadoDeAnimo.obtenerEstadoDeAnimoPorId(estadoDeAnimoID);
            System.out.println("Id del estado de ánimo recibido: " + estadoDeAnimoID);
            servicioPerfil.actualizarEstadoDeAnimoUsuario(token, estado);


        } catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/perfil";
    }

    @PostMapping("/generar-recomendaciones")
    public String generarRecomendaciones(HttpSession session, RedirectAttributes redirectAttributes) throws Exception{
        String token = (String) session.getAttribute("token");
        EstadoDeAnimo estadoDeAnimo = servicioPerfil.obtenerEstadoDeAnimoDelUsuario(token);
        System.out.println("CHECK CONTROLLER");
        try{
            List<Track> recomendaciones = servicioRecomendaciones.generarRecomendaciones(token, estadoDeAnimo);
            System.out.println("Cantidad de recomendaciones filtradas: " + recomendaciones.size());
            redirectAttributes.addFlashAttribute("recomendaciones", recomendaciones);
        } catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/perfil";
    }


    @GetMapping("/perfil/{id}")
    public String perfilUsuario(@PathVariable Long id, HttpSession session, Model model) throws Exception {
        String token = (String) session.getAttribute("token");
        Long usuarioLogueadoId = (Long) session.getAttribute("user");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(id);
        try {
            User user = servicioPerfil.obtenerPerfilUsuario(token);

            model.addAttribute("nombre", usuario.getUser());
            model.addAttribute("foto", usuario.getUrlFoto());
            model.addAttribute("seguidos", servicioUsuario.obtenerSeguidos(id).size());
            model.addAttribute("seguidores", servicioUsuario.obtenerSeguidores(id).size());
            model.addAttribute("misSeguidos", servicioUsuario.obtenerSeguidos(id));
            model.addAttribute("misSeguidores", servicioUsuario.obtenerSeguidores(id));

            model.addAttribute("usuarioIdPerfil", id);
            model.addAttribute("usuarioLogueadoId", usuarioLogueadoId);
            boolean yaLoSigo = servicioUsuario.yaSigo(usuarioLogueadoId, id);
            model.addAttribute("yaLoSigo", yaLoSigo);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "perfil";
    }

    @PostMapping("/seguir/{id}")
    public String seguirUsuario(@PathVariable Long id, HttpSession session) throws Exception {
        Long idLogueado = (Long) session.getAttribute("user");
        servicioUsuario.seguirUsuario(idLogueado, id);
        return "redirect:/perfil/" + id;
    }

    @PostMapping("/dejar-de-seguir/{id}")
    public String dejarDeSeguirUsuario(@PathVariable Long id, HttpSession session) throws Exception {
        Long idLogueado = (Long) session.getAttribute("user");
        servicioUsuario.dejarDeSeguirUsuario(idLogueado, id);
        return "redirect:/perfil/" + id;
    }
    @GetMapping("/perfilMejorado")
    public String perfilMejorado(HttpSession session, Model model) throws Exception {
        String token = (String) session.getAttribute("token");
        String refreshToken = (String) session.getAttribute("refreshToken");
        Long usuarioId = (Long) session.getAttribute("user");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(usuarioId);


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
            model.addAttribute("listaDeEstadosDeAnimo", servicioEstadoDeAnimo.obtenerTodosLosEstadosDeAnimo());
            model.addAttribute("usuarioId", usuarioId);
            model.addAttribute("post",servicioLike.obtenerPostConLikeDeUsuario(usuarioId));
            EstadoDeAnimo estado = servicioPerfil.obtenerEstadoDeAnimoDelUsuario(token);
            System.out.println("Estado desde servicioPerfil: " + (estado != null ? estado.getNombre() : "null"));

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

            Set<UsuarioDto> seguidos = servicioUsuario.obtenerSeguidos(usuarioId);
            Set<UsuarioDto> seguidores = servicioUsuario.obtenerSeguidores(usuarioId);

            if (seguidos == null) seguidos = Collections.emptySet();
            if (seguidores == null) seguidores = Collections.emptySet();

            model.addAttribute("misSeguidos", seguidos);
            model.addAttribute("misSeguidores", seguidores);

        }catch (Exception e) {
            e.printStackTrace();

        }

        return "perfil-mejorado";
    }

    @GetMapping("/escuchando/{idUsuario}")
    @ResponseBody
    public CancionDto escuchando(@PathVariable Long idUsuario) throws Exception {
        CancionDto cancionDto = servicioReproduccion.obtenerCancionActualDeUsuario(idUsuario);
        return cancionDto;
    }


}
