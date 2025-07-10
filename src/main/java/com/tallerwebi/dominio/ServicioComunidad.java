package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.*;
import org.apache.hc.core5.http.ParseException;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.requests.data.player.StartResumeUsersPlaybackRequest;

import java.io.IOException;
import java.util.List;

public interface ServicioComunidad {
    List<Mensaje> obtenerMensajes(Long id);
    ChatMessage registrarUsuarioEnCanalDeComunidad(ChatMessage message, SimpMessageHeaderAccessor simpMessageHeaderAccessor,String idComunidad);
    ChatMessage guardarMensaje(ChatMessage message, Long idUsuario, Long idComunidad);
    String obtenerUsuarioDeLaComunidadActivoDeLaLista(String canal,String user);
    List<String> obtenerTodosLosUsuariosActivosDeUnaComunidad(Long idComunidad);
    Comunidad obtenerComunidad(Long id);
    Boolean hayAlguienEnLaComunidad (String nombreComunidad, String user);
    List<Comunidad> obtenerTodasLasComunidades();
    void eliminarUsuarioDelCanal(String user);
    void agregarUserAlCanal(String idComunidad, String username);
    void crearCanalSiNoExiste(String idComunidad);
    List<UsuarioDto> obtenerUsuariosDeLaComunidad(Long idComunidad) throws IOException, ParseException, SpotifyWebApiException;
    List<ComunidadDto> buscarComunidadesPorNombre(String nombreComunidad);

    UsuarioDto obtenerUsuarioPorSuNombreEnUnaComunidad(String usuario, Long idComunidad);


}
