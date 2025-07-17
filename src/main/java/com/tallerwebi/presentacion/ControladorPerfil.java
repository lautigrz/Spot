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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se.michaelthelin.spotify.model_objects.specification.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
    private ServicioPosteo servicioPosteo;
    @Autowired
    private ServicioArtista servicioArtista;

    private ServicioRating servicioRating;

    @Autowired
    private ServicioGuardarImagen servicioGuardarImagen;

    @Autowired
    public ControladorPerfil(ServicioPerfil servicioPerfil, ServicioEstadoDeAnimo servicioEstadoDeAnimo, ServicioRecomendaciones servicioRecomendaciones, ServicioUsuario servicioUsuario, ServicioReproduccion servicioReproduccion, ServicioLike servicioLike,ServicioRating servicioRating ) {
        this.servicioPerfil = servicioPerfil;
        this.servicioEstadoDeAnimo = servicioEstadoDeAnimo;
        this.servicioReproduccion = servicioReproduccion;
        this.servicioRecomendaciones = servicioRecomendaciones;
        this.servicioUsuario = servicioUsuario;
        this.servicioLike = servicioLike;
        this.servicioRating = servicioRating;
    }

    @Autowired
    public void setServicioFavorito(ServicioFavorito servicioFavorito) {
        this.servicioFavorito = servicioFavorito;
    }

    @Autowired
    public void setServicioPreescucha(ServicioPreescucha servicioPreescucha) {
        this.servicioPreescucha = servicioPreescucha;
    }

    @GetMapping("/perfil-l")
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

            List<Preescucha> preescuchasLocales = servicioPreescucha.obtenerPreescuchasCompradasLocalmente(usuario);
            model.addAttribute("preescuchasLocales", preescuchasLocales);

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


    @GetMapping("/perfil/artista")
    public ModelAndView perfilArtista(HttpSession session, ModelMap model) throws Exception {

        Artista artista = (Artista) session.getAttribute("artista");
        if (artista == null) {
            return new ModelAndView("redirect:/login");
        }

        model.put("habilitar", true);
        model.put("usuario", null);
        model.put("artista", artista);
        model.put("foto", artista.getFotoPerfil());
        model.put("nombre", artista.getNombre());
        model.put("posteos", servicioPosteo.obtenerPosteosDeArtista(artista));
        model.put("preescuchas", servicioPreescucha.obtenerPreescuchasPorArtista(artista.getId()));
        model.addAttribute("portada", artista.getPortada());
        return new ModelAndView("perfil-mejorado", model);
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


    @GetMapping("/perfidsfl/{id}")
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

        return "perfil-mejorado";
    }



    @GetMapping("/perfil/artista/{id}")
    public String perfilArtista(@PathVariable Long id, HttpSession session, Model model) throws Exception {
        String token = (String) session.getAttribute("token");
        Artista artista1 = servicioArtista.buscarPorId(id);
        Long idUsuario = (Long) session.getAttribute("user");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        boolean esFavorito = servicioFavorito.yaEsFavorito("LOCAL_" + id,usuario);

        try {
            model.addAttribute("habilitar", true);
            model.addAttribute("usuario", true);
            model.addAttribute("artistaId", artista1.getId());
            model.addAttribute("nombre", artista1.getNombre());
            model.addAttribute("foto", artista1.getFotoPerfil());
            model.addAttribute("posteos", servicioPosteo.obtenerPosteosDeArtista(artista1));
            model.addAttribute("preescuchas", servicioPreescucha.obtenerPreescuchasPorArtista(artista1.getId()));
            model.addAttribute("esFavorito", esFavorito);
            model.addAttribute("portada", artista1.getPortada());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "perfil-mejorado";
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
    @GetMapping("/perfil")
    public String perfilMejorado(HttpSession session, Model model) throws Exception {
        String token = (String) session.getAttribute("token");
        String refreshToken = (String) session.getAttribute("refreshToken");
        Long usuarioId = (Long) session.getAttribute("user");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(usuarioId);


        try {
            User user = servicioPerfil.obtenerPerfilUsuario(token);
            model.addAttribute("usuarioLogueado", true);
            model.addAttribute("inicio", "Se inicio correctamente");
            model.addAttribute("nombre",user.getDisplayName());
            model.addAttribute("portada", usuario.getUrlPortada());
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
            String spotifyId = user.getId();
            System.out.println("Usuario encontrado: " + usuario.getId() + " - " + usuario.getSpotifyID());
            List<Rating> ratings = servicioRating.obtenerRating(spotifyId);
            System.out.println("Número de ratings encontrados: " + ratings.size()); // Log Y
            model.addAttribute("ratings", ratings);

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


    @GetMapping("/perfil/{id}")
    public String perfilMejoradoPorId(@PathVariable Long id, HttpSession session, Model model) throws Exception {

        Long usuarioId = (Long) session.getAttribute("user");

        Boolean esUsuarioLogueado = usuarioId != null && usuarioId.equals(id);

        Usuario usuarioPerfil = servicioUsuario.obtenerUsuarioPorId(id);
        try {
            User user = servicioPerfil.obtenerPerfilUsuario(usuarioPerfil.getToken());
            model.addAttribute("usuarioLogueado", esUsuarioLogueado);
            model.addAttribute("inicio", "Se inicio correctamente");
            model.addAttribute("nombre",user.getDisplayName());
            model.addAttribute("portada", usuarioPerfil.getUrlPortada());
            model.addAttribute("foto", user.getImages()[0].getUrl());
            model.addAttribute("seguidos", servicioPerfil.obtenerCantidadDeArtistaQueSigueElUsuario(usuarioPerfil.getToken()));
            model.addAttribute("mejores", servicioPerfil.obtenerMejoresArtistasDelUsuario(usuarioPerfil.getToken()));
            model.addAttribute("playlist", servicioPerfil.obtenerNombreDePlaylistDelUsuario(usuarioPerfil.getToken()));
            model.addAttribute("totalPlaylist", servicioPerfil.obtenerCantidadDePlaylistDelUsuario(usuarioPerfil.getToken()));
            model.addAttribute("escuchando", servicioPerfil.obtenerReproduccionActualDelUsuario(usuarioPerfil.getToken()));
            model.addAttribute("listaDeEstadosDeAnimo", servicioEstadoDeAnimo.obtenerTodosLosEstadosDeAnimo());
            model.addAttribute("usuarioId", usuarioPerfil.getId());
            model.addAttribute("post",servicioLike.obtenerPostConLikeDeUsuario(usuarioPerfil.getId()));
            EstadoDeAnimo estado = servicioPerfil.obtenerEstadoDeAnimoDelUsuario(usuarioPerfil.getToken());
            System.out.println("Estado desde servicioPerfil: " + (estado != null ? estado.getNombre() : "null"));

            if (servicioPerfil.obtenerEstadoDeAnimoDelUsuario(usuarioPerfil.getToken()) != null){
                model.addAttribute("estadoDeAnimoActual", servicioPerfil.obtenerEstadoDeAnimoDelUsuario(usuarioPerfil.getToken()));
            }

            if (usuarioPerfil.getToken() != null) {
                model.addAttribute("favoritos", servicioFavorito.obtenerFavoritos(usuarioPerfil));
            }

            if (!model.containsAttribute("recomendaciones")) {
                model.addAttribute("recomendaciones", new ArrayList<Track>());
            }

            List<String> albumsId = servicioPreescucha.obtenerAlbumesComprados(usuarioPerfil);
            List<Album> albumesComprados = servicioPerfil.obtenerAlbumesDePreescuchaCompradosPorElUsuario(albumsId, usuarioPerfil.getToken());
            model.addAttribute("albumesCompradosDetalle", albumesComprados);

            Set<UsuarioDto> seguidos = servicioUsuario.obtenerSeguidos(usuarioPerfil.getId());
            Set<UsuarioDto> seguidores = servicioUsuario.obtenerSeguidores(usuarioPerfil.getId());

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
