package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioComunidad;
import com.tallerwebi.dominio.ServicioInstancia;
import com.tallerwebi.dominio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Optional;

@Controller
public class ControladorHome {

    private ServicioUsuario servicioUsuario;
    private ServicioComunidad servicioComunidad;
    private ServicioInstancia spotify;


    public ControladorHome(ServicioUsuario servicioUsuario, ServicioComunidad servicioComunidad, ServicioInstancia spotify) {
        this.servicioUsuario = servicioUsuario;
        this.servicioComunidad = servicioComunidad;
        this.spotify = spotify;
    }

    @GetMapping("/home")
    public ModelAndView vistaHome(HttpSession session) {
        ModelMap modelMap = new ModelMap();

        Long idUsuario = (Long) session.getAttribute("user");

        modelMap.put("usuario", servicioUsuario.obtenerUsuarioPorId(idUsuario));
        modelMap.put("comunidades", servicioComunidad.obtenerTodasLasComunidades());
        return new ModelAndView("home", modelMap);
    }

    @GetMapping("/buscar-artista")
    public String buscarArtistaPorBuscador(String nombre, HttpSession session, Model model) {
        try {
            String token = (String) session.getAttribute("token");
            if (token == null) {
                return "redirect:/login";
            }
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
        } catch (Exception e) {
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


}
