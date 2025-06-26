package com.tallerwebi.dominio;

import com.google.gson.JsonArray;
import com.tallerwebi.presentacion.dto.CancionDto;
import com.tallerwebi.presentacion.dto.Sincronizacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Image;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;
import se.michaelthelin.spotify.requests.data.player.StartResumeUsersPlaybackRequest;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ServicioReproduccionTest {


    private ServicioInstancia servicioInstancia;
    private ServicioSpotify servicioSpotify;
    private ServicioComunidad servicioComunidad;
    private ServicioReproduccion servicioReproduccion;
    private RepositorioComunidad repositorioComunidad;
    private RepositorioUsuarioComunidad repositorioUsuarioComunidad;

    @BeforeEach
    public void setUp() {
        servicioInstancia = mock(ServicioInstancia.class);
        servicioSpotify = mock(ServicioSpotify.class);
        servicioComunidad = mock(ServicioComunidad.class);
        repositorioComunidad = mock(RepositorioComunidad.class);
        repositorioUsuarioComunidad = mock(RepositorioUsuarioComunidad.class);
        servicioReproduccion = new ServicioReproduccionImpl(servicioInstancia, servicioSpotify, repositorioComunidad, servicioComunidad, repositorioUsuarioComunidad);
    }

    @Test
    public void seDebeReproducirUnaCancionParaUnUsuario() throws Exception {

        SpotifyApi spotifyApiMock = mock(SpotifyApi.class);
        StartResumeUsersPlaybackRequest requestMock = mock(StartResumeUsersPlaybackRequest.class);
        StartResumeUsersPlaybackRequest.Builder builderMock = mock(StartResumeUsersPlaybackRequest.Builder.class);


        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUser("lauti");
        usuario.setUrlFoto("http://fds");

        Comunidad comunidad = new Comunidad();
        comunidad.setId(2L);
        comunidad.setNombre("Rock");
        comunidad.setDescripcion("descripcion");

        UsuarioComunidad usuarioComunidad = new UsuarioComunidad();
        usuarioComunidad.setId(3L);
        usuarioComunidad.setUsuario(usuario);
        usuarioComunidad.setComunidad(comunidad);


        when(servicioInstancia.obtenerInstanciaDeSpotifyConToken("fake-token")).thenReturn(spotifyApiMock);


        when(repositorioUsuarioComunidad.obtenerUsuarioEnComunidad(1L, 1L)).thenReturn(usuarioComunidad);


        Cancion cancionMock = mock(Cancion.class);
        when(cancionMock.getUri()).thenReturn("spotify:track:123");

        Set<Cancion> canciones = new HashSet<>();
        canciones.add(cancionMock);


        Playlist playlistMock = mock(Playlist.class);
        when(playlistMock.getCanciones()).thenReturn(canciones);
        when(playlistMock.getNombre()).thenReturn("Mi Playlist");

        List<Playlist> playlists = new ArrayList<>();
        playlists.add(playlistMock);
        when(repositorioComunidad.obtenerPlaylistsPorComunidadId(1L)).thenReturn(playlists);


        when(spotifyApiMock.startResumeUsersPlayback()).thenReturn(builderMock);
        when(builderMock.uris(any(JsonArray.class))).thenReturn(builderMock);
        when(builderMock.position_ms(0)).thenReturn(builderMock);
        when(builderMock.build()).thenReturn(requestMock);
        when(requestMock.execute()).thenReturn(null); // ejecuta sin error


        Boolean resultado = servicioReproduccion.reproducirCancion("fake-token", 1L, 1L);


        assertThat(resultado, equalTo(true));
        verify(servicioInstancia).obtenerInstanciaDeSpotifyConToken("fake-token");
        verify(spotifyApiMock).startResumeUsersPlayback();
        verify(requestMock).execute();
    }



    @Test
    public void debeTirarUnaExcepcionAlConsultarPorUnaPlaylistDeLaComunidad(){
        Usuario usuario = new Usuario();
        usuario.setUser("lauti");

        UsuarioComunidad uc = new UsuarioComunidad();
        uc.setUsuario(usuario);

        when(repositorioUsuarioComunidad.obtenerUsuarioEnComunidad(anyLong(), anyLong())).thenReturn(uc);
        when(repositorioComunidad.obtenerPlaylistsPorComunidadId(anyLong())).thenReturn(null); // <--- playlists = null

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> servicioReproduccion.reproducirCancion("token", 1L, 1L)
        );

        assertEquals("La comunidad no tiene playlists.", ex.getMessage());



    }

    @Test
    public void debeRetornarNullAlObtenerUnaCancionSonandoEnUnaComunidad() throws Exception {
        List<String> usuarios = new ArrayList<>();

        when(servicioComunidad.obtenerTodosLosUsuariosActivosDeUnaComunidad(anyLong())).thenReturn(usuarios);

        CancionDto cancionDto = servicioReproduccion.obtenerCancionSonandoEnLaComunidad(1L);

        assertThat(cancionDto, equalTo(null));

    }

    @Test
    public void debeRetornarLaCancionSonandoEnUnaComunidad() throws Exception {

        List<String> usuarios = new ArrayList<>(List.of("lautigrz"));
        String token = "token";
        SpotifyApi spotifyApiMock = mock(SpotifyApi.class);
        CurrentlyPlaying playing = mock(CurrentlyPlaying.class);
        GetUsersCurrentlyPlayingTrackRequest getUsersCurrentlyPlayingMock = mock(GetUsersCurrentlyPlayingTrackRequest.class);
        GetUsersCurrentlyPlayingTrackRequest.Builder builderMock = mock(GetUsersCurrentlyPlayingTrackRequest.Builder.class);

        Track trackMock = mock(Track.class);
        ArtistSimplified artistMock = mock(ArtistSimplified.class);
        AlbumSimplified albumMock = mock(AlbumSimplified.class);
        Image imageMock = mock(Image.class);


        ServicioReproduccionImpl.reproduccionDelUsuario.put("lautigrz", "algunaPlaylist");

        when(servicioComunidad.obtenerTodosLosUsuariosActivosDeUnaComunidad(anyLong())).thenReturn(usuarios);

        when(repositorioComunidad.obtenerTokenDelUsuarioQuePerteneceAUnaComunidad(anyString(), anyLong())).thenReturn(token);

        when(servicioInstancia.obtenerInstanciaDeSpotifyConToken(anyString())).thenReturn(spotifyApiMock);
        when(spotifyApiMock.getUsersCurrentlyPlayingTrack()).thenReturn(builderMock);
        when(spotifyApiMock.getUsersCurrentlyPlayingTrack().build()).thenReturn(getUsersCurrentlyPlayingMock);
        when(getUsersCurrentlyPlayingMock.execute()).thenReturn(playing);

        // Mock del Track
        when(playing.getItem()).thenReturn(trackMock);
        when(playing.getProgress_ms()).thenReturn(123456);

        when(trackMock.getName()).thenReturn("Nombre de Canción");
        when(trackMock.getDurationMs()).thenReturn(180000);

        // Artista
        when(trackMock.getArtists()).thenReturn(new ArtistSimplified[]{artistMock});
        when(artistMock.getName()).thenReturn("Billie");

        // Álbum e imagen
        when(trackMock.getAlbum()).thenReturn(albumMock);
        when(albumMock.getImages()).thenReturn(new Image[]{imageMock});
        when(imageMock.getUrl()).thenReturn("http://imagen.com/img.jpg");

        CancionDto cancionDto = servicioReproduccion.obtenerCancionSonandoEnLaComunidad(1L);

        assertNotNull(cancionDto);
        assertThat(cancionDto.getArtista(), equalTo("Billie"));
        assertThat(cancionDto.getDuracion(), equalTo(180000));
    }

    @Test
    public void seDebeObtenerUnaSincronizacion() throws Exception {
        Set<Cancion> canciones = new HashSet<>();
        Cancion cancion = new Cancion();
        cancion.setUri("uri/ghffd");
        cancion.setArtista("billie");
        canciones.add(cancion);
        String token = "token";

        String uriFake ="spotify:track:sdada12";
        Playlist playlistComunidad = mock(Playlist.class);

        SpotifyApi spotifyApiMock = mock(SpotifyApi.class);
        CurrentlyPlaying playing = mock(CurrentlyPlaying.class);
        GetUsersCurrentlyPlayingTrackRequest getUsersCurrentlyPlayingMock = mock(GetUsersCurrentlyPlayingTrackRequest.class);
        GetUsersCurrentlyPlayingTrackRequest.Builder builderMock = mock(GetUsersCurrentlyPlayingTrackRequest.Builder.class);
        IPlaylistItem iPlaylistItem = mock(IPlaylistItem.class);
        when(repositorioComunidad.obtenerTokenDelUsuarioQuePerteneceAUnaComunidad(anyString(), anyLong())).thenReturn(token);
        when(repositorioComunidad.obtenerCancionesDeUnaPlaylistDeUnaComunidad(anyLong())).thenReturn(canciones);
        when(servicioInstancia.obtenerInstanciaDeSpotifyConToken(anyString())).thenReturn(spotifyApiMock);
        when(spotifyApiMock.getUsersCurrentlyPlayingTrack()).thenReturn(builderMock);
        when(spotifyApiMock.getUsersCurrentlyPlayingTrack().build()).thenReturn(getUsersCurrentlyPlayingMock);
        when(getUsersCurrentlyPlayingMock.execute()).thenReturn(playing);

        when(playing.getItem()).thenReturn(iPlaylistItem);
        when(playing.getItem().getUri()).thenReturn(uriFake);
        when(playing.getProgress_ms()).thenReturn(100);

        when(servicioSpotify.obtenerUriDeLoQueEscucha(anyString())).thenReturn(uriFake);
        when(servicioSpotify.obtenerPosicionEnMsDeLoQueEscucha(anyString())).thenReturn(100);

        when(repositorioComunidad.obtenerPlaylistDeUnaComunidad(anyLong())).thenReturn(playlistComunidad);
        when(repositorioComunidad.obtenerPlaylistDeUnaComunidad(anyLong()).getNombre()).thenReturn("prueba");

        Sincronizacion sincronizacion = servicioReproduccion.obtenerSincronizacion("lauti", 2L);

        assertNotNull(sincronizacion);
        assertThat(sincronizacion.getUris(), containsInAnyOrder(cancion.getUri()));
        assertThat(sincronizacion.getPositionMs(), equalTo(1000));
        assertThat(sincronizacion.getUriInicio(), equalTo(uriFake));


    }


}




