package com.tallerwebi.dominio;

import se.michaelthelin.spotify.SpotifyApi;

public interface ServicioInstancia {
    SpotifyApi obtenerInstanciaDeSpotifyConToken(String accessToken) ;
}
