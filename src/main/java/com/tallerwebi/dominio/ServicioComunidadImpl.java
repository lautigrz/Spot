package com.tallerwebi.dominio;

import com.google.gson.JsonParser;
import com.tallerwebi.presentacion.dto.CancionDto;
import com.tallerwebi.presentacion.dto.ChatMessage;
import com.tallerwebi.presentacion.dto.Sincronizacion;
import com.tallerwebi.presentacion.dto.UsuarioDto;
import org.apache.hc.core5.http.ParseException;
import org.hibernate.annotations.Synchronize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.player.StartResumeUsersPlaybackRequest;

import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class ServicioComunidadImpl implements ServicioComunidad {
    private static Map<String, List<String>> canales = new HashMap<>();

    private RepositorioComunidad repositorioComunidad;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioUsuarioComunidad repositorioUsuarioComunidad;
    @Autowired
    public ServicioComunidadImpl(RepositorioUsuario repositorioUsuario ,RepositorioComunidad repositorioComunidad, RepositorioUsuarioComunidad repositorioUsuarioComunidad) {
        this.repositorioUsuarioComunidad = repositorioUsuarioComunidad;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioComunidad = repositorioComunidad;
    }

    @Override
    public List<Mensaje> obtenerMensajes(Long id) {
        return repositorioComunidad.obtenerMensajesDeComunidad(id);
    }

    // falta test
    @Override
    public ChatMessage registrarUsuarioEnCanalDeComunidad(ChatMessage message, SimpMessageHeaderAccessor simpMessageHeaderAccessor, String idComunidad) {
        try {

            simpMessageHeaderAccessor.getSessionAttributes().put("usuario", message.getSender());

            String username = (String) simpMessageHeaderAccessor.getSessionAttributes().get("usuario");
            crearCanalSiNoExiste(idComunidad);
            List<String> usuarios = canales.get(idComunidad);
            if (username != null && !usuarios.contains(username)) {
                agregarUserAlCanal(idComunidad, username);
                System.out.println("agregado " + username + "en canal " + idComunidad);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }

    @Override
    public ChatMessage guardarMensaje(ChatMessage message, Long idUsuario, Long idComunidad) {
        try {
            UsuarioComunidad usuarioComunidad = repositorioUsuarioComunidad.obtenerUsuarioEnComunidad(idUsuario, idComunidad);
            repositorioComunidad.guardarMensajeDeLaComunidad(message.getContent(), usuarioComunidad.getComunidad(),usuarioComunidad.getUsuario());
            return message;
        } catch (Exception e) {
            System.out.println("Error al guardar el mensaje: " + e.getMessage());
            e.printStackTrace();
        }
        return new ChatMessage();
    }


    @Override
    public String obtenerUsuarioDeLaComunidadActivoDeLaLista(String canal, String user) {

        if(user.isEmpty()){
            return "";
        }
        List<String> usuarios = canales.get(canal);

        for (String usuario : usuarios) {
            if(!usuario.equals(user)) {
                return usuario;
            }
        }
        return "";
    }

    @Override
    public List<String> obtenerTodosLosUsuariosActivosDeUnaComunidad(Long idComunidad) {
        String idd = String.valueOf(idComunidad);
        if(canales.get(idd) == null){
            return new ArrayList<>();
        }
        return canales.get(idd);
    }

    @Override
    public Comunidad obtenerComunidad(Long id) {
        return repositorioComunidad.obtenerComunidad(id);
    }


    @Override
    public Boolean hayAlguienEnLaComunidad(String nombreComunidad, String user) {
        List<String> usuarios = canales.get(nombreComunidad);

        // Validamos que no sea null y que haya al menos otro usuario que no sea 'user'
        return usuarios != null && usuarios.stream().anyMatch(u -> !u.equals(user));
    }

    @Override
    public List<Comunidad> obtenerTodasLasComunidades() {

        return repositorioComunidad.obtenerComunidades();
    }


    @Override
    public void eliminarUsuarioDelCanal(String user) {
        for (List<String> usuarios : canales.values()) {
            usuarios.remove(user);
        }
    }
    @Override
    public void agregarUserAlCanal(String idComunidad, String username) {

        canales.get(idComunidad).add(username);
    }

    @Override
    public void crearCanalSiNoExiste(String idComunidad) {
        canales.putIfAbsent(idComunidad, new ArrayList<>());
    }

    @Override
    public List<UsuarioDto> obtenerUsuariosDeLaComunidad(Long idComunidad) {

        List<UsuarioDto> usuariosDto = new ArrayList<>();

        List<Usuario> usuarios = repositorioComunidad.obtenerUsuariosPorComunidad(idComunidad);

        System.out.println("usuarios del repo" + usuarios.size());

        List<String> usuariosEnCanalActivos = obtenerTodosLosUsuariosActivosDeUnaComunidad(idComunidad);

        System.out.println("usuarios del canal" + usuariosEnCanalActivos.size());

        for (Usuario usuario : usuarios) {
            UsuarioDto usuarioDto = new UsuarioDto();
            if(usuariosEnCanalActivos.contains(usuario.getUser())){

                usuarioDto.setActivo(true);

            }

            usuarioDto.setUser(usuario.getUser());
            usuarioDto.setId(usuario.getId());
            usuarioDto.setUrlFoto(usuario.getUrlFoto());
            usuariosDto.add(usuarioDto);

        }
        return usuariosDto;
    }

    public static void limpiarCanales() {
        canales.clear();
    }


}

