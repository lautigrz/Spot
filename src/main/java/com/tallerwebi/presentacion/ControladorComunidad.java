package com.tallerwebi.presentacion;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallerwebi.dominio.*;

import com.tallerwebi.presentacion.dto.*;

import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;


import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class ControladorComunidad {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private ServicioComunidad servicioComunidad;
    private ServicioSpotify servicioSpotify;
    private ServicioPlaylist servicioPlaylist;
    private ServicioReproduccion servicioReproduccion;
    private ServicioGuardarImagen servicioGuardarImagen;
    private ServicioUsuarioComunidad servicioUsuarioComunidad;
    private ServicioUsuario servicioUsuario;
    private ServicioRecomedacionComunidad servicioRecomedacionComunidad;
    private ServicioEventoCombinado servicioEventoCombinado;

    public ControladorComunidad(ServicioComunidad servicioComunidad, ServicioSpotify
            servicioSpotify, ServicioPlaylist servicioPlaylist,
                                ServicioReproduccion servicioReproduccion, ServicioGuardarImagen servicioGuardarImagen
            ,ServicioUsuario servicioUsuario, ServicioUsuarioComunidad servicioUsuarioComunidad,
                                ServicioRecomedacionComunidad servicioRecomedacionComunidad, ServicioEventoCombinado servicioEventoCombinado) {
        this.servicioPlaylist = servicioPlaylist;
        this.servicioGuardarImagen = servicioGuardarImagen;
        this.servicioReproduccion = servicioReproduccion;
        this.servicioComunidad = servicioComunidad;
        this.servicioSpotify = servicioSpotify;
        this.servicioUsuario = servicioUsuario;
        this.servicioUsuarioComunidad = servicioUsuarioComunidad;
        this.servicioRecomedacionComunidad = servicioRecomedacionComunidad;
        this.servicioEventoCombinado = servicioEventoCombinado;
    }


    @GetMapping("/buscar-comunidad/{nombreComunidad}")
    @ResponseBody
    public ResponseEntity<List<ComunidadDto>> buscarComunidad(@PathVariable String nombreComunidad) {
        List<ComunidadDto> comunidades = servicioComunidad.buscarComunidadesPorNombre(nombreComunidad);
        return ResponseEntity.ok(comunidades);
    }



    @GetMapping("/busqueda-cancion/{texto}")
    public ResponseEntity<?> buscarCancion(@PathVariable String texto, HttpSession session) throws IOException, ParseException, SpotifyWebApiException {
        String token = (String) session.getAttribute("token");
        List<CancionDto> cancionesObtenida = servicioSpotify.obtenerCancionesDeSpotify(token, texto);

        return ResponseEntity.ok(cancionesObtenida);
    }


    @PostMapping("/guardar-canciones/{idComunidad}")
    @ResponseBody
    public String guardarCanciones(
            @PathVariable Long idComunidad,
            @RequestParam("nombre") String nombrePlaylist,
            @RequestParam("imagen") MultipartFile imagen,
            @RequestParam("canciones") String cancionesJson) {

        try {
            Comunidad comunidad = servicioComunidad.obtenerComunidad(idComunidad);


            String urlImagen = servicioGuardarImagen.guardarImagenDePlaylist(imagen);

            ObjectMapper mapper = new ObjectMapper();
            List<CancionDto> cancionesDto = mapper.readValue(cancionesJson, new TypeReference<List<CancionDto>>() {});


            servicioPlaylist.crearNuevaPlaylistConCanciones(comunidad, cancionesDto, nombrePlaylist, urlImagen);

            return "/spring/comunidad/" + idComunidad;

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }


    @PostMapping("/sincronizarme/{idComunidad}")
    @ResponseBody
    public ResponseEntity<?> sincronizar(@RequestBody ChatMessage message, @PathVariable Long idComunidad) {
        try {

            String usuario = servicioComunidad.obtenerUsuarioDeLaComunidadActivoDeLaLista(String.valueOf(idComunidad), message.getSender());

            Sincronizacion sincronizacion = servicioReproduccion.obtenerSincronizacion(usuario, idComunidad);
            return ResponseEntity.ok(sincronizacion);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al obtener la sincronización");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Sincronizacion());
        }
    }

    @GetMapping("/reproducir/{idComunidad}")
    public String reproducirMusica(HttpSession session, @PathVariable Long idComunidad,
                                   RedirectAttributes redirectAttributes) {
        try {
            String token = (String) session.getAttribute("token");
            Long idUsuario = (Long) session.getAttribute("user");
            servicioReproduccion.reproducirCancion(token, idComunidad, idUsuario);
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/comunidad/" + idComunidad;

    }


    @MessageMapping("/chat.register/{idComunidad}")
    public void register(@Payload ChatMessage message,
                         @DestinationVariable String idComunidad,
                         SimpMessageHeaderAccessor headerAccessor) {

        servicioComunidad.registrarUsuarioEnCanalDeComunidad(message, headerAccessor, idComunidad);

        // Enviar manualmente el mensaje al canal de esa comunidad
        messagingTemplate.convertAndSend("/topic/" + idComunidad, message);
    }

    @MessageMapping("/chat.send/{idComunidad}")
    public void send(@Payload ChatMessage message,
                     @DestinationVariable String idComunidad) {

        try {
            Long id = Long.parseLong(message.getId());
            Long idComuni = Long.parseLong(idComunidad);
            ChatMessage response = servicioComunidad.guardarMensaje(message, id, idComuni);
            messagingTemplate.convertAndSend("/topic/" + idComunidad, response);
        } catch (NumberFormatException e) {
            System.err.println("ID de usuario inválido: " + message.getId());
        }
    }

    @GetMapping("/comunidad/{id}")
    public ModelAndView comunidad(HttpSession session, @PathVariable Long id, ModelMap model) throws IOException, ParseException, SpotifyWebApiException {

        Long idUsuario = (Long) session.getAttribute("user");
        String idComunidad = String.valueOf(id);
        UsuarioComunidad usuarioComunidad = servicioUsuarioComunidad.obtenerUsuarioEnComunidad(idUsuario, id);
        Comunidad comunidad = servicioComunidad.obtenerComunidad(id);
        boolean estaEnComunidad = false;

        if (usuarioComunidad != null) {
            model.put("usuario", usuarioComunidad.getUsuario().getUser());
            model.put("urlFoto",  usuarioComunidad.getUsuario().getUrlFoto());
            model.put("id",  usuarioComunidad.getUsuario().getId());
            model.put("token",  usuarioComunidad.getUsuario().getToken());
            model.put("hayUsuarios", hayAlguienEscuchandoMusica(idComunidad));
            model.put("playlistsDeLaComunidad", servicioPlaylist.obtenerPlaylistsRelacionadasAUnaComunidad(id));
            model.put("mensajes", servicioComunidad.obtenerMensajes(id));
            model.put("rol", usuarioComunidad.getRol());
            model.put("recomendaciones", servicioRecomedacionComunidad.obtenerRecomendacionesPorComunidad(Long.parseLong(idComunidad)));
            model.put("eventos", servicioEventoCombinado.obtenerEventos(comunidad.getArtista(), id));
            estaEnComunidad = true;
        }

        model.put("fotoUsuario", servicioUsuario.obtenerUsuarioDtoPorId(idUsuario).getUrlFoto());
        model.put("comunidad", comunidad);
        model.put("estaEnComunidad", estaEnComunidad);
        model.put("usuariosActivos", servicioComunidad.obtenerUsuariosDeLaComunidad(id));

        return new ModelAndView("comunidad-general", model);
    }

    public Boolean hayAlguienEscuchandoMusica(String id) {
        List<String> usuariosActivos = servicioComunidad.obtenerTodosLosUsuariosActivosDeUnaComunidad(Long.parseLong(id));

        for(String us : usuariosActivos){
          boolean estaEscuchando = servicioReproduccion.estaEscuchandoMusica(us);
           if (estaEscuchando) {
               return true;
           }

        }
        return false;
    }



    //ResponseEntity clase de Spring que representa toda la respuesta HTTP
    @GetMapping("/unirme/{idComunidad}")
    public String unirme(HttpSession session, @PathVariable Long idComunidad) {
        Long idUsuario = (Long) session.getAttribute("user");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        Comunidad comunidad = servicioComunidad.obtenerComunidad(idComunidad);
        Boolean seGuardo = servicioUsuarioComunidad.agregarUsuarioAComunidad(usuario, comunidad, "Miembro");

        if(!seGuardo){

            return "redirect:/error";

        }

        return "redirect:/comunidad/" + idComunidad;
    }

    @GetMapping("/usuario-en-comunidad/{idUsuario}/{idComunidad}")
    @ResponseBody
    public ResponseEntity<?> usuarioEnComunidad(@PathVariable Long idUsuario, @PathVariable Long idComunidad) {

        UsuarioComunidad usuarioComunidad = servicioUsuarioComunidad.obtenerUsuarioEnComunidad(idUsuario, idComunidad);

        if(usuarioComunidad == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("/canciones-de-playlist/{idPlaylist}")
    @ResponseBody
    public List<CancionDto> cancionesDePlaylist(@PathVariable Long idPlaylist) {
        return servicioPlaylist.obtenerCancionesDeLaPlaylist(idPlaylist);
    }


    @GetMapping("/cancion/{idComunidad}")
    @ResponseBody
    public ResponseEntity<?> cancionSonando(@PathVariable Long idComunidad) {
        try {

            CancionDto cancion = servicioReproduccion.obtenerCancionSonandoEnLaComunidad(idComunidad);

            if (cancion == null) {
                return ResponseEntity.badRequest().body("Cancion  null"); // 204 No Content
            }

            return ResponseEntity.ok(cancion); // 200 OK con cuerpo

        } catch (SpotifyWebApiException e) {
            // Error específico de Spotify
            System.err.println("Error Spotify: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build(); // 502 Bad Gateway error servidor externo
        } catch (IOException | ParseException e) {
            // Otros errores del backend o parseo
            System.err.println("Error al procesar la canción: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }

}