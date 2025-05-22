package com.tallerwebi.dominio;

import se.michaelthelin.spotify.SpotifyApi;

public interface ServicioSpotify {
    SpotifyApi obtenerInstanciaDeSpotifyConToken(String accessToken, String refreshToken);
}
