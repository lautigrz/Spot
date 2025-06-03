package com.tallerwebi.presentacion;

import com.google.gson.JsonParser;
import com.tallerwebi.dominio.Mensaje;
import com.tallerwebi.dominio.ServicioComunidad;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.presentacion.dto.ChatMessage;
import com.tallerwebi.presentacion.dto.Sincronizacion;
import org.hibernate.annotations.Synchronize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.requests.data.player.StartResumeUsersPlaybackRequest;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Controller
public class ControladorComunidad {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private ServicioComunidad servicioComunidad;

    public ControladorComunidad(ServicioComunidad servicioComunidad) {
        this.servicioComunidad = servicioComunidad;
    }


    @GetMapping("lista")
    public String listar(Model model) {

        model.addAttribute("comunidades", servicioComunidad.obtenerTodasLasComunidades());

        return "lista-comunidades";
    }


    @MessageMapping("/chat.repro")
    @SendToUser("/queue/playback")
    public Sincronizacion sincronizar(@Payload ChatMessage message) throws Exception {
        // o podés devolver un Synchronize vacío o con error, depende de tu diseño

        Sincronizacion sincronizacion = null;

        try{
            System.out.println("Entroo a sincronizar");
            sincronizacion = servicioComunidad.obtenerSincronizacion("lautigrz");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Error al obtener la sincronizacion");
        }

        return sincronizacion;
    }


    @GetMapping("/reproducir")
    public String reproducirMusica(HttpSession session) {
        // Obtener el objeto spotifyApi de la sesión

        try {
            String token = (String) session.getAttribute("token");
            servicioComunidad.reproducirCancion(token);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/comunidad";
    }

    @MessageMapping("/chat.register/{idComunidad}")
    public void register(@Payload ChatMessage message,
                         @DestinationVariable String idComunidad,
                         SimpMessageHeaderAccessor headerAccessor) {

        servicioComunidad.register(message, headerAccessor);

        // Enviar manualmente el mensaje al canal de esa comunidad
        messagingTemplate.convertAndSend("/topic/" + idComunidad, message);
    }

    @MessageMapping("/chat.send/{idComunidad}")
    public void send(@Payload ChatMessage message,
                     @DestinationVariable String idComunidad) {

        try {
            Long id = Long.parseLong(message.getId());
            ChatMessage response = servicioComunidad.send(message, id);

            messagingTemplate.convertAndSend("/topic/" + idComunidad, response);
        } catch (NumberFormatException e) {
            // Log y manejo de error
            System.err.println("ID de usuario inválido: " + message.getId());
            // Podrías enviar un mensaje de error o ignorar
        }
    }



    @GetMapping("/comunidad/{id}")
    public String comunidad(Model model, HttpSession session, @PathVariable Long id) {

        String user = (String) session.getAttribute("user");

        String nombreComunidad = servicioComunidad.obtenerComunidad(id).getNombre();

        Usuario usuario = servicioComunidad.obtenerUsuarioDeLaComunidad(user);
        List<Mensaje> mensajes = servicioComunidad.obtenerMensajes();

        Boolean com = servicioComunidad.hayAlguienEnLaComunidad(nombreComunidad, user);

        System.out.println("validacion:" + com);

        model.addAttribute("hayUsuarios", com);

        model.addAttribute("comunidad", id);
        model.addAttribute("usuario", usuario.getUser());
        model.addAttribute("urlFoto", usuario.getUrlFoto());
        model.addAttribute("id", usuario.getId());
        model.addAttribute("token", usuario.getToken());
        model.addAttribute("mensajes", mensajes);

        return "comunidad-general";
    }
}