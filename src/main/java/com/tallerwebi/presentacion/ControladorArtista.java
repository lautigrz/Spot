package com.tallerwebi.presentacion;

import com.neovisionaries.i18n.CountryCode;
import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.ServicioFavorito;
import com.tallerwebi.dominio.ServicioPreescucha;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;


import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Controller
public class ControladorArtista {
    private final ServicioFavorito servicioFavorito;
    private final SpotifyApi spotifyApi;
    private final RepositorioUsuario repositorioUsuario;
    private final ServicioPreescucha servicioPreescucha;


    public ControladorArtista(ServicioFavorito servicioFavorito, SpotifyApi spotifyApi, RepositorioUsuario repositorioUsuario, ServicioPreescucha servicioPreescucha) {
        this.servicioFavorito = servicioFavorito;
        this.spotifyApi = spotifyApi;
        this.repositorioUsuario = repositorioUsuario;
        this.servicioPreescucha = servicioPreescucha;
    }

    @GetMapping("/artistas/{id}")
    public String verArtista(@PathVariable String id, Model model, HttpSession session) {
        try{

            Artist artist = spotifyApi.getArtist(id).build().execute();
            AlbumSimplified[] albums = spotifyApi.getArtistsAlbums(id).limit(10).build().execute().getItems();
            Track[] topTracks = spotifyApi.getArtistsTopTracks(id, CountryCode.US).build().execute();

            model.addAttribute("artista", artist);
            model.addAttribute("albums", albums);
            model.addAttribute("topTracks", topTracks);


            Usuario usuario = (Usuario) session.getAttribute("usuario");
            boolean esFavorito = usuario!= null && servicioFavorito.yaEsFavorito(id,usuario);
            model.addAttribute("esFavorito", esFavorito);

            List<String> albumesComprados = List.of();
            if (usuario != null) {
                albumesComprados = servicioPreescucha.obtenerAlbumesComprados(usuario);
            }
            model.addAttribute("albumesComprados", albumesComprados);

            AlbumSimplified albumMasReciente = Arrays.stream(albums)
                    .filter(a -> a.getReleaseDate() != null)
                    .sorted((a1, a2) -> a2.getReleaseDate().compareTo(a1.getReleaseDate())) // Descendente
                    .findFirst()
                    .orElse(null);

            model.addAttribute("albumReciente", albumMasReciente);

            // Cargar mensaje si hubo compra
            String preescuchaExitosa = (String) session.getAttribute("preescuchaExitosa");
            if (preescuchaExitosa != null) {
                model.addAttribute("preescuchaExitosa", preescuchaExitosa);
                session.removeAttribute("preescuchaExitosa");
            }

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

    @PostMapping("/artistas/{id}/comprar-preescucha")
    public String comprarPreescucha(@PathVariable String id, HttpSession session, String albumId) {
        Object usuarioIdObj = session.getAttribute("user");

        if (usuarioIdObj != null) {
            Long usuarioId = Long.valueOf(usuarioIdObj.toString());
            Usuario usuario = repositorioUsuario.buscarUsuarioPorId(usuarioId);

            if(!servicioPreescucha.yaComproPreescucha(albumId, usuario)){
                servicioPreescucha.comprarPreescucha(albumId, usuario);
            }
        }

        return "redirect:/perfil";
    }


}
