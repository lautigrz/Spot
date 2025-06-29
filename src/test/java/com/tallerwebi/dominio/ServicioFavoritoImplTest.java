package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioFavoritoImpl;
import com.tallerwebi.presentacion.dto.FavoritoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Image;
import se.michaelthelin.spotify.requests.data.artists.GetArtistRequest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class ServicioFavoritoImplTest {

    private RepositorioFavorito repositorioFavoritoMock;
    private ServicioFavorito servicioFavorito;
    private SpotifyApi spotifyApiMock;

    @BeforeEach
    public void setUp(){
        repositorioFavoritoMock = mock(RepositorioFavorito.class);
        spotifyApiMock = mock(SpotifyApi.class);
        servicioFavorito = new ServicioFavoritoImpl(repositorioFavoritoMock,spotifyApiMock);

    }

    @Test
    public void queSePuedaAgregarUnFavorito(){
        String spotifyArtistId = "3TVXtAsR1Inumwj472S9r4"; // Drake
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUser("testuser");
        usuario.setToken("token");
        usuario.setRefreshToken("refresh");
        usuario.setUrlFoto("foto.jpg");

        when(repositorioFavoritoMock.yaEsFavorito(spotifyArtistId, usuario.getId())).thenReturn(false);
        servicioFavorito.agregarFavorito(spotifyArtistId, usuario);
        verify(repositorioFavoritoMock).agregarFavorito(any(Favorito.class));

    }


    @Test
    public void queNoMePermitaAgregarUnFavoritoYaExistenteParaUnUsuario() {
        Usuario usuario = new Usuario();
        usuario.setUser("testuser");
        usuario.setToken("token");
        usuario.setRefreshToken("refresh");
        usuario.setUrlFoto("foto.jpg");

        String spotifyArtistId = "3TVXtAsR1Inumwj472S9r4"; // Drake


        when(repositorioFavoritoMock.yaEsFavorito(spotifyArtistId, usuario.getId()))
                .thenReturn(false)
                .thenReturn(true);

        // Llamada real al servicio
        servicioFavorito.agregarFavorito(spotifyArtistId, usuario);
        servicioFavorito.agregarFavorito(spotifyArtistId, usuario);

        // Verifico que agregarFavorito se llamó solo 1 vez, en el primer intento
        verify(repositorioFavoritoMock, times(1)).agregarFavorito(any(Favorito.class));
    }

    @Test
    public void debeVerificarSiUnArtistaYaEsFavorito() {
        Usuario usuario = new Usuario();
        usuario.setUser("testuser");
        usuario.setToken("token");
        usuario.setRefreshToken("refresh");
        usuario.setUrlFoto("foto.jpg");

        String spotifyArtistId = "3TVXtAsR1Inumwj472S9r4"; // Drake

        when(repositorioFavoritoMock.yaEsFavorito(spotifyArtistId, usuario.getId()))
                .thenReturn(true);

        boolean resultado = servicioFavorito.yaEsFavorito(spotifyArtistId, usuario);

        assertThat(resultado, equalTo(true));
    }

    @Test
    public void obtenerLosFavoritosDeUnUsuario() throws Exception{
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUser("testuser");
        usuario.setToken("token");
        usuario.setRefreshToken("refresh");
        usuario.setUrlFoto("foto.jpg");

        Favorito favorito = new Favorito();
        favorito.setSpotifyArtistId("3TVXtAsR1Inumwj472S9r4");
        favorito.setUsuario(usuario);

        when(repositorioFavoritoMock.obtenerFavoritosDeUsuario(usuario.getId()))
                .thenReturn(List.of(favorito));

        Artist mockArtist = mock(Artist.class);
        when(mockArtist.getName()).thenReturn("Artista Prueba");
        when(mockArtist.getImages()).thenReturn(new Image[0]);

        GetArtistRequest.Builder builderMock = mock(GetArtistRequest.Builder.class);
        GetArtistRequest requestMock = mock(GetArtistRequest.class);
        when(spotifyApiMock.getArtist("3TVXtAsR1Inumwj472S9r4")).thenReturn(builderMock);
        when(builderMock.build()).thenReturn(requestMock);
        when(requestMock.execute()).thenReturn(mockArtist);

        List<FavoritoDTO> dtos = servicioFavorito.obtenerFavoritos(usuario);

        assertThat(dtos.size(), equalTo(1));
        assertThat(dtos.get(0).getNombre(), equalTo("Artista Prueba"));
    }
}
