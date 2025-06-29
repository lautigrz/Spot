package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioPreescuchaImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.Local;
import se.michaelthelin.spotify.SpotifyApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class ServicioPreescuchaImplTest {

    private RepositorioPreescucha repositorioPreescuchaMock;
    private ServicioPreescucha servicioPreescucha;
    private SpotifyApi spotifyApiMock;

    @BeforeEach
    public void setUp() {
        repositorioPreescuchaMock = mock(RepositorioPreescucha.class);
        spotifyApiMock = mock(SpotifyApi.class);
        servicioPreescucha = new ServicioPreescuchaImpl(repositorioPreescuchaMock,spotifyApiMock);
    }

    @Test
    public void queSePuedaComprarUnAlbumDePreescucha(){
        String spotifyAlbumId = "05IR0uSEjd7nu4ZgQnIMiA";
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUser("testuser");
        usuario.setToken("token");
        usuario.setRefreshToken("refresh");
        usuario.setUrlFoto("foto.jpg");

        //Se simula que el usuario no compro nada por ahora
        when(repositorioPreescuchaMock.existeCompra(spotifyAlbumId,usuario.getId())).thenReturn(false);
        //Se compra el album de preescucha
        servicioPreescucha.comprarPreescucha(spotifyAlbumId, usuario);
        //Se verifica que realmente se haya comprado una Preescucha
        verify(repositorioPreescuchaMock).guardar(any(Preescucha.class));
    }

    @Test
    public void queNoMePermitaComprarUnAlbumDePreescuchaYaCompradoAnteriormente(){
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUser("testuser");
        usuario.setToken("token");
        usuario.setRefreshToken("refresh");
        usuario.setUrlFoto("foto.jpg");

        String spotifyAlbumId = "05IR0uSEjd7nu4ZgQnIMiA";

        when(repositorioPreescuchaMock.existeCompra(spotifyAlbumId,usuario.getId()))
                .thenReturn(false)
                .thenReturn(true);

        servicioPreescucha.comprarPreescucha(spotifyAlbumId, usuario);
        servicioPreescucha.comprarPreescucha(spotifyAlbumId, usuario);

        //Verifico que solamente se haya comprado una vez
        verify(repositorioPreescuchaMock, times(1)).guardar(any(Preescucha.class));
    }

    @Test
    public void debeVerificarSiUnAlbumDePreescuchaFueComprado(){

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUser("testuser");
        usuario.setToken("token");
        usuario.setRefreshToken("refresh");
        usuario.setUrlFoto("foto.jpg");

        String spotifyAlbumId = "05IR0uSEjd7nu4ZgQnIMiA";

        when(repositorioPreescuchaMock.existeCompra(spotifyAlbumId,usuario.getId())).thenReturn(true);
        boolean comprado = servicioPreescucha.yaComproPreescucha(spotifyAlbumId, usuario);
        assertThat(comprado, equalTo(true));
    }

    @Test
    public void obtenerLosAlbumsDePreescuchaDeUnUsuario(){
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUser("testuser");
        usuario.setToken("token");
        usuario.setRefreshToken("refresh");
        usuario.setUrlFoto("foto.jpg");

        Preescucha p1 = new Preescucha();
        p1.setSpotifyAlbumId("05IR0uSEjd7nu4ZgQnIMiA");
        p1.setUsuario(usuario);
        p1.setFechaCompra(LocalDateTime.now());

        Preescucha p2 = new Preescucha();
        p2.setSpotifyAlbumId("25wv6eU2tDQDPLAYTyuj2Q");
        p2.setUsuario(usuario);
        p2.setFechaCompra(LocalDateTime.now());

        List<Preescucha> compras = Arrays.asList(p1, p2);

        when(repositorioPreescuchaMock.obtenerComprasPorUsuario(usuario.getId())).thenReturn(compras);

        List<String> albumesComprados =  servicioPreescucha.obtenerAlbumesComprados(usuario);

        assertThat(albumesComprados, containsInAnyOrder("25wv6eU2tDQDPLAYTyuj2Q" ,"05IR0uSEjd7nu4ZgQnIMiA" ));
        assertThat(albumesComprados.size(), equalTo(2));


    }


}
