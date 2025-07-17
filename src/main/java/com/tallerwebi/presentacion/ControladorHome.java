package com.tallerwebi.presentacion;


import com.tallerwebi.dominio.*;
import com.tallerwebi.presentacion.dto.ArtistaDto;
import com.tallerwebi.presentacion.dto.PostLikeDto;
import com.tallerwebi.presentacion.dto.UsuarioPreescuchaDto;
import org.dom4j.rule.Mode;

import com.tallerwebi.dominio.ServicioComunidad;
import com.tallerwebi.dominio.ServicioInstancia;
import com.tallerwebi.dominio.ServicioNotificacion;
import com.tallerwebi.dominio.ServicioUsuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ControladorHome {


    private ServicioArtista servicioArtista;
    private ServicioUsuario servicioUsuario;
    private ServicioComunidad servicioComunidad;
    private ServicioNotificacion servicioNotificacion;
    private ServicioUsuarioComunidad servicioUsuarioComunidad;
    private ServicioInstancia spotify;
    private ServicioPosteo servicioPosteo;
    private ServicioLike servicioLike;
    private ServicioComentario servicioComentario;
    private ServicioFavorito servicioFavorito;
    private ServicioUsuarioPreescucha servicioUsuarioPreescucha;

    private ServicioPreescucha servicioPreescucha;
    public ControladorHome(ServicioArtista servicioArtista,ServicioUsuario servicioUsuario, ServicioComunidad servicioComunidad, ServicioInstancia spotify, ServicioNotificacion servicioNotificacion,ServicioPosteo servicioPosteo, ServicioLike servicioLike, ServicioUsuarioComunidad servicioUsuarioComunidad, ServicioComentario servicioComentario, ServicioFavorito servicioFavorito, ServicioUsuarioPreescucha servicioUsuarioPreescucha, ServicioPreescucha servicioPreescucha) {
            this.servicioArtista = servicioArtista;

        this.servicioUsuario = servicioUsuario;
        this.servicioPreescucha = servicioPreescucha;
        this.servicioUsuarioComunidad = servicioUsuarioComunidad;
        this.servicioComunidad = servicioComunidad;
        this.servicioNotificacion = servicioNotificacion;
        this.spotify = spotify;
        this.servicioLike = servicioLike;
        this.servicioPosteo = servicioPosteo;
        this.servicioComentario = servicioComentario;
        this.servicioFavorito = servicioFavorito;
        this.servicioUsuarioPreescucha = servicioUsuarioPreescucha;
    }

    @GetMapping("/home")
    public ModelAndView vistaHome(HttpSession session) {
        ModelMap modelMap = new ModelMap();

        Long idUsuario = (Long) session.getAttribute("user");
        Object artistaObj = session.getAttribute("artista");

        if (idUsuario != null) {
            Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
            modelMap.put("usuario", usuario);
            modelMap.put("favoritos", servicioFavorito.obtenerFavoritos(usuario));



            List<Post> posteos = servicioPosteo.obtenerPosteosDeArtistasFavoritos(usuario);
            List<Long> idsDePostConLike = servicioLike.devolverIdsDePostConLikeDeUsuarioDeUnaListaDePosts(idUsuario, posteos.stream().map(Post::getId).collect(Collectors.toList()));

            List<PostLikeDto> postsConLike = posteos.stream()
                    .map(post -> {
                        boolean liked = idsDePostConLike.contains(post.getId());
                        List<Comentario> comentarios = servicioComentario.obtenerComentariosDePosteo(post.getId());
                        return new PostLikeDto(post, liked, comentarios);
                    })
                    .collect(Collectors.toList());


            modelMap.put("posteos", postsConLike);


            List<Comunidad> comunidadesUnidas = servicioUsuarioComunidad.obtenerComunidadesDondeELUsuarioEsteUnido(idUsuario);

            List<Comunidad> todasLasComunidades = servicioComunidad.obtenerTodasLasComunidades();

            List<Comunidad> comunidadesNoUnidas = todasLasComunidades.stream()
                    .filter(comunidad -> !comunidadesUnidas.contains(comunidad))
                    .collect(Collectors.toList());


            modelMap.put("usuarioComunidad", comunidadesUnidas);
            modelMap.put("comunidades", comunidadesNoUnidas);

            modelMap.put("compras", servicioUsuarioPreescucha.buscarPorUsuario(idUsuario));
            modelMap.put("notificacion", servicioNotificacion.elUsuarioTieneNotificaciones(idUsuario));
        } else if (artistaObj != null) {
            Artista artista = (Artista) artistaObj;
            modelMap.put("artista", artista);

            List<Post> posteos = servicioPosteo.obtenerPosteosDeArtista(artista);
            List<PostLikeDto> postsConLike = posteos.stream()
                    .map(post -> {
                        List<Comentario> comentarios = servicioComentario.obtenerComentariosDePosteo(post.getId());
                        return new PostLikeDto(post, false, comentarios);
                    })
                    .collect(Collectors.toList());


            modelMap.put("posteos", postsConLike);
            modelMap.put("preescucha", servicioPreescucha.obtenerPreescuchasPorArtista(artista.getId()));
        } else {
            return new ModelAndView("redirect:/login");
        }

       // modelMap.put("comunidades", servicioComunidad.obtenerTodasLasComunidades());
        return new ModelAndView("home", modelMap);

    }

    @GetMapping("/compras-usuario/{idUsuario}")
    @ResponseBody
    public ResponseEntity<List<UsuarioPreescuchaDto>> comprasOrdenadas(
            @PathVariable Long idUsuario,
            @RequestParam(defaultValue = "ASC") String orden) {

        List<UsuarioPreescuchaDto> compras = servicioUsuarioPreescucha.buscarPorUsuarioOrdenado(idUsuario, orden);

        if (compras.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(compras);
    }



    @GetMapping("/buscar-artista")
    public String buscarArtistaPorBuscador(String nombre, HttpSession session, Model model) {

        try {
            //Primero se busca localmente el artista
            Artista artistaLocal = servicioArtista.buscarPorNombre(nombre);
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
                return "redirect:/perfil/artista/" + artistas[0].getId();
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


    @GetMapping("/buscador-artista")
    @ResponseBody
    public ResponseEntity<List<ArtistaDto>> buscarArtistas(@RequestParam String texto, HttpSession session) {
        try{
            List<ArtistaDto> resultados = new ArrayList<>();

            //1ero buscamos localmente
            List<Artista> artistasLocales = servicioArtista.buscarPorTexto(texto);
            for (Artista a : artistasLocales) {
                resultados.add(new ArtistaDto(a.getId(), a.getNombre(), a.getFotoPerfil(), "/spring/perfil/artista/" + a.getId(), true));
            }

            //2 Se busca por spoti
            String token = (String) session.getAttribute("token");
            if (token != null) {
                SpotifyApi api = spotify.obtenerInstanciaDeSpotifyConToken(token);
                Paging<Artist> respuesta = api.searchArtists(texto).limit(5).build().execute();
                for (Artist a : respuesta.getItems()) {
                    String imagen = a.getImages().length > 0 ? a.getImages()[0].getUrl() : null;
                    resultados.add(new ArtistaDto(null, a.getName(), imagen, "/spring/artistas/" + a.getId(), false));
                }
            }

            return ResponseEntity.ok(resultados);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }

    }


    @GetMapping("/cerrar")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }



}



