package com.tallerwebi.presentacion;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
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
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

    @Autowired
    private ServicioPreescucha servicioPreescucha;
    @Autowired
    private ServicioUsuarioPreescucha servicioUsuarioPreescucha;

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

            String urlImagen = "";
            if(imagen != null){
                urlImagen = servicioGuardarImagen.guardarImagenDePlaylist(imagen);
            }


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

        messagingTemplate.convertAndSend("/topic/" + idComunidad, message);
    }

    @MessageMapping("/chat.send/{idComunidad}")
    public void send(@Payload ChatMessage message,
                     @DestinationVariable String idComunidad) {

        try {
            Long id = Long.parseLong(message.getId());
            Long idComuni = Long.parseLong(idComunidad);
            ChatMessage response = servicioComunidad.guardarMensaje(message, id, idComuni);
            response.setType(ChatMessage.MessageType.CHAT);
            messagingTemplate.convertAndSend("/topic/" + idComunidad, response);
        } catch (NumberFormatException e) {
            System.err.println("ID de usuario inválido: " + message.getId());
        }
    }

    @PostMapping("/abandonar-comunidad/{idComunidad}/{idUsuario}")
    public String abandonarComunidad(@PathVariable Long idComunidad, @PathVariable Long idUsuario) {
        if (idComunidad == null || idUsuario == null) {
            return "redirect:/error";
        }
        try {
              Boolean seELimino =  servicioUsuarioComunidad.eliminarUsuarioDeComunidad(idUsuario, idComunidad);
                return seELimino ? "redirect:/home" : "redirect:/error";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/comunidad/" + idComunidad;
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
            model.put("habilitar", true);
            model.put("usuario", usuarioComunidad.getUsuario().getUser());
            model.put("urlFoto",  usuarioComunidad.getUsuario().getUrlFoto());
            model.put("id",  usuarioComunidad.getUsuario().getId());
            model.put("token",  usuarioComunidad.getUsuario().getToken());
            model.put("hayUsuarios", hayAlguienEscuchandoMusica(idComunidad));
            model.put("playlistsDeLaComunidad", servicioPlaylist.obtenerPlaylistsRelacionadasAUnaComunidad(id));
            model.put("mensajes", servicioComunidad.obtenerMensajes(id));
            model.put("rol", usuarioComunidad.getRol());
            model.put("usuarioComunidad", servicioUsuarioComunidad.obtenerComunidadesDondeELUsuarioEsteUnido(idUsuario));
            model.put("recomendaciones", servicioRecomedacionComunidad.obtenerRecomendacionesPorComunidadQueNoFueronLeidas(Long.parseLong(idComunidad)));
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
          boolean estaEscuchando = servicioReproduccion.estaEscuchandoMusica(us, Long.parseLong(id));
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

            if(servicioComunidad.obtenerComunidadDeArtista(idComunidad,idUsuario)) return ResponseEntity.status(HttpStatus.OK).build();
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


    @PostMapping("/compartir-post/{idPost}/{idUsuario}")
    @ResponseBody
    public ResponseEntity<?> compartirPosteoEnComunidad(@PathVariable Long idPost,
                                                        @RequestBody List<Long> comunidades,
                                                        @PathVariable Long idUsuario) {
        try {
            servicioUsuarioComunidad.compartirPosteoEnComunidad(idPost, comunidades, idUsuario);
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Post compartido correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al compartir el post");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @GetMapping("/comunidad-preescucha/{idPreescucha}")
    public ModelAndView preescuchaComunidad(@PathVariable Long idPreescucha, HttpSession session, ModelMap model) throws IOException, ParseException, SpotifyWebApiException {
        Long idUsuario = (Long) session.getAttribute("user");
        Artista artista = (Artista) session.getAttribute("artista");

        if (idUsuario == null && artista == null) {
            return new ModelAndView("redirect:/login");
        }

        Comunidad comunidad = servicioComunidad.obtenerComuniadDePreescucha(idPreescucha);
        if (comunidad == null) {
            return new ModelAndView("error").addObject("mensaje", "No se encontró la comunidad");
        }
        boolean estaEnComunidad = false;
        if(idUsuario != null) {
            UsuarioComunidad usuarioComunidad = servicioUsuarioComunidad.obtenerUsuarioEnComunidad(idUsuario, comunidad.getId());
            boolean compro = servicioUsuarioPreescucha.comprobarSiYaCompro(idUsuario, idPreescucha);
            LocalDateTime fechaEscucha = usuarioComunidad.getComunidad().getPreescucha().getFechaEscucha();

            boolean esFuturo = fechaEscucha.isAfter(LocalDateTime.now());

            if (compro && !esFuturo) {
                model.put("preescucha", servicioPreescucha.obtenerPreescuchaLocal(idPreescucha));
                model.put("usuariosActivos", servicioComunidad.obtenerUsuariosDeLaComunidad(comunidad.getId()));
                model.put("fotoUsuario", servicioUsuario.obtenerUsuarioDtoPorId(idUsuario).getUrlFoto());
                model.put("usuario", usuarioComunidad.getUsuario().getUser());
                model.put("urlFoto", usuarioComunidad.getUsuario().getUrlFoto());
                model.put("id", usuarioComunidad.getUsuario().getId());
                model.put("token", usuarioComunidad.getUsuario().getToken());
               // model.put("hayUsuarios", hayAlguienEscuchandoMusica(comunidad.getId().toString()));
                model.put("playlistsDeLaComunidad", servicioPlaylist.obtenerPlaylistsRelacionadasAUnaComunidad(comunidad.getId()));
                model.put("mensajes", servicioComunidad.obtenerMensajes(comunidad.getId()));
                model.put("rol", usuarioComunidad.getRol());
                model.put("recomendaciones", servicioRecomedacionComunidad.obtenerRecomendacionesPorComunidadQueNoFueronLeidas(comunidad.getId()));
                model.put("eventos", servicioEventoCombinado.obtenerEventos(comunidad.getArtista(), comunidad.getId()));
                estaEnComunidad = true;
            }else if(esFuturo){
                Duration duracion = Duration.between(LocalDateTime.now(), fechaEscucha);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                String fechaFormateada = fechaEscucha.format(formatter);

                model.addAttribute("fechaFormateada", fechaFormateada);
                model.addAttribute("faltanDias", duracion.toDays());
                model.addAttribute("faltanHoras", duracion.toHoursPart());
                model.addAttribute("faltanMinutos", duracion.toMinutesPart());

                return new ModelAndView("preescucha-pendiente", model);

            }
        }
        else if(comunidad.getHost().equals(artista)) {
            model.put("usuariosActivos", servicioComunidad.obtenerUsuariosDeLaComunidad(comunidad.getId()));
            model.put("usuario", artista.getNombre());
            model.put("urlFoto", artista.getFotoPerfil());
            model.put("id", artista.getId());
            model.put("preescucha", servicioPreescucha.obtenerPreescuchaLocal(idPreescucha));

            model.put("token", "");
            model.put("mensajes", servicioComunidad.obtenerMensajes(comunidad.getId()));
            model.put("rol", "Artista");
            estaEnComunidad = true;
        }

        model.addAttribute("habilitar", false);
        model.put("comunidad", comunidad);
        model.put("estaEnComunidad", estaEnComunidad);


        return new ModelAndView("comunidad-general", model);
    }

    @GetMapping("/api/preescucha/{id}/canciones")
    @ResponseBody
    public List<CancionSimpleDto> obtenerCanciones(@PathVariable Long id) {
        Preescucha pre = servicioPreescucha.obtenerPreescuchaLocal(id);

        return pre.getAudios().stream()
                .map(audio -> new CancionSimpleDto(audio.getTitulo(), audio.getRutaAudio(),audio.getPreescucha().getArtista().getNombre(), audio.getPortadaUrl()))
                .collect(Collectors.toList());
    }

    @MessageMapping("/preescucha.iniciar.{idComunidad}")
    public void iniciarPreescucha(@DestinationVariable Long idComunidad, String payload) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(payload);
        JsonNode cancionesNode = root.path("data").path("canciones");

        List<CancionSimpleDto> canciones = new ArrayList<>();
        for (JsonNode cancionNode : cancionesNode) {
            CancionSimpleDto c = mapper.treeToValue(cancionNode, CancionSimpleDto.class);
            canciones.add(c);
        }

        servicioPreescucha.iniciarPreescucha(idComunidad, canciones);

        messagingTemplate.convertAndSend(
                "/topic/" + idComunidad,
                payload
        );
    }


    @MessageMapping("/preescucha.estado.{idComunidad}")
    @SendTo("/topic/estado-actual.{idComunidad}")
    public Map<String, Object> estadoPreescucha(@DestinationVariable Long idComunidad, Principal principal) {
        System.out.println("Principal que pidió estado: " + principal.getName());

        EstadoPreescucha estado = servicioPreescucha.obtenerEstado(idComunidad);
        if (estado == null) {
            System.out.println("No hay estado para comunidad " + idComunidad);
            return null;
        }

        System.out.println("Estado encontrado para comunidad " + idComunidad);

        if (estado.isReproduciendo()) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("type", "ESTADO_ACTUAL");
            payload.put("canciones", estado.getCanciones());
            payload.put("indiceActual", estado.getIndiceActual());
            payload.put("idComunidad", idComunidad);

            long segundosReproducidos = (System.currentTimeMillis() - estado.getTimestampInicio()) / 1000;
            payload.put("segundosReproducidos", segundosReproducidos);

            System.out.println("Enviando payload a usuario " + principal.getName() + ": " + payload);
            return payload;
        }

        return null;

    }


    @MessageMapping("/preescucha.actualizarEstado.{idComunidad}")
    public void actualizarEstado(@DestinationVariable Long idComunidad, String payload) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(payload);

        int indiceActual = root.path("indiceActual").asInt();
        int segundosReproducidos = root.path("segundosReproducidos").asInt();

        // Actualiza estado interno
        servicioPreescucha.actualizarEstado(idComunidad, indiceActual, segundosReproducidos);
        EstadoPreescucha estado = servicioPreescucha.obtenerEstado(idComunidad);
        // Actualiza el timestamp para que futuros cálculos sean correctos
        long nuevoTimestampInicio = System.currentTimeMillis() - (segundosReproducidos * 1000L);
        estado.setTimestampInicio(nuevoTimestampInicio);

        Map<String, Object> payloadResponse = new HashMap<>();
        payloadResponse.put("type", "ESTADO_ACTUAL");
        payloadResponse.put("canciones", estado.getCanciones());
        payloadResponse.put("indiceActual", estado.getIndiceActual());
        payloadResponse.put("segundosReproducidos", segundosReproducidos);
        payloadResponse.put("idComunidad", idComunidad);
        System.out.println("Enviando payload actualizado a todos los usuarios de la comunidad " + idComunidad + ": " + payloadResponse);


        messagingTemplate.convertAndSend("/topic/" + idComunidad, payloadResponse);
    }


}