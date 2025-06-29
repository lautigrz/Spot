package com.tallerwebi.dominio;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import com.tallerwebi.presentacion.dto.CancionDto;
import com.tallerwebi.presentacion.dto.Sincronizacion;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.exceptions.detailed.BadGatewayException;
import se.michaelthelin.spotify.exceptions.detailed.UnauthorizedException;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.player.StartResumeUsersPlaybackRequest;

import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class ServicioReproduccionImpl implements ServicioReproduccion {

    public static Map<String,String> reproduccionDelUsuario = new HashMap<>();

    private ServicioInstancia servicioInstancia;
    private ServicioSpotify servicioSpotify;
    private ServicioComunidad servicioComunidad;
    private RepositorioComunidad repositorioComunidad;
    private RepositorioUsuarioComunidad repositorioUsuarioComunidad;
    @Autowired
    public ServicioReproduccionImpl(ServicioInstancia servicioInstancia, ServicioSpotify servicioSpotify, RepositorioComunidad repositorioComunidad
            , ServicioComunidad servicioComunidad, RepositorioUsuarioComunidad repositorioUsuarioComunidad) {
        this.servicioInstancia = servicioInstancia;
        this.servicioSpotify = servicioSpotify;
        this.repositorioUsuarioComunidad = repositorioUsuarioComunidad;
        this.repositorioComunidad = repositorioComunidad;
        this.servicioComunidad = servicioComunidad;
    }


    @Override
    public Boolean reproducirCancion(String token, Long idComunidad, Long idUsuario) throws IOException, ParseException, SpotifyWebApiException {
        SpotifyApi spotifyApi = servicioInstancia.obtenerInstanciaDeSpotifyConToken(token);
        Usuario usuario = repositorioUsuarioComunidad.obtenerUsuarioEnComunidad(idUsuario, idComunidad).getUsuario();


        List<Playlist> playlists = repositorioComunidad.obtenerPlaylistsPorComunidadId(idComunidad);
        if (playlists == null || playlists.isEmpty()) {
            throw new IllegalStateException("La comunidad no tiene playlists.");
        }

        Set<Cancion> canciones = playlists.get(0).getCanciones();
        if (canciones == null || canciones.isEmpty()) {
            throw new IllegalStateException("La playlist no tiene canciones.");
        }

        JsonArray jsonArray = new JsonArray();
        for (Cancion cancion : canciones) {
            jsonArray.add(new JsonPrimitive(cancion.getUri()));
        }

        try {
            StartResumeUsersPlaybackRequest star = spotifyApi.startResumeUsersPlayback()
                    .uris(jsonArray)
                    .position_ms(0)
                    .build();
            star.execute();
        } catch (SpotifyWebApiException e) {
            if (e instanceof BadGatewayException) {
                throw new RuntimeException("Spotify respondió con error 502 (Bad Gateway). ¿Hay algún dispositivo activo?", e);
            }

            throw new RuntimeException("Error al intentar reproducir desde Spotify.", e);
        }

        reproduccionDelUsuario.put(usuario.getUser(), playlists.get(0).getNombre());

        return true;
    }

    @Override
    public CancionDto obtenerCancionSonandoEnLaComunidad(Long idComunidad)
            throws IOException, ParseException, SpotifyWebApiException {

        List<String> usuariosActivos = servicioComunidad.obtenerTodosLosUsuariosActivosDeUnaComunidad(idComunidad);

        if (usuariosActivos == null || usuariosActivos.isEmpty()) {
            return null; // No hay usuarios activos
        }

        for (String usuario : usuariosActivos) {
            if (esUsuarioInvalido(usuario) || !estaEscuchandoMusica(usuario)) {
                continue;
            }

            try {
                CancionDto cancion = obtenerCancionActualDeUsuario(usuario, idComunidad);
                if (cancion != null) {
                    return cancion;
                }
            } catch (Exception e) {
                System.err.println("Error al obtener canción de usuario " + usuario + ": " + e.getMessage());
            }
        }

        return null; // Ningún usuario tenía una canción activa
    }

    private boolean esUsuarioInvalido(String usuario) {
        return usuario == null || usuario.trim().isEmpty();
    }

    @Override
    public Boolean estaEscuchandoMusica(String usuario) {
        return reproduccionDelUsuario.get(usuario) != null;
    }

    @Override
    public CancionDto obtenerCancionActualDeUsuario(String usuario, Long idComunidad)
            throws IOException, ParseException, SpotifyWebApiException {

        String token = repositorioComunidad.obtenerTokenDelUsuarioQuePerteneceAUnaComunidad(usuario, idComunidad);

        SpotifyApi spotifyApi = servicioInstancia.obtenerInstanciaDeSpotifyConToken(token);

        CurrentlyPlaying playing = spotifyApi.getUsersCurrentlyPlayingTrack()
                .build()
                .execute();

        if (playing != null && playing.getItem() instanceof Track) {

            Track track = (Track) playing.getItem();
            return mapearTrackACancionDto(track, playing.getProgress_ms());
        }

        return null;
    }



    private CancionDto mapearTrackACancionDto(Track track, int progreso) {
        CancionDto cancionDto = new CancionDto();
        cancionDto.setArtista(track.getArtists()[0].getName());
        cancionDto.setTitulo(track.getName());
        cancionDto.setUrlImagen(track.getAlbum().getImages()[0].getUrl());
        cancionDto.setDuracion(track.getDurationMs());
        cancionDto.setProgreso(progreso);
        return cancionDto;
    }



    @Override
    public Sincronizacion obtenerSincronizacion(String user, Long idComunidad) throws IOException, ParseException, SpotifyWebApiException {
        String token = repositorioComunidad.obtenerTokenDelUsuarioQuePerteneceAUnaComunidad(user, idComunidad);
        Set<Cancion> playlist = repositorioComunidad.obtenerCancionesDeUnaPlaylistDeUnaComunidad(idComunidad);

        List<String> uris = new ArrayList<>();

        for (Cancion cancion : playlist) {
            uris.add(cancion.getUri());
        }
        String uri = servicioSpotify.obtenerUriDeLoQueEscucha(token);
        int ms = servicioSpotify.obtenerPosicionEnMsDeLoQueEscucha(token);
        ms+=900;
        System.out.println("Ms: " + ms);
        Sincronizacion synchronize = new Sincronizacion();
        synchronize.setPositionMs(ms);
        synchronize.setUriInicio(uri);
        System.out.println("llego esto:" + ms);
        synchronize.setUris(uris);

        reproduccionDelUsuario.put(user, repositorioComunidad.obtenerPlaylistDeUnaComunidad(idComunidad).getNombre());
        return synchronize;
    }
}
