package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.CancionDto;
import com.tallerwebi.presentacion.dto.Sincronizacion;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;

public interface ServicioReproduccion {
    Boolean reproducirCancion(String token, Long idComunidad, Long idUsuario) throws Exception;
    CancionDto obtenerCancionSonandoEnLaComunidad(Long idComunidad) throws IOException, ParseException, SpotifyWebApiException;
    Sincronizacion obtenerSincronizacion(String user, Long idComunidad) throws IOException, ParseException, SpotifyWebApiException;
    CancionDto obtenerCancionActualDeUsuario(Long id)  throws IOException, ParseException, SpotifyWebApiException;
    Boolean estaEscuchandoMusica(String usuario);

}
