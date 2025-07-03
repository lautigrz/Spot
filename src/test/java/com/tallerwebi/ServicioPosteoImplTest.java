package com.tallerwebi;

import com.tallerwebi.dominio.*;
import com.tallerwebi.infraestructura.RepositorioPosteoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class ServicioPosteoImplTest {

    private RepositorioPosteo repositorioPosteoMock;
    private RepositorioFavorito repositorioFavoritoMock;
    private ServicioPosteo servicioPosteo;

    @BeforeEach
    public void setUp() {
        repositorioPosteoMock = mock(RepositorioPosteo.class);
        repositorioFavoritoMock = mock(RepositorioFavorito.class);
        servicioPosteo = new ServicioPosteoImpl(repositorioPosteoMock, repositorioFavoritoMock);
    }


    @Test
    public void queSePuedaPublicarUnPosteo(){
        Artista artista = new Artista();
        artista.setId(1L);
        artista.setNombre("Facu");

        servicioPosteo.publicarPosteo(artista, "Posteo de prueba");

        verify(repositorioPosteoMock, times(1))
                .guardar(argThat(post -> post.getContenido().equals("Posteo de prueba")));
    }

    @Test
    public void deberiaObtenerPosteosDeUnArtista(){
        Artista artista = new Artista();
        artista.setId(1L);

        List<Post> postMock = List.of(new Post());
        when(repositorioPosteoMock.obtenerPostsPorArtista(1L)).thenReturn(postMock);

        List<Post> resultado = servicioPosteo.obtenerPosteosDeArtista(artista);
        assertThat(resultado, equalTo(postMock));
    }

    @Test
    public void deberiaObtenerPosteosDeArtistasFavoritosConIdsLocales(){
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Favorito favLocal1 = new Favorito();
        favLocal1.setSpotifyArtistId("LOCAL_10");
        Favorito favLocal2 = new Favorito();
        favLocal2.setSpotifyArtistId("LOCAL_20");
        Favorito favNoLocal = new Favorito();
        favNoLocal.setSpotifyArtistId("SPOTIFY_123");

        when(repositorioFavoritoMock.obtenerFavoritosDeUsuario(usuario.getId()))
                .thenReturn(List.of(favLocal1, favLocal2, favNoLocal));

        List<Post> postsMock = List.of(new Post());
        when(repositorioPosteoMock.obtenerPostsDeArtistasFavoritos(List.of(10L, 20L)))
                .thenReturn(postsMock);

        List<Post> resultado = servicioPosteo.obtenerPosteosDeArtistasFavoritos(usuario);

        assertThat(resultado, equalTo(postsMock));
    }
}
