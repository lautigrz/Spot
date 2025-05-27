package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.util.List;

public interface ServicioComunidad {
    void guardarMensaje(String mensaje, Long idUsuario);
    void guardarUsuarioEnComunidad();
    List<Mensaje> obtenerMensajes();
    ChatMessage register(ChatMessage message, SimpMessageHeaderAccessor simpMessageHeaderAccessor);
    ChatMessage send(ChatMessage message, Long idUsuario);
    void conectarmeALaComunidad(Usuario usuario);
    Usuario obtenerUsuarioDeLaComunidad(String user);
}
