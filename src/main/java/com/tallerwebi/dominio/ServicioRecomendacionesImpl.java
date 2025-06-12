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

@Transactional
@Service
public class ServicioRecomendacionesImpl implements ServicioRecomendaciones {

    private ServicioSpotify servicioSpotify;

    @Autowired
    public ServicioRecomendacionesImpl(ServicioSpotify servicioSpotify){
        this.servicioSpotify = servicioSpotify;
    }

    /*
    @Override
    public List<Track> generarRecomendaciones(EstadoDeAnimo estado, String token, String refreshToken, List<String> seedArtistsIds, List<String>seedTracksIds) {

        SpotifyApi spotifyApi = servicioSpotify.obtenerInstanciaDeSpotifyConToken(token, refreshToken);

        float valence = estado.getValence();
        float danceability = estado.getDanceability();
        float energy = estado.getEnergy();
        float tempo = estado.getTempo();

        String seedArtists = String.join(",", seedArtistsIds.stream().limit(5).collect(Collectors.toList()));
        String seedTracks = String.join(",", seedTracksIds.stream().limit(5).collect(Collectors.toList()));

        GetRecommendationsRequest request = spotifyApi.getRecommendations()
                .limit(20)
                .seed_artists(seedArtists)
                .seed_tracks(seedTracks)
                .target_valence(valence)
                .target_danceability(danceability)
                .target_energy(energy)
                .target_tempo(tempo)
                .build();

        try{
            Recommendations recommendations = request.execute();
            List<Track> tracks = Arrays.stream(recommendations.getTracks())
                    .filter(t -> t.getPopularity() != null && t.getPopularity() >= 50)
                    .collect(Collectors.toList());
            return tracks;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    */

    @Override
    public List<Track> generarRecomendaciones(String token, String refreshToken) throws Exception {
        SpotifyApi spotifyApi = servicioSpotify.obtenerInstanciaDeSpotifyConToken(token, refreshToken);
        System.out.println("CHECK SERVICIO !!!!!!!!!!");
        GetUsersTopTracksRequest getUsersTopTracksRequest = spotifyApi.getUsersTopTracks().limit(10).build();
        Paging <Track> topTracks = getUsersTopTracksRequest.execute();
        List<Track> mejores = new ArrayList<>();
        mejores.addAll(Arrays.asList(topTracks.getItems()));
        return mejores;
    }

}

