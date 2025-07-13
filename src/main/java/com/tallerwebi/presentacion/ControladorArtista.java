package com.tallerwebi.presentacion;

import com.mercadopago.resources.preference.Preference;
import com.neovisionaries.i18n.CountryCode;
import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;


import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

@Controller
public class ControladorArtista {
    private final ServicioFavorito servicioFavorito;
    private final SpotifyApi spotifyApi;
    private final ServicioUsuario servicioUsuario;
    private final ServicioPreescucha servicioPreescucha;
    private final ServicioArtista servicioArtista;

    @Autowired
    private ServicioMercadoPago servicioMercadoPago;


    public ControladorArtista(ServicioFavorito servicioFavorito, SpotifyApi spotifyApi, ServicioUsuario servicioUsuario, ServicioPreescucha servicioPreescucha, ServicioArtista servicioArtista) {
        this.servicioFavorito = servicioFavorito;
        this.spotifyApi = spotifyApi;
        this.servicioUsuario = servicioUsuario;
        this.servicioPreescucha = servicioPreescucha;
        this.servicioArtista = servicioArtista;
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


    @GetMapping("/artistas-local/{id}")
    public String verArtistaLocal(@PathVariable Long id, Model model, HttpSession session) {
        try{
            Artista artista = servicioArtista.buscarPorId(id);
            if(artista == null){
                return "redirect:/home";
            }

            Usuario usuario = (Usuario) session.getAttribute("usuario");
            boolean esFavorito = usuario!= null && servicioFavorito.yaEsFavorito(String.valueOf(id),usuario);
            model.addAttribute("esFavorito", esFavorito);
            model.addAttribute("artistaLocal", artista);

            List<Preescucha> preescuchas = artista.getPreescuchas();
            if (preescuchas == null){
                preescuchas = new ArrayList<>();
            }
            model.addAttribute("preescuchas", preescuchas);

            List<Long> preescuchasCompradasIds = new ArrayList<>();
            if (usuario != null) {
                for (Preescucha p : preescuchas) {
                    if (servicioPreescucha.yaComproPreescuchaLocal(p.getId(), usuario)) {
                        preescuchasCompradasIds.add((long) p.getId());
                    }
                }
            }
            model.addAttribute("preescuchasCompradasIds", preescuchasCompradasIds);

            return "detalle-artista-local";
        }
        catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }


    @PostMapping("/artistas/{id}/favorito")
    public String agregarAFavoritos(@PathVariable String id, HttpSession session) {
        Object usuarioIdObj = session.getAttribute("user");

        if (usuarioIdObj != null) {
            Long usuarioId = Long.valueOf(usuarioIdObj.toString());
            Usuario usuario = servicioUsuario.obtenerUsuarioPorId(usuarioId);
            servicioFavorito.agregarFavorito(id,usuario);
        }
        return "redirect:/perfil";

    }

    @PostMapping("/artistas-locales/{id}/favorito")
    public String agregarFavoritoLocal(@PathVariable Long id, HttpSession session) {
        Object usuarioIdObj = session.getAttribute("user");

        if (usuarioIdObj != null) {
            Long usuarioId = Long.valueOf(usuarioIdObj.toString());
            Usuario usuario = servicioUsuario.obtenerUsuarioPorId(usuarioId);

            String idLocal = "LOCAL_" + id;
            servicioFavorito.agregarFavorito(idLocal,usuario);
        }

        return "redirect:/perfil";
    }

    @PostMapping("/artistas/{id}/comprar-preescucha")
    public String comprarPreescucha(@PathVariable String id, HttpSession session, String albumId) {
        Object usuarioIdObj = session.getAttribute("user");

        if (usuarioIdObj != null) {
            Long usuarioId = Long.valueOf(usuarioIdObj.toString());
            Usuario usuario = servicioUsuario.obtenerUsuarioPorId(usuarioId);

            if(!servicioPreescucha.yaComproPreescucha(albumId, usuario)){
                try{
                    Preference pref = servicioMercadoPago.crearPreferenciaPago(
                            "Pre-escucha exclusiva del album " + albumId,
                            new BigDecimal("100.00"),
                            "https://e63cc7a933be.ngrok-free.app/spring/pago-exitoso",
                            "https://e63cc7a933be.ngrok-free.app/spring/pago-error",
                            albumId
                    );
                    return "redirect:" + pref.getInitPoint();
                } catch (Exception e){
                    e.printStackTrace();
                    return "redirect:/perfil?errorPago";
                }
            }
        }
        return "redirect:/perfil";
    }

    @PostMapping("/artistas-locales/{artistaId}/comprar-preescucha/{preescuchaId}")
    public String comprarPreescuchaLocal(@PathVariable Long artistaId, @PathVariable int preescuchaId, HttpSession session) {
        Object usuarioIdObj = session.getAttribute("user");

        if (usuarioIdObj != null) {
            Long usuarioId = Long.valueOf(usuarioIdObj.toString());
            Usuario usuario = servicioUsuario.obtenerUsuarioPorId(usuarioId);

            if (!servicioPreescucha.yaComproPreescuchaLocal(preescuchaId, usuario)){
                servicioPreescucha.comprarPreescuchaLocal(preescuchaId, usuario);
                session.setAttribute("preescuchaExitosaLocal", "Compra exitosa de la preescucha");
            }

        }
        return "redirect:/perfil";
    }


    @GetMapping("/pago-exitoso")
    public String pagoExitoso(@RequestParam Map<String, String> params, HttpSession session, Model model){
        Object usuarioIdObj = session.getAttribute("user");
        if (usuarioIdObj != null) {
            Long usuarioId = Long.valueOf(usuarioIdObj.toString());
            Usuario usuario = servicioUsuario.obtenerUsuarioPorId(usuarioId);

            String albumId = params.get("external_reference");

            if(albumId != null && !servicioPreescucha.yaComproPreescucha(albumId, usuario)){
                servicioPreescucha.comprarPreescucha(albumId, usuario);
            }
            model.addAttribute("albumId", albumId);
        }
        return "pago-exitoso";
    }

    @GetMapping("/pago-error")
    public String pagoError(){
        return "pago-error";
    }


}
