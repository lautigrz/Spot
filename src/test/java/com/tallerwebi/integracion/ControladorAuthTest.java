package com.tallerwebi.integracion;

import com.tallerwebi.dominio.ServicioAuth;

import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.presentacion.ControladorAuth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URI;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;



public class ControladorAuthTest {
 /*
    private AuthorizationCodeUriRequest authorizationCodeUriRequest;

    private HttpServletResponse response;
    private ControladorAuth controladorAuth;
    private AuthorizationCodeCredentials mockCredentials;
    private ServicioAuth servicioAuth;


    @BeforeEach
    public void setup() {
        authorizationCodeUriRequest = mock(AuthorizationCodeUriRequest.class);
        servicioAuth = mock(ServicioAuth.class);
        controladorAuth = new ControladorAuth(servicioAuth);
        response = mock(HttpServletResponse.class);
        mockCredentials = mock(AuthorizationCodeCredentials.class);
    }

    @Test
    public void debeRedirigirASpotify() throws Exception {
        // simulamos una url de redireccion
        URI mockUri = new URI("https://spotify/authorize");

        // cuando se llama a login retorna un objeto que representa una solicitud
        // para construir una URL de autorización de Spotify
        when(servicioAuth.login()).thenReturn(authorizationCodeUriRequest);

        // cuando se llama a getUri() devuelve mockURI, la url simulada
        when(authorizationCodeUriRequest.getUri()).thenReturn(mockUri);
        controladorAuth.login(response);

        // Verificar que se llamó al metodo sendRedirect con la URL correcta
        verify(response).sendRedirect(mockUri.toString());

    }

    @Test
    public void callbackDebeGuardarTokensYRedirigirAlPerfil() throws Exception {
        // simulamos token, refresh token y codigo de autorizacion
        String code = "mockCode";
        String accessToken = "mockAccessToken";
        String refreshToken = "mockRefreshToken";

        // Crear mocks

        HttpSession sessionMock = mock(HttpSession.class);

        // Configurar mocks 'AuthorizationCodeCredentials' que contiene los datos de autenticación
        when(servicioAuth.credentials(anyString())).thenReturn(mockCredentials);
        when(mockCredentials.getAccessToken()).thenReturn(accessToken);
        when(mockCredentials.getRefreshToken()).thenReturn(refreshToken);
        when(servicioAuth.guardarUsuario(anyString(), anyString())).thenReturn("user");

        // Act
        String vista = controladorAuth.callback(code, sessionMock);

        // Assert
        verify(sessionMock).setAttribute("token", accessToken);
        verify(sessionMock).setAttribute("refreshToken", refreshToken);
        assertThat("redirect:/comunidad", equalTo(vista));
    }

  */

}
