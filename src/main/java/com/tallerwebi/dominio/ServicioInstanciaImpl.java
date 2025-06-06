package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;

@Service
public class ServicioInstanciaImpl implements ServicioInstancia {
    private SpotifyApi spotifyApi;

    @Autowired
    public ServicioInstanciaImpl(SpotifyApi spotifyApi) {
        this.spotifyApi = spotifyApi;
    }
    @Override
    public SpotifyApi obtenerInstanciaDeSpotifyConToken(String accessToken) {
        return new SpotifyApi.Builder()
                .setClientId(this.spotifyApi.getClientId())
                .setClientSecret(this.spotifyApi.getClientSecret())
                .setRedirectUri(this.spotifyApi.getRedirectURI())
                .setAccessToken(accessToken)
                .build();
    }

}
