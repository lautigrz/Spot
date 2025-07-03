package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioAuthImpl;
import com.tallerwebi.presentacion.dto.UsuarioDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.enums.AuthorizationScope;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

@Service
@Transactional
public class ServicioAuthImpl implements ServicioAuth {

    private SpotifyApi spotifyApi;
    private RepositorioAuth repositorioAuth;

    @Autowired
    public ServicioAuthImpl(SpotifyApi spotifyApi, RepositorioAuth repositorioAuth) {
        this.spotifyApi = spotifyApi;
        this.repositorioAuth = repositorioAuth;
    }


    @Override
    public AuthorizationCodeUriRequest login() throws Exception {
        //user-read-email user-read-playback-state"
        AuthorizationCodeUriRequest uriRequest = spotifyApi.authorizationCodeUri()
                .scope(AuthorizationScope.USER_READ_PRIVATE,AuthorizationScope.USER_READ_EMAIL, AuthorizationScope.USER_READ_PLAYBACK_STATE,
                        AuthorizationScope.PLAYLIST_MODIFY_PUBLIC,
                        AuthorizationScope.STREAMING, AuthorizationScope.USER_FOLLOW_READ,
                        AuthorizationScope.USER_TOP_READ, AuthorizationScope.USER_READ_RECENTLY_PLAYED,
                        AuthorizationScope.USER_LIBRARY_READ)
                .show_dialog(true)
                .build();

        return uriRequest;

    }

    @Override
    public AuthorizationCodeCredentials credentials(String code) throws Exception {

        AuthorizationCodeRequest request = spotifyApi.authorizationCode(code).build();

        AuthorizationCodeCredentials autorizacion =  request.execute();
        return autorizacion;
    }

    @Override
    public UsuarioDto guardarUsuario(String token, String refreshToken) throws Exception {

        User user = obtenerPerfilUsuario(token, refreshToken);

        if (user == null) {
            throw new Exception("Error al obtener el usuario");
        }

        // 1️⃣ Buscar usuario existente
        Usuario usuario = repositorioAuth.buscarPorSpotifyID(user.getId());

        // 2️⃣ Si no existe, lo creo
        if (usuario == null) {
            usuario = new Usuario();
            usuario.setSpotifyID(user.getId());
        }

        // 3️⃣ Actualizo o seteo todos los datos relevantes
        usuario.setUser(user.getDisplayName());
        usuario.setToken(token);
        usuario.setRefreshToken(refreshToken);

        if (user.getImages() != null && user.getImages().length > 0) {
            usuario.setUrlFoto(user.getImages()[0].getUrl());
        }

        // 4️⃣ Guardar (insert o update)
        UsuarioDto  usuarioGuardado = repositorioAuth.guardar(usuario);

        return usuarioGuardado;
    }


    @Override
    public User obtenerPerfilUsuario(String token, String refreshToken) throws Exception {
        spotifyApi.setAccessToken(token);
        spotifyApi.setRefreshToken(refreshToken);
        return spotifyApi.getCurrentUsersProfile().build().execute();
    }




}
