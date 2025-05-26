package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public interface ServicioComunidad {
    void guardarMensaje(String mensaje);
    void guardarUsuarioEnComunidad();
    void obtenerMensajes();
    ChatMessage register(ChatMessage message, SimpMessageHeaderAccessor simpMessageHeaderAccessor);
    ChatMessage send(ChatMessage message);
    void conectarmeALaComunidad(Usuario usuario);
    Usuario obtenerUsuarioDeLaComunidad(String user);
}
