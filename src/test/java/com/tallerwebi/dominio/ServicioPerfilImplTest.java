package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import se.michaelthelin.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicioPerfilImplTest {
    private ServicioInstancia servicioSpotify;
    private ServicioPerfil perfil;
    private SpotifyApi spotifyApi;
    private GetCurrentUsersProfileRequest.Builder profileRequestBuilder;
    private GetCurrentUsersProfileRequest profileRequest;
    private RepositorioUsuarioImpl repositorioUsuarioImpl;
    @BeforeEach
    public void setUp() {
        servicioSpotify = mock(ServicioInstancia.class);
        perfil = new ServicioPerfilImpl(servicioSpotify);
        repositorioUsuarioImpl = mock(RepositorioUsuarioImpl.class);
        perfil = new ServicioPerfilImpl(servicioSpotify, repositorioUsuarioImpl);
        spotifyApi = mock(SpotifyApi.class);
        profileRequestBuilder = mock(GetCurrentUsersProfileRequest.Builder.class);
        profileRequest = mock(GetCurrentUsersProfileRequest.class);
    }

    @Test
    public void testObtenerPerfilUsuario() throws Exception {
        String token = "token123";
        String refreshToken = "refresh123";

        User mockUser = mock(User.class);

        // Simulamos que cuando se pide una instancia de SpotifyApi con los tokens,
        // el mock servicioSpotify devuelve el mock spotifyApi.
        // Es decir, estamos "inyectando" spotifyApi para que se use en el metodo probado.
        when(servicioSpotify.obtenerInstanciaDeSpotifyConToken(token)).thenReturn(spotifyApi);

// Simulamos que al llamar al metodo getCurrentUsersProfile() del spotifyApi,
// devuelve un mock del builder de la solicitud (request builder).
// Esto permite controlar el siguiente paso en la cadena de llamadas.
        when(spotifyApi.getCurrentUsersProfile()).thenReturn(profileRequestBuilder);

// Simulamos que cuando el builder (profileRequestBuilder) ejecuta build(),
// devuelve un mock del request real (profileRequest) que obtiene información un usuario de Spotify.
// Esto permite encadenar la llamada para luego ejecutar el request mockeado.
        when(profileRequestBuilder.build()).thenReturn(profileRequest);

// Simulamos que cuando se ejecuta el metodo execute() del request (profileRequest),
// devuelve el usuario mockeado (mockUser).
// la respuesta final esperada del API Spotify en este flujo.
        when(profileRequest.execute()).thenReturn(mockUser);


        // Ejecutamos el metodo real
        User userObtenido = perfil.obtenerPerfilUsuario(token, refreshToken);

        // Verificaciones
        assertNotNull(userObtenido);
        assertEquals(mockUser, userObtenido);

        // Verificar que se llamó correctamente
        verify(servicioSpotify).obtenerInstanciaDeSpotifyConToken(token);
        verify(spotifyApi).getCurrentUsersProfile();
        verify(profileRequestBuilder).build();
        verify(profileRequest).execute();
    }

    @Test
    public void testObtenerMejoresArtistas() throws Exception {
        String token = "token123";
        String refreshToken = "refresh123";

        // Mocks necesarios
        SpotifyApi spotifyApi = mock(SpotifyApi.class);
        //Clase Builder para crear un GetUsersTopArtistsRequest.

        GetUsersTopArtistsRequest.Builder requestBuilder = mock(GetUsersTopArtistsRequest.Builder.class);
        // para obtener los mejores artistas del usuario
        GetUsersTopArtistsRequest getUsersTopArtistsRequest = mock(GetUsersTopArtistsRequest.class);

        Paging<Artist> paging = mock(Paging.class);

        Artist artista1 = mock(Artist.class);
        Artist artista2 = mock(Artist.class);
        Artist[] artistasArray = new Artist[]{artista1, artista2};

        // Configuramos el mock para que devuelva la instancia de spotifyApi con los tokens
        when(servicioSpotify.obtenerInstanciaDeSpotifyConToken(token)).thenReturn(spotifyApi);

        // Configuramos la cadena de llamadas encadenadas
        when(spotifyApi.getUsersTopArtists()).thenReturn(requestBuilder);
        when(requestBuilder.limit(10)).thenReturn(requestBuilder);
        when(requestBuilder.build()).thenReturn(getUsersTopArtistsRequest);

        // Simulamos que la ejecución del request devuelve el paging mockeado con artistas
        when(getUsersTopArtistsRequest.execute()).thenReturn(paging);
        when(paging.getItems()).thenReturn(artistasArray);

        // Ejecutamos el metodo real que queremos probar
        List<Artist> resultado = perfil.obtenerMejoresArtistasDelUsuario(token, refreshToken);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(artista1));
        assertTrue(resultado.contains(artista2));

        // Verificar que se invocaron los métodos esperados
        verify(servicioSpotify).obtenerInstanciaDeSpotifyConToken(token);
        verify(spotifyApi).getUsersTopArtists();
        verify(requestBuilder).limit(10);
        verify(requestBuilder).build();
        verify(getUsersTopArtistsRequest).execute();
        verify(paging).getItems();
    }
    @Test
    public void testObtenerNombreDePlaylistDelUsuario() throws Exception {
        String token = "token123";
        String refreshToken = "refresh123";

        // Mocks necesarios
        SpotifyApi spotifyApi = mock(SpotifyApi.class);
        //Clase para crear una GetListOfCurrentUsersPlaylistsRequest
        GetListOfCurrentUsersPlaylistsRequest.Builder requestBuilder = mock(GetListOfCurrentUsersPlaylistsRequest.Builder.class);
        // Obtén una lista de las listas de reproducción que posee o sigue el usuario actual de Spotify
        GetListOfCurrentUsersPlaylistsRequest request = mock(GetListOfCurrentUsersPlaylistsRequest.class);

        //respuesta paginada con objetos del tipo PlaylistSimplified
        Paging<PlaylistSimplified> paging = mock(Paging.class);


        //Recupere información sobre los objetos Playlist simplificados
        PlaylistSimplified playlist1 = mock(PlaylistSimplified.class);
        PlaylistSimplified playlist2 = mock(PlaylistSimplified.class);
        PlaylistSimplified[] playlistsArray = new PlaylistSimplified[]{playlist1, playlist2};

        // Configuramos mocks para devolver la instancia de SpotifyApi con tokens
        when(servicioSpotify.obtenerInstanciaDeSpotifyConToken(token)).thenReturn(spotifyApi);

        // Configuramos la cadena de llamadas
        when(spotifyApi.getListOfCurrentUsersPlaylists()).thenReturn(requestBuilder);
        when(requestBuilder.build()).thenReturn(request);
        when(request.execute()).thenReturn(paging);
        when(paging.getItems()).thenReturn(playlistsArray);

        // Ejecutamos el método real
        List<PlaylistSimplified> resultado = perfil.obtenerNombreDePlaylistDelUsuario(token, refreshToken);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(playlist1));
        assertTrue(resultado.contains(playlist2));

        // Verificar que se llamaron los métodos esperados
        verify(servicioSpotify).obtenerInstanciaDeSpotifyConToken(token);
        verify(spotifyApi).getListOfCurrentUsersPlaylists();
        verify(requestBuilder).build();
        verify(request).execute();
        verify(paging).getItems();
    }

    @Test
    public void testObtenerCantidadDePlaylistDelUsuario() throws Exception {
        String token = "token123";
        String refreshToken = "refresh123";

        // Mocks necesarios
        SpotifyApi spotifyApi = mock(SpotifyApi.class);
        GetListOfCurrentUsersPlaylistsRequest.Builder requestBuilder = mock(GetListOfCurrentUsersPlaylistsRequest.Builder.class);
        GetListOfCurrentUsersPlaylistsRequest request = mock(GetListOfCurrentUsersPlaylistsRequest.class);
        Paging<PlaylistSimplified> paging = mock(Paging.class);

        // Configuración de valores
        int cantidadEsperada = 15;

        // Simulación de comportamiento
        when(servicioSpotify.obtenerInstanciaDeSpotifyConToken(token)).thenReturn(spotifyApi);
        when(spotifyApi.getListOfCurrentUsersPlaylists()).thenReturn(requestBuilder);
        when(requestBuilder.build()).thenReturn(request);
        when(request.execute()).thenReturn(paging);
        when(paging.getTotal()).thenReturn(cantidadEsperada);

        // Llamada al metodo real
        Integer cantidadObtenida = perfil.obtenerCantidadDePlaylistDelUsuario(token, refreshToken);

        // Verificaciones
        assertNotNull(cantidadObtenida);
        assertEquals(cantidadEsperada, cantidadObtenida);

        // Verificar interacciones
        verify(servicioSpotify).obtenerInstanciaDeSpotifyConToken(token);
        verify(spotifyApi).getListOfCurrentUsersPlaylists();
        verify(requestBuilder).build();
        verify(request).execute();
        verify(paging).getTotal();
    }

    @Test
    public void testObtenerReproduccionActualDelUsuario() throws Exception {
        String token = "token123";
        String refreshToken = "refresh123";

        // Mocks
        SpotifyApi spotifyApi = mock(SpotifyApi.class);
        GetUsersCurrentlyPlayingTrackRequest request = mock(GetUsersCurrentlyPlayingTrackRequest.class);
        GetUsersCurrentlyPlayingTrackRequest.Builder requestBuilder = mock(GetUsersCurrentlyPlayingTrackRequest.Builder.class);
        CurrentlyPlaying currentlyPlaying = mock(CurrentlyPlaying.class);
        Track mockTrack = mock(Track.class);

        // Configurar comportamiento simulado
        when(servicioSpotify.obtenerInstanciaDeSpotifyConToken(token)).thenReturn(spotifyApi);
        when(spotifyApi.getUsersCurrentlyPlayingTrack()).thenReturn(requestBuilder);
        when(requestBuilder.build()).thenReturn(request);
        when(request.execute()).thenReturn(currentlyPlaying);
        when(currentlyPlaying.getItem()).thenReturn(mockTrack);

        // Ejecutar metodo real
        Track trackObtenido = perfil.obtenerReproduccionActualDelUsuario(token, refreshToken);

        // Verificaciones
        assertNotNull(trackObtenido);
        assertEquals(mockTrack, trackObtenido);

        verify(servicioSpotify).obtenerInstanciaDeSpotifyConToken(token);
        verify(spotifyApi).getUsersCurrentlyPlayingTrack();
        verify(requestBuilder).build();
        verify(request).execute();

    }

    @Test
    public void obtenerEstadoDeAnimoDelUsuario() throws Exception {
        String token = "token123";
        String refreshToken = "refresh123";
        String spotifyUserID = "1";
        EstadoDeAnimo estadoDeAnimo = new EstadoDeAnimo("Feliz", 0.5f, 0.5f, 0.5f, 0.5f);

        when(servicioSpotify.obtenerInstanciaDeSpotifyConToken(token, refreshToken)).thenReturn(spotifyApi);

        when(spotifyApi.getCurrentUsersProfile()).thenReturn(profileRequestBuilder);
        when(profileRequestBuilder.build()).thenReturn(profileRequest);
        User userMock = mock(User.class);
        when(profileRequest.execute()).thenReturn(userMock);

        when(userMock.getId()).thenReturn(spotifyUserID);

        Usuario usuarioMock = mock(Usuario.class);
        when(repositorioUsuarioImpl.buscarUsuarioPorSpotifyID(spotifyUserID)).thenReturn(usuarioMock);
        when(usuarioMock.getEstadoDeAnimo()).thenReturn(estadoDeAnimo);

        EstadoDeAnimo resultado = perfil.obtenerEstadoDeAnimoDelUsuario(token, refreshToken);

        assertNotNull(resultado);
        assertEquals("Feliz", estadoDeAnimo.getNombre());
    }

    @Test
    public void actualizarEstadoDeAnimoDelUsuario() throws Exception {
        String token = "token123";
        String refreshToken = "refresh123";
        EstadoDeAnimo estadoDeAnimo = new EstadoDeAnimo("Feliz", 0.5f, 0.5f, 0.5f, 0.5f);

        when(servicioSpotify.obtenerInstanciaDeSpotifyConToken(token, refreshToken)).thenReturn(spotifyApi);
        when(spotifyApi.getCurrentUsersProfile()).thenReturn(profileRequestBuilder);
        when(profileRequestBuilder.build()).thenReturn(profileRequest);

        User userMock = mock(User.class);
        when(profileRequest.execute()).thenReturn(userMock);
        when(userMock.getId()).thenReturn("1");

        Usuario usuarioMock = mock(Usuario.class);
        when(repositorioUsuarioImpl.buscarUsuarioPorSpotifyID("1")).thenReturn(usuarioMock);

        perfil.actualizarEstadoDeAnimoUsuario(token, refreshToken, estadoDeAnimo);

        verify(usuarioMock).setEstadoDeAnimo(estadoDeAnimo);
    }

}
