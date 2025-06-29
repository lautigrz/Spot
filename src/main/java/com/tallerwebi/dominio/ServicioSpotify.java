package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.CancionDto;
import com.tallerwebi.presentacion.dto.Sincronizacion;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.io.IOException;
import java.util.List;

public interface ServicioSpotify {

    int obtenerPosicionEnMsDeLoQueEscucha(String token) throws IOException, ParseException, SpotifyWebApiException;

    List<CancionDto> obtenerCancionesDeSpotify(String accessToken, String textoBusqueda) throws IOException, ParseException, SpotifyWebApiException;
    CancionDto getCancionDto(Track track);
    String obtenerUriDeLoQueEscucha(String token);
}
