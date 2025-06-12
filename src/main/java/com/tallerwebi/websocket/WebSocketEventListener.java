package com.tallerwebi.websocket;

import com.tallerwebi.dominio.ServicioComunidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {
    @Autowired
    private ServicioComunidad servicioComunidad;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("usuario");

        if (username != null) {
            servicioComunidad.eliminarUsuarioDelCanal(username);
            System.out.println("Usuario desconectado: " + username);
        }
    }

}
