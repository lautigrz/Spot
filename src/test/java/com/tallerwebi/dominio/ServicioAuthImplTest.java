package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.enums.AuthorizationScope;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ServicioAuthImplTest {
/*
    private SpotifyApi mockSpotifyApi;

    // objeto que usa patron Builder para construir paso a paso el objeto
    private AuthorizationCodeUriRequest.Builder mockUriBuilder;

    //objeto final
    private AuthorizationCodeUriRequest mockUriRequest;

    // objeto que representa el resultado de autenticacion
    private AuthorizationCodeCredentials mockCredentials;

    private AuthorizationCodeRequest mockRequest;

    private AuthorizationCodeRequest.Builder mockRequestBuilder;

    private ServicioAuthImpl servicio;

    @BeforeEach
    public void setUp() {
        mockSpotifyApi = mock(SpotifyApi.class);
        mockUriBuilder = mock(AuthorizationCodeUriRequest.Builder.class);
        mockUriRequest = mock(AuthorizationCodeUriRequest.class);
        mockCredentials = mock(AuthorizationCodeCredentials.class);
        mockRequest = mock(AuthorizationCodeRequest.class);
        mockRequestBuilder = mock(AuthorizationCodeRequest.Builder.class);

        // instancia del servicio pasando el mock
        servicio = new ServicioAuthImpl(mockSpotifyApi);
    }

    @Test
    public void debeConstruirLaUrlDeAutorizacion() throws Exception {
        AuthorizationScope[] scopes = {
                AuthorizationScope.USER_READ_PRIVATE,AuthorizationScope.USER_READ_EMAIL, AuthorizationScope.USER_READ_PLAYBACK_STATE,
                AuthorizationScope.PLAYLIST_MODIFY_PUBLIC,
                AuthorizationScope.STREAMING, AuthorizationScope.USER_FOLLOW_READ,
                AuthorizationScope.USER_TOP_READ, AuthorizationScope.USER_READ_RECENTLY_PLAYED

        };

        // Configuramos mocks en cadena (builder pattern)
        when(mockSpotifyApi.authorizationCodeUri()).thenReturn(mockUriBuilder);

        // le pasamaos las autorizaciones
        when(mockUriBuilder.scope(scopes)).thenReturn(mockUriBuilder);

        // el usuario vera las autorizaciones em la pantalla de autenticacion
        when(mockUriBuilder.show_dialog(true)).thenReturn(mockUriBuilder);

        // terminamos de construir el objeto
        when(mockUriBuilder.build()).thenReturn(mockUriRequest);


        AuthorizationCodeUriRequest autorizacion = servicio.login();

        // el objeto retornado por login()
        // debe ser exactamente el que se esperaba
        // (el mock que se preparo con .build()).
        assertThat(autorizacion, is(equalTo(mockUriRequest)));
    }

    @Test
    public void debeCompletarElFlujoDeAutenticacion() throws Exception {
        // 1. Definimos un código de autorización simulado recibido del usuario o del flujo OAuth
        String code = "spot12";

        //   Cuando se llama al metodo authorizationCode(code) en el mock de SpotifyApi

        //    devuelve un builder para construir la petición de autorización,
        //    entonces devolvemos el mock del builder 'mockRequestBuilder'
        when(mockSpotifyApi.authorizationCode(code)).thenReturn(mockRequestBuilder);

        // 3. Cuando se llama al metodo 'build()' en el builder (para construir la solicitud),
        //    entonces devolvemos nuestro mock de la solicitud 'mockRequest'
        when(mockRequestBuilder.build()).thenReturn(mockRequest);

        // 4. Cuando se ejecuta la solicitud con 'execute()', se simula que devuelve
        //    el objeto 'AuthorizationCodeCredentials' que contiene los datos de autenticación
        when(mockRequest.execute()).thenReturn(mockCredentials);

        // 5. Llamamos al metodo real que queremos probar
        // el cual hace toda esa cadena de llamadas
        AuthorizationCodeCredentials autorizacion = servicio.credentials(code);

        // 6. Verificamos que el resultado retornado es el mismo objeto mockeado que definimos en el paso 4
        assertThat(autorizacion, is(equalTo(mockCredentials)));

        // verifica si llamo a cada metodo
        verify(mockSpotifyApi).authorizationCode(code);
        verify(mockRequestBuilder).build();
        verify(mockRequest).execute();

    }

 */



}
