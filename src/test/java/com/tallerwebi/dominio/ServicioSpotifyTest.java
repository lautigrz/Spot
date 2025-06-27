package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.CancionDto;
import org.apache.hc.core5.http.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioSpotifyTest {
    private ServicioInstancia servicioInstancia;
    private ServicioSpotify servicioSpotify;
    @BeforeEach
    public void setUp() {
        servicioInstancia = mock(ServicioInstancia.class);
        servicioSpotify = new ServicioSpotifyImpl(servicioInstancia);
    }

    @Test
    public void seDebeobtenerPosicionEnMsDeUnaCancion() throws IOException, ParseException, SpotifyWebApiException {
        SpotifyApi spotifyApi = mock(SpotifyApi.class);
        CurrentlyPlaying playing = mock(CurrentlyPlaying.class);
        GetUsersCurrentlyPlayingTrackRequest getUsersCurrentlyPlayingTrackMock = mock(GetUsersCurrentlyPlayingTrackRequest.class);
        GetUsersCurrentlyPlayingTrackRequest.Builder getUsersCurrentlyPlayingTrackMockBuilder = mock(GetUsersCurrentlyPlayingTrackRequest.Builder.class);

        when(servicioInstancia.obtenerInstanciaDeSpotifyConToken(anyString())).thenReturn(spotifyApi);
        when(spotifyApi.getUsersCurrentlyPlayingTrack()).thenReturn(getUsersCurrentlyPlayingTrackMockBuilder);
        when(spotifyApi.getUsersCurrentlyPlayingTrack().build()).thenReturn(getUsersCurrentlyPlayingTrackMock);
        when(getUsersCurrentlyPlayingTrackMock.execute()).thenReturn(playing);

        when(playing.getProgress_ms()).thenReturn(1000);

        int ms = servicioSpotify.obtenerPosicionEnMsDeLoQueEscucha("fake");

        assertThat(ms,equalTo(1000));

    }

    @Test
    public void seDebeObtenerUnaCancionDto(){
        Track track = mock(Track.class);
        when(track.getId()).thenReturn("las#43fGSa");
        when(track.getUri()).thenReturn("spotify:track:sdaas#DAS1");
        when(track.getName()).thenReturn("Golden");

        ArtistSimplified artistSimplified = mock(ArtistSimplified.class);

        ArtistSimplified[] artistSimplifieds = {artistSimplified};
        AlbumSimplified albumSimplified = mock(AlbumSimplified.class);

        Image image = mock(Image.class);

        Image[] images = {image};

        when(track.getArtists()).thenReturn(artistSimplifieds);
        when(track.getArtists()[0].getName()).thenReturn("Harry");
        when(track.getAlbum()).thenReturn(albumSimplified);
        when(albumSimplified.getImages()).thenReturn(images);

        when(image.getUrl()).thenReturn("https://image/spotify");

        CancionDto cancionDto = servicioSpotify.getCancionDto(track);

        assertThat(cancionDto.getSpotifyId(),equalTo("las#43fGSa"));
        assertThat(cancionDto.getUrlImagen(),equalTo("https://image/spotify"));
        assertThat(cancionDto.getUri(),equalTo("spotify:track:sdaas#DAS1"));
        assertThat(cancionDto.getArtista(),equalTo("Harry"));
        assertThat(cancionDto.getTitulo(),equalTo("Golden"));

    }

    @Test
    public void seDebeObtenerLaUriDeUnaCancionDto() throws IOException, ParseException, SpotifyWebApiException {
        SpotifyApi spotifyApi = mock(SpotifyApi.class);
        CurrentlyPlaying playing = mock(CurrentlyPlaying.class);
        GetUsersCurrentlyPlayingTrackRequest getUsersCurrentlyPlayingTrackMock = mock(GetUsersCurrentlyPlayingTrackRequest.class);
        GetUsersCurrentlyPlayingTrackRequest.Builder getUsersCurrentlyPlayingTrackMockBuilder = mock(GetUsersCurrentlyPlayingTrackRequest.Builder.class);
        IPlaylistItem iPlaylistItem = mock(IPlaylistItem.class);
        when(servicioInstancia.obtenerInstanciaDeSpotifyConToken(anyString())).thenReturn(spotifyApi);
        when(spotifyApi.getUsersCurrentlyPlayingTrack()).thenReturn(getUsersCurrentlyPlayingTrackMockBuilder);
        when(spotifyApi.getUsersCurrentlyPlayingTrack().build()).thenReturn(getUsersCurrentlyPlayingTrackMock);
        when(getUsersCurrentlyPlayingTrackMock.execute()).thenReturn(playing);
        when(playing.getItem()).thenReturn(iPlaylistItem);
        when(playing.getItem().getUri()).thenReturn("spotify:track:sdaas#DAS1");

        String uri = servicioSpotify.obtenerUriDeLoQueEscucha("KlAdE12");

        assertThat(uri,equalTo("spotify:track:sdaas#DAS1"));


    }

    @Test
    public void seDebeDevolverUnaListaDeCancionesBuscadaPorTexto() throws IOException, ParseException, SpotifyWebApiException {
        SpotifyApi spotifyApi = mock(SpotifyApi.class);
        SearchTracksRequest searchTracksRequest = mock(SearchTracksRequest.class);
        SearchTracksRequest.Builder searchTrackBuilder = mock(SearchTracksRequest.Builder.class);
        // Mocks de tracks con datos válidos
        Track track = crearTrackMockeado("123", "Bad Guy", "spotify:track:123", "Billie Eilish");
        Track track1 = crearTrackMockeado("456", "Happier Than Ever", "spotify:track:456", "Billie Eilish");
        Track track2 = crearTrackMockeado("789", "Lovely", "spotify:track:789", "Billie Eilish");
        Track[] trackArray = new Track[] { track, track1, track2 };
        Paging<Track> pagingMock = mock(Paging.class);

        String texto = "billie";


        when(servicioInstancia.obtenerInstanciaDeSpotifyConToken(anyString())).thenReturn(spotifyApi);
        when(spotifyApi.searchTracks(anyString())).thenReturn(searchTrackBuilder);
        when(searchTrackBuilder.limit(anyInt())).thenReturn(searchTrackBuilder);
        when(searchTrackBuilder.build()).thenReturn(searchTracksRequest);
        when(searchTracksRequest.execute()).thenReturn(pagingMock);
        when(pagingMock.getItems()).thenReturn(trackArray);

        List<CancionDto> canciones = servicioSpotify.obtenerCancionesDeSpotify(texto, "tokenFicticio");
        assertThat(3, equalTo(canciones.size()));
        assertThat("Bad Guy", equalTo(canciones.get(0).getTitulo()));
        assertThat("Happier Than Ever", equalTo(canciones.get(1).getTitulo()));
        assertThat("Lovely", equalTo(canciones.get(2).getTitulo()));

    }
    private Track crearTrackMockeado(String spotifyId, String titulo, String uris,String artista) {
        // Mock del track
        Track track = mock(Track.class);

        // Mock del artista
        ArtistSimplified artist = mock(ArtistSimplified.class);
        when(artist.getName()).thenReturn(artista);
        when(track.getArtists()).thenReturn(new ArtistSimplified[] { artist });

        // Mock de la imagen
        Image image = mock(Image.class);
        when(image.getUrl()).thenReturn("https://img.url/cover.jpg");

        // Mock del álbum
        AlbumSimplified album = mock(AlbumSimplified.class);
        when(album.getImages()).thenReturn(new Image[] { image });
        when(track.getAlbum()).thenReturn(album);

        // Mock de atributos simples
        when(track.getName()).thenReturn(titulo);
        when(track.getId()).thenReturn(spotifyId);
        when(track.getUri()).thenReturn(uris);

        return track;
    }


}
