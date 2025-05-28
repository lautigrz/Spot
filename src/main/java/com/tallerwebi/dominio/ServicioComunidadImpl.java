package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ServicioComunidadImpl implements ServicioComunidad {
    private static final Map<String, List<String>> canales = new HashMap<>();


    private RepositorioUsuario repositorioUsuario;
    private RepositorioComunidad repositorioComunidad;

    @Autowired
    public ServicioComunidadImpl(RepositorioUsuario repositorioUsuario, RepositorioComunidad repositorioComunidad) {
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioComunidad = repositorioComunidad;
    }

    @Override
    public void guardarMensaje(String mensaje, Long idUsuario) {
        repositorioComunidad.guardarMensajeDeLaComunidad(mensaje, idUsuario);
    }

    @Override
    public void guardarUsuarioEnComunidad() {

    }

    @Override
    public List<Mensaje> obtenerMensajes() {
        return repositorioComunidad.obtenerMensajesDeComunidad();
    }

    // falta test
    @Override
    public ChatMessage register(ChatMessage message, SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        try{
            simpMessageHeaderAccessor.getSessionAttributes().put("usuario", message.getSender());

            String username = (String) simpMessageHeaderAccessor.getSessionAttributes().get("usuario");

            // Si el canal no existe, crearlo
            canales.putIfAbsent("public", new ArrayList<>());

            // Agregar el usuario a la lista del canal correspondiente
            List<String> usuarios = canales.get("public");
            if (username != null && !usuarios.contains(username)) {
                canales.get("public").add(username);
                System.out.println("agregado " + username);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }

    // falta test
    @Override
    public ChatMessage send(ChatMessage message, Long idUsuario) {
        try {
            repositorioComunidad.guardarMensajeDeLaComunidad(message.getContent(), idUsuario);
            System.out.println("Mensaje guardado correctamente");
        } catch (Exception e) {
            System.out.println("Error al guardar el mensaje: " + e.getMessage());
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public void conectarmeALaComunidad(Usuario usuario) {

    }

    @Override
    public Usuario obtenerUsuarioDeLaComunidad(String user) {
        return repositorioUsuario.buscar(user);
    }
}
