package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.util.List;

import static junit.framework.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;

public class ServicioRatingTest {

    private ServicioRatingImpl servicioRating;

    private RepositorioRating repositorioRating;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioCancion repositorioCancion;
    private ServicioInstancia servicioInstancia;

    private SpotifyApi spotifyApi;
    private User user;

    @BeforeEach
    public void setUp() throws Exception {
        repositorioRating = mock(RepositorioRating.class);
        repositorioUsuario = mock(RepositorioUsuario.class);
        repositorioCancion = mock(RepositorioCancion.class);
        servicioInstancia = mock(ServicioInstancia.class);
        spotifyApi = mock(SpotifyApi.class);
        user = mock(User.class);

        servicioRating = new ServicioRatingImpl(repositorioRating, repositorioUsuario, repositorioCancion, servicioInstancia);
    }

    @Test
    public void queSeGuardeUnRatingNuevoCorrectamente() throws Exception {
        String token = "fake-token";
        Usuario usuarioMock = new Usuario();
        usuarioMock.setSpotifyID("usuario123");
        Cancion cancionMock = null;

        GetCurrentUsersProfileRequest.Builder builder = mock(GetCurrentUsersProfileRequest.Builder.class);
        GetCurrentUsersProfileRequest request = mock(GetCurrentUsersProfileRequest.class);

        when(servicioInstancia.obtenerInstanciaDeSpotifyConToken(token)).thenReturn(spotifyApi);
        when(spotifyApi.getCurrentUsersProfile()).thenReturn(builder);
        when(builder.build()).thenReturn(request);
        when(request.execute()).thenReturn(user);
        when(user.getId()).thenReturn("usuario123");
        when(repositorioUsuario.buscarUsuarioPorSpotifyID("usuario123")).thenReturn(usuarioMock);
        when(repositorioCancion.buscarCancionPorElIdDeSpotify(anyString())).thenReturn(cancionMock);

        servicioRating.guardarRating(token, "spotifyId", "titulo", "artista", "url", "uri", 4);

        verify(repositorioCancion).guardarCancion(any(Cancion.class));
        verify(repositorioRating).guardarRating(any(Rating.class));
    }


    @Test
    public void queActualiceUnRatingExistente() throws Exception {
        String token = "fake-token";
        String spotifyId = "123";
        String titulo = "Cancion Test";
        String artista = "Artista Test";
        String urlImagen = "http://imagen";
        String uri = "spotify:uri";
        Integer puntaje = 3;

        Usuario usuarioMock = new Usuario();
        usuarioMock.setSpotifyID("usuario123");
        Cancion cancionMock = new Cancion();
        Rating ratingMock = new Rating(usuarioMock, cancionMock, 2);

        GetCurrentUsersProfileRequest.Builder builder = mock(GetCurrentUsersProfileRequest.Builder.class);
        GetCurrentUsersProfileRequest request = mock(GetCurrentUsersProfileRequest.class);

        when(servicioInstancia.obtenerInstanciaDeSpotifyConToken(token)).thenReturn(spotifyApi);
        when(spotifyApi.getCurrentUsersProfile()).thenReturn(builder);
        when(builder.build()).thenReturn(request);
        when(request.execute()).thenReturn(user);
        when(user.getId()).thenReturn("usuario123");

        when(repositorioUsuario.buscarUsuarioPorSpotifyID("usuario123")).thenReturn(usuarioMock);
        when(repositorioCancion.buscarCancionPorElIdDeSpotify(spotifyId)).thenReturn(cancionMock);
        when(repositorioRating.buscarPorUsuarioYCancion(usuarioMock, cancionMock)).thenReturn(ratingMock);

        servicioRating.guardarRating(token, spotifyId, titulo, artista, urlImagen, uri, puntaje);

        assertEquals(puntaje, ratingMock.getPuntaje()); // puntaje actualizado a 3
        verify(repositorioRating).guardarRating(ratingMock);
    }

    @Test
    public void queRetorneRatingVacioSiNoExisteElUsuario() {
        String spotifyId = "spotify-id";

        when(repositorioUsuario.buscarUsuarioPorSpotifyID(spotifyId)).thenReturn(null);

        List<Rating> resultado = servicioRating.obtenerRating(spotifyId);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void queRetorneRatingsSiUsuarioExiste() {
        String spotifyId = "spotify-id";

        Usuario usuarioMock = new Usuario();
        List<Rating> ratingsMock = List.of(new Rating());

        when(repositorioUsuario.buscarUsuarioPorSpotifyID(spotifyId)).thenReturn(usuarioMock);
        when(repositorioRating.obtenerRating(usuarioMock)).thenReturn(ratingsMock);

        List<Rating> resultado = servicioRating.obtenerRating(spotifyId);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(ratingsMock, resultado);
    }


}
