package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.ChatMessage;
import com.tallerwebi.presentacion.dto.Sincronizacion;
import org.apache.hc.core5.http.ParseException;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.requests.data.player.StartResumeUsersPlaybackRequest;

import java.io.IOException;
import java.util.List;

public interface ServicioComunidad {
    void guardarMensaje(String mensaje, Long idUsuario);
    void guardarUsuarioEnComunidad();
    List<Mensaje> obtenerMensajes();
    ChatMessage register(ChatMessage message, SimpMessageHeaderAccessor simpMessageHeaderAccessor);
    ChatMessage send(ChatMessage message, Long idUsuario);
    void conectarmeALaComunidad(Usuario usuario);
    Usuario obtenerUsuarioDeLaComunidad(String user);
    String obtenerUsuarioDeLaComunidadActivoDeLaLista(String canal);
    Comunidad obtenerComunidad(Long id);
    Boolean reproducirCancion(String token) throws Exception;
    Sincronizacion obtenerSincronizacion(String user) throws Exception;
    int obtenerPosicionEnMsDeLoQueEscucha(String token) throws IOException, ParseException, SpotifyWebApiException;
    String obtenerTokenDelUsuario(String user);
    Boolean hayAlguienEnLaComunidad (String nombreComunidad, String user);
    List<Comunidad> obtenerTodasLasComunidades();
}
