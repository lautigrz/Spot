package com.tallerwebi.dominio;

import antlr.NameSpace;
import com.neovisionaries.i18n.CountryCode;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.data.artists.GetArtistsRelatedArtistsRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistsTopTracksRequest;
import se.michaelthelin.spotify.requests.data.browse.GetRecommendationsRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetAudioFeaturesForTrackRequest;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@Service
public class ServicioRecomendacionesImpl implements ServicioRecomendaciones {

    private final ServicioInstanciaImpl servicioInstanciaImpl;
    private ServicioSpotify servicioSpotify;
    private static final float UMBRAL_ENERGY = 0.3f;
    private static final float UMBRAL_DANCEABILITY = 0.3f;
    private static final float UMBRAL_VALENCE = 0.3f;
    private static final float UMBRAL_TEMPO = 25f;

    @Autowired
    public ServicioRecomendacionesImpl(ServicioSpotify servicioSpotify, ServicioInstanciaImpl servicioInstanciaImpl){
        this.servicioSpotify = servicioSpotify;
        this.servicioInstanciaImpl = servicioInstanciaImpl;
    }

    @Override
    public List<Track> generarRecomendaciones(String token, EstadoDeAnimo estadoDeAnimo) throws Exception {
        SpotifyApi spotifyApi = servicioInstanciaImpl.obtenerInstanciaDeSpotifyConToken(token);

        List<Track> saved = obtenerSavedTracks(spotifyApi);
        List<Track> recent = obtenerRecentlyPlayed(spotifyApi);
        List<Track> top = obtenerTopTracks(spotifyApi);

        List<Track> todasLasCanciones = Stream.of(saved, recent, top)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());

        System.out.println("Total de canciones combinadas: " + todasLasCanciones.size());

        Collections.shuffle(todasLasCanciones);

        return todasLasCanciones.stream()
                .limit(10)
                .collect(Collectors.toList());
    }

    private List<Track> obtenerSavedTracks(SpotifyApi spotifyApi) throws Exception {
        Paging<SavedTrack> savedTracks = spotifyApi.getUsersSavedTracks()
                .limit(50)
                .build()
                .execute();

        List<Track> guardadas = Arrays.stream(savedTracks.getItems())
                .map(SavedTrack::getTrack)
                .collect(Collectors.toList());

        System.out.println("✔️ Tracks guardados: " + guardadas.size());
        guardadas.forEach(t -> System.out.println("Track guardado: " + t.getName()));

        return guardadas;
    }

    private List<Track> obtenerRecentlyPlayed(SpotifyApi spotifyApi) throws Exception {
        PagingCursorbased<PlayHistory> recentlyPlayed = spotifyApi.getCurrentUsersRecentlyPlayedTracks()
                .limit(20)
                .build()
                .execute();

        List<Track> recientes = Arrays.stream(recentlyPlayed.getItems())
                .map(PlayHistory::getTrack)
                .collect(Collectors.toList());

        System.out.println("✔️ Tracks recientes: " + recientes.size());
        recientes.forEach(t -> System.out.println("Track reciente: " + t.getName()));

        return recientes;
    }

    private List<Track> obtenerTopTracks(SpotifyApi spotifyApi) throws Exception {
        Paging<Track> topTracks = spotifyApi.getUsersTopTracks()
                .limit(20)
                .build()
                .execute();

        List<Track> top = Arrays.asList(topTracks.getItems());

        System.out.println("✔️ Top tracks: " + top.size());
        top.forEach(t -> System.out.println("Track top: " + t.getName()));

        return top;
    }

    private boolean coincideConEstadoDeAnimo(AudioFeatures f, EstadoDeAnimo ea) {
        return Math.abs(f.getEnergy() - ea.getEnergy()) <= UMBRAL_ENERGY &&
                Math.abs(f.getDanceability() - ea.getDanceability()) <= UMBRAL_DANCEABILITY &&
                Math.abs(f.getValence() - ea.getValence()) <= UMBRAL_VALENCE &&
                Math.abs(f.getTempo() - ea.getTempo()) <= UMBRAL_TEMPO;
    }

}

