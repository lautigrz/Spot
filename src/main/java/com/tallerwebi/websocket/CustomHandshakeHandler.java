package com.tallerwebi.websocket;

import org.codehaus.plexus.component.annotations.Component;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession(false);
            // usar sesión
            if(session != null) {
                Object principal = session.getAttribute("user");
                if(principal != null) {
                    String userId = principal.toString();

                    attributes.put("user", userId);

                    System.out.println("principal is " + userId);

                    return () -> userId;

                }
            }
        }

        return null;
    }


}
