package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioComunidad;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.presentacion.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorComunidad {
    private ServicioComunidad servicioComunidad;

    public ControladorComunidad(ServicioComunidad servicioComunidad) {
        this.servicioComunidad = servicioComunidad;
    }

    @MessageMapping("/chat.register")
    @SendTo("/topic/public")
    public ChatMessage register(@Payload ChatMessage message, SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        return servicioComunidad.register(message, simpMessageHeaderAccessor);
    }

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public ChatMessage send(@Payload ChatMessage message) {
        return servicioComunidad.send(message);
    }

    @GetMapping("/comunidad")
    public String comunidad(Model model) {

        Usuario usuario = servicioComunidad.obtenerUsuarioDeLaComunidad("lautigrz");

        System.out.println("Usuario" + usuario.getUser());

        model.addAttribute("usuario", usuario.getUser());
        model.addAttribute("urlFoto", usuario.getUrlFoto());
        model.addAttribute("idUsuario", usuario.getId());
        model.addAttribute("token", usuario.getToken());
        return "comunidad";
    }
}