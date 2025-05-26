package com.tallerwebi.dominio;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

public interface ServicioAuth {
    AuthorizationCodeUriRequest login() throws Exception;
    AuthorizationCodeCredentials credentials(String code) throws Exception;
    void guardarUsuario(Usuario usuario);
    User obtenerPerfilUsuario(String token, String refreshToken) throws Exception;
}
