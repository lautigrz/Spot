package com.tallerwebi.dominio;

import com.google.gson.JsonParser;
import com.tallerwebi.presentacion.dto.ChatMessage;
import com.tallerwebi.presentacion.dto.Sincronizacion;
import org.apache.hc.core5.http.ParseException;
import org.hibernate.annotations.Synchronize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import se.michaelthelin.spotify.requests.data.player.StartResumeUsersPlaybackRequest;

import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class ServicioComunidadImpl implements ServicioComunidad {
    private static final Map<String, List<String>> canales = new HashMap<>();

    private SpotifyApi spotifyApi;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioComunidad repositorioComunidad;

    @Autowired
    public ServicioComunidadImpl(RepositorioUsuario repositorioUsuario, RepositorioComunidad repositorioComunidad, SpotifyApi spotifyApi) {
        this.repositorioUsuario = repositorioUsuario;
        this.spotifyApi = spotifyApi;
        this.repositorioComunidad = repositorioComunidad;
    }

    @Override
    public void guardarUsuarioEnComunidad() {

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

            // Si el canal no existe, crearlo
            canales.putIfAbsent(idComunidad, new ArrayList<>());

            // Agregar el usuario a la lista del canal correspondiente
            List<String> usuarios = canales.get(idComunidad);
            if (username != null && !usuarios.contains(username)) {
                canales.get(idComunidad).add(username);
                System.out.println("agregado " + username + "en canal " + idComunidad);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }

    // falta test
    @Override
    public ChatMessage guardarMensaje(ChatMessage message, Long idUsuario, Long idComunidad) {
        try {
            repositorioComunidad.guardarMensajeDeLaComunidad(message.getContent(), idUsuario,idComunidad);
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

    @Override
    public String obtenerUsuarioDeLaComunidadActivoDeLaLista(String canal, String user) {

        List<String> usuarios = canales.get(canal);

        for (String usuario : usuarios) {
            if(!usuario.equals(user)) {
                return usuario;
            }
        }
        return null;
    }

    @Override
    public Comunidad obtenerComunidad(Long id) {
        return repositorioComunidad.obtenerComunidad(id);
    }

    @Override
    public Boolean reproducirCancion(String token) throws Exception {

        try {
            spotifyApi.setAccessToken(token);

            StartResumeUsersPlaybackRequest star = spotifyApi.startResumeUsersPlayback()
                    .uris(JsonParser.parseString("[\"spotify:track:4stQ9ma0kqGifqLQQSgOGH\"]").getAsJsonArray())
                    .position_ms(0)
                    .build();
            star.execute();
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }

        throw new Exception();
    }

    @Override
    public Sincronizacion obtenerSincronizacion(String user) throws Exception {

            String token = obtenerTokenDelUsuario(user);
            int ms = obtenerPosicionEnMsDeLoQueEscucha(token);
            ms+=500;
            System.out.println("Ms: " + ms);
            Sincronizacion synchronize = new Sincronizacion();
            synchronize.setPositionMs(ms);

            System.out.println("llego esto:" + ms);

            synchronize.setUris(Collections.singletonList("spotify:track:4stQ9ma0kqGifqLQQSgOGH"));
            return synchronize;

    }

    @Override
    public int obtenerPosicionEnMsDeLoQueEscucha(String token) throws IOException, ParseException, SpotifyWebApiException {
        CurrentlyPlaying playing = null;
        try {
            spotifyApi = new SpotifyApi.Builder()
                    .setAccessToken(token)
                    .build();

            playing = spotifyApi.getUsersCurrentlyPlayingTrack()
                    .build().execute();

            System.out.println("Position");
            return playing.getProgress_ms();
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al obtener el token: " + e.getMessage());
        }

        return 0;

    }

    @Override
    public String obtenerTokenDelUsuario(String user) {
        return repositorioComunidad.obtenerTokenDelUsuario(user);
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


}

