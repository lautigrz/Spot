package com.tallerwebi.presentacion;


import com.tallerwebi.dominio.*;
import org.dom4j.rule.Mode;

import com.tallerwebi.dominio.ServicioComunidad;
import com.tallerwebi.dominio.ServicioInstancia;
import com.tallerwebi.dominio.ServicioNotificacion;
import com.tallerwebi.dominio.ServicioUsuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class ControladorHome {


    private RepositorioArtista repositorioArtista;
    private ServicioUsuario servicioUsuario;
    private ServicioComunidad servicioComunidad;
    private ServicioNotificacion servicioNotificacion;
    private ServicioInstancia spotify;
    private ServicioPosteo servicioPosteo;


    public ControladorHome(RepositorioArtista repositorioArtista,ServicioUsuario servicioUsuario, ServicioComunidad servicioComunidad, ServicioInstancia spotify, ServicioNotificacion servicioNotificacion,ServicioPosteo servicioPosteo) {
            this.repositorioArtista = repositorioArtista;
        this.servicioUsuario = servicioUsuario;
        this.servicioComunidad = servicioComunidad;
        this.servicioNotificacion = servicioNotificacion;
        this.spotify = spotify;
        this.servicioPosteo = servicioPosteo;
    }

    @GetMapping("/home")
    public ModelAndView vistaHome(HttpSession session) {
        ModelMap modelMap = new ModelMap();

        Long idUsuario = (Long) session.getAttribute("user");
        Object artistaObj = session.getAttribute("artista");

        if (idUsuario != null) {
            Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
            modelMap.put("usuario", usuario);

            List<Post> posteos = servicioPosteo.obtenerPosteosDeArtistasFavoritos(usuario);
            modelMap.put("posteos", posteos);
        } else if (artistaObj != null) {
            Artista artista = (Artista) artistaObj;
            modelMap.put("artista", artista);

            //Muestra sus propios posteos
            List<Post> posteos = servicioPosteo.obtenerPosteosDeArtista(artista);
            modelMap.put("posteos", posteos);
        } else {
            return new ModelAndView("redirect:/login");
        }

        modelMap.put("notificacion", servicioNotificacion.elUsuarioTieneNotificaciones(idUsuario));
        modelMap.put("usuario", servicioUsuario.obtenerUsuarioPorId(idUsuario));

        modelMap.put("comunidades", servicioComunidad.obtenerTodasLasComunidades());
        return new ModelAndView("home", modelMap);
    }


    @GetMapping("/buscar-artista")
    public String buscarArtistaPorBuscador(String nombre, HttpSession session, Model model) {

        try {
            //Primero se busca localmente el artista
            Artista artistaLocal = repositorioArtista.buscarPorNombre(nombre);
            if (artistaLocal!=null){
                return "redirect:/artistas-local/" + artistaLocal.getId();
            }

            String token = (String) session.getAttribute("token");
            if (token == null) return "redirect:/login";

            SpotifyApi spotifyApi = spotify.obtenerInstanciaDeSpotifyConToken(token);
            Paging<Artist> resultado = spotifyApi.searchArtists(nombre).limit(10).build().execute();
            Artist[] artistas = resultado.getItems();

            Optional<Artist> coincidenciaExacta = Arrays.stream(artistas)
                    .filter(a -> a.getName().equalsIgnoreCase(nombre)).findFirst();

            if (coincidenciaExacta.isPresent()) {
                return "redirect:/artistas/" + coincidenciaExacta.get().getId();
            } else if (artistas.length > 0) {
                model.addAttribute("errorBusqueda", "No se encontró una coincidencia exacta. Mostrando el más parecido.");
                return "redirect:/artistas/" + artistas[0].getId();
            } else {
                model.addAttribute("errorBusqueda", "No se encontró ningún artista con ese nombre.");
                return "home";
            }
        }catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorBusqueda", "Ocurrió un error al buscar el artista.");
            return "home";
        }
    }



    @GetMapping("/cerrar")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @PostMapping("/postear")
    public String postearTexto(@RequestParam("texto") String texto, HttpSession session) {
        Object artistaObj = session.getAttribute("artista");
        if (artistaObj == null) {
            // No es artista, por lo tanto no puede subir un post
            return "redirect:/home";
        }

        Artista artista = (Artista) artistaObj;
        servicioPosteo.publicarPosteo(artista, texto);
        return "redirect:/home";
    }

}



