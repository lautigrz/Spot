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

    @Autowired
    public ServicioReproduccionImpl(ServicioInstancia servicioInstancia, ServicioSpotify servicioSpotify, RepositorioComunidad repositorioComunidad, ServicioComunidad servicioComunidad) {
        this.servicioInstancia = servicioInstancia;
        this.servicioSpotify = servicioSpotify;
        this.repositorioComunidad = repositorioComunidad;
        this.servicioComunidad = servicioComunidad;
    }


    @Override
    public Boolean reproducirCancion(String token, Long idComunidad, Long idUsuario) throws Exception {
        try {
            SpotifyApi spotifyApi = servicioInstancia.obtenerInstanciaDeSpotifyConToken(token);
            Usuario usuario = repositorioComunidad.obtenerUsuarioEnComunidad(idUsuario, idComunidad);

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

            StartResumeUsersPlaybackRequest star = spotifyApi.startResumeUsersPlayback()
                    .uris(jsonArray)
                    .position_ms(0)
                    .build();
            star.execute();

            System.out.println("Canciones entro");

           reproduccionDelUsuario.put(usuario.getUser(),playlists.get(0).getNombre());

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public CancionDto obtenerCancionSonandoEnLaComunidad(Long idComunidad) throws IOException, ParseException, SpotifyWebApiException {
        List<String> usuariosActivos = servicioComunidad.obtenerTodosLosUsuariosActivosDeUnaComunidad(idComunidad);

        if (usuariosActivos == null || usuariosActivos.isEmpty()) {
            return null; // No hay usuarios activos
        }

        for (String usuario : usuariosActivos) {
            boolean usuarioInvalido = usuario == null || usuario.trim().isEmpty();
            boolean noEscuchaNada = reproduccionDelUsuario.get(usuario) == null;

            if (usuarioInvalido || noEscuchaNada) {
                continue; // Ignorar usuarios inválidos o sin reproducción
            }

            try {
                String token = repositorioComunidad
                        .obtenerTokenDelUsuarioQuePerteneceAUnaComunidad(usuario, idComunidad);

                SpotifyApi spotifyApi = servicioInstancia
                        .obtenerInstanciaDeSpotifyConToken(token);

                CurrentlyPlaying playing = spotifyApi.getUsersCurrentlyPlayingTrack()
                        .build()
                        .execute();

                if (playing != null && playing.getItem() instanceof Track) {
                    Track track = (Track) playing.getItem();

                    CancionDto cancionDto = new CancionDto();
                    cancionDto.setArtista(track.getArtists()[0].getName());
                    cancionDto.setTitulo(track.getName());
                    cancionDto.setUrlImagen(track.getAlbum().getImages()[0].getUrl());
                    cancionDto.setDuracion(track.getDurationMs());
                    cancionDto.setProgreso(playing.getProgress_ms());

                    return cancionDto;
                }

            } catch (Exception e) {
                // Podés loguear el error para debugging, pero no frenar el bucle por una sola falla
                System.err.println("Error al obtener canción de usuario " + usuario + ": " + e.getMessage());
            }
        }

        return null; // Ningún usuario tenía una canción activa

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
