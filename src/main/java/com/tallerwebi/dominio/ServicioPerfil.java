package com.tallerwebi.dominio;

import se.michaelthelin.spotify.model_objects.specification.*;

import java.util.List;

public interface ServicioPerfil {
    User obtenerPerfilUsuario(String token, String refreshToken) throws Exception;
    Integer obtenerCantidadDeArtistaQueSigueElUsuario(String token, String refreshToken) throws Exception;
    List<Artist> obtenerMejoresArtistasDelUsuario(String token, String refreshToken) throws Exception;
    List<PlaylistSimplified> obtenerNombreDePlaylistDelUsuario(String token, String refreshToken) throws Exception;
    Integer obtenerCantidadDePlaylistDelUsuario(String token, String refreshToken) throws Exception;
    Track obtenerReproduccionActualDelUsuario(String token, String refreshToken) throws Exception;
    EstadoDeAnimo obtenerEstadoDeAnimoDelUsuario(String token, String refreshToken) throws Exception;
    void actualizarEstadoDeAnimoUsuario(String token, String refreshToken, EstadoDeAnimo estadoDeAnimo) throws Exception;
}
