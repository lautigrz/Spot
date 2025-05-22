package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.michaelthelin.spotify.SpotifyApi;

@Service
@Transactional
public class ServicioSpotifyImpl implements ServicioSpotify {

    private SpotifyApi spotifyApi;
    @Autowired
    public ServicioSpotifyImpl(SpotifyApi spotifyApi) {
        this.spotifyApi = spotifyApi;
    }

    @Override
    public SpotifyApi obtenerInstanciaDeSpotifyConToken(String accessToken, String refreshToken) {
        return new SpotifyApi.Builder()
                .setClientId(this.spotifyApi.getClientId())
                .setClientSecret(this.spotifyApi.getClientSecret())
                .setRedirectUri(this.spotifyApi.getRedirectURI())
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken)
                .build();
    }
}
