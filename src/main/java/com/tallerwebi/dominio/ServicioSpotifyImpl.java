package com.tallerwebi.dominio;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.tallerwebi.presentacion.dto.CancionDto;
import com.tallerwebi.presentacion.dto.Sincronizacion;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.player.StartResumeUsersPlaybackRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class ServicioSpotifyImpl implements ServicioSpotify {



    @Autowired
    private ServicioInstancia servicioInstancia;

    @Autowired
    public ServicioSpotifyImpl(ServicioInstancia servicioInstancia) {
        this.servicioInstancia = servicioInstancia;
    }

    @Override
    public int obtenerPosicionEnMsDeLoQueEscucha(String token) throws IOException, ParseException, SpotifyWebApiException {
        try {
            SpotifyApi spotifyApi = servicioInstancia.obtenerInstanciaDeSpotifyConToken(token);

            CurrentlyPlaying playing = spotifyApi.getUsersCurrentlyPlayingTrack()
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
    public List<CancionDto> obtenerCancionesDeSpotify(String accessToken, String textoBusqueda)
            throws IOException, ParseException, SpotifyWebApiException {

        SpotifyApi spotifyApi = servicioInstancia.obtenerInstanciaDeSpotifyConToken(accessToken);

        SearchTracksRequest cancion = spotifyApi.searchTracks(textoBusqueda).limit(10).build();
        Paging<Track> tracks = cancion.execute();

        List<CancionDto> cancionesDevolver = new ArrayList<>();

        for (Track track : tracks.getItems()) {

            CancionDto cancionDto = getCancionDto(track);

            cancionesDevolver.add(cancionDto);
        }

        return cancionesDevolver;
    }

    @Override
    public CancionDto getCancionDto(Track track) {
        CancionDto cancionDto = new CancionDto();
        cancionDto.setSpotifyId(track.getId());
        cancionDto.setUri(track.getUri());
        cancionDto.setTitulo(track.getName());

        if (track.getArtists().length > 0) {
            cancionDto.setArtista(track.getArtists()[0].getName());
        }

        if (track.getAlbum() != null &&
                track.getAlbum().getImages() != null &&
                track.getAlbum().getImages().length > 0) {
            cancionDto.setUrlImagen(track.getAlbum().getImages()[0].getUrl());
        }
        return cancionDto;
    }


    @Override
    public String obtenerUriDeLoQueEscucha(String token) {
        try {
            SpotifyApi spotifyApi = servicioInstancia.obtenerInstanciaDeSpotifyConToken(token);

            CurrentlyPlaying playing = spotifyApi.getUsersCurrentlyPlayingTrack()
                    .build().execute();

            System.out.println("Position");
           return playing.getItem().getUri();
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al obtener el token: " + e.getMessage());
        }
        return "";
    }

}
