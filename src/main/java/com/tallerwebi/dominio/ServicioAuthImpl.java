package com.tallerwebi.dominio;

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

    @Autowired
    public ServicioAuthImpl(SpotifyApi spotifyApi) {
        this.spotifyApi = spotifyApi;
    }


    @Override
    public AuthorizationCodeUriRequest login() throws Exception {
        //user-read-email user-read-playback-state"
        AuthorizationCodeUriRequest uriRequest = spotifyApi.authorizationCodeUri()
                .scope(AuthorizationScope.USER_READ_PRIVATE,AuthorizationScope.USER_READ_EMAIL, AuthorizationScope.USER_READ_PLAYBACK_STATE,
                        AuthorizationScope.PLAYLIST_MODIFY_PUBLIC,
                        AuthorizationScope.STREAMING, AuthorizationScope.USER_FOLLOW_READ,
                        AuthorizationScope.USER_TOP_READ, AuthorizationScope.USER_READ_RECENTLY_PLAYED)
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




}
