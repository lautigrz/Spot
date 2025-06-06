package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.ChatMessage;
import com.tallerwebi.presentacion.dto.Sincronizacion;
import com.tallerwebi.presentacion.dto.UsuarioDto;
import org.apache.hc.core5.http.ParseException;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.requests.data.player.StartResumeUsersPlaybackRequest;

import java.io.IOException;
import java.util.List;

public interface ServicioComunidad {
    Boolean guardarUsuarioEnComunidad(Long idUsuario, Long idComunidad);
    UsuarioDto obtenerUsuarioDeLaComunidad(Long idUsuario, Long idComunidad);
    List<Mensaje> obtenerMensajes(Long id);
    ChatMessage registrarUsuarioEnCanalDeComunidad(ChatMessage message, SimpMessageHeaderAccessor simpMessageHeaderAccessor,String idComunidad);
    ChatMessage guardarMensaje(ChatMessage message, Long idUsuario, Long idComunidad);
    String obtenerUsuarioDeLaComunidadActivoDeLaLista(String canal,String user);
    Comunidad obtenerComunidad(Long id);
    Boolean reproducirCancion(String token) throws Exception;
    Sincronizacion obtenerSincronizacion(String user,Long id) throws Exception;
    int obtenerPosicionEnMsDeLoQueEscucha(String token) throws IOException, ParseException, SpotifyWebApiException;
    String obtenerTokenDelUsuario(String user, Long idComunidad);
    Boolean hayAlguienEnLaComunidad (String nombreComunidad, String user);
    List<Comunidad> obtenerTodasLasComunidades();
}
