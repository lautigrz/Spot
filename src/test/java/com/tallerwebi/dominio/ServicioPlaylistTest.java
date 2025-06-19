package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.CancionDto;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ServicioPlaylistTest {

    private RepositorioPlaylist repositorioPlaylist;
    private RepositorioCancion repositorioCancion;
    private ServicioPlaylist servicioPlaylist;
    @BeforeEach
    public void setUp() {
        repositorioPlaylist = mock(RepositorioPlaylist.class);
        repositorioCancion = mock(RepositorioCancion.class);
        servicioPlaylist = new ServicioPlaylistImpl(repositorioPlaylist, repositorioCancion);
    }

    @Test
    public void seDebeCrearNuevaPlaylistConCancionesEnUnaComunidad() {
        // Arrange
        CancionDto cancionDto = new CancionDto();
        cancionDto.setId(4L);
        cancionDto.setArtista("Billie");
        cancionDto.setTitulo("Ocean eyes");
        cancionDto.setUri("spotify:track:434fjsd");
        cancionDto.setSpotifyId("dfsdfds$fDAc");

        Comunidad comunidad = new Comunidad();
        comunidad.setId(2L);
        comunidad.setNombre("Billie");
        comunidad.setDescripcion("Ocean eyes");

        List<CancionDto> canciones = new ArrayList<>();
        canciones.add(cancionDto);

        // Simular que la canción no existe aún
        when(repositorioCancion.buscarCancionPorElIdDeSpotify("dfsdfds$fDAc")).thenReturn(null);

        // Act
        servicioPlaylist.crearNuevaPlaylistConCanciones(comunidad, canciones, "d", "d");

        // Assert
        verify(repositorioCancion).buscarCancionPorElIdDeSpotify("dfsdfds$fDAc");

        // Verificar que se guarda la canción nueva
        verify(repositorioCancion).guardarCancion(any(Cancion.class));

        // Verificar que se llama a crear la nueva playlist
       verify(repositorioPlaylist).crearNuevaPlaylistConCanciones(eq(comunidad), anySet(), anyString(), anyString());
    }

    @Test
    public void seDebeObtenerPlaylistsRelacionadasAUnaComunidad(){

        Cancion cancion = new Cancion();
        cancion.setId(4L);
        cancion.setArtista("Billie");
        cancion.setTitulo("Ocean eyes");
        cancion.setUri("spotify:track:434fjsd");
        cancion.setSpotifyId("dfsdfds$fDAc");

        Cancion cancion2 = new Cancion();
        cancion2.setId(5L);
        cancion2.setArtista("Billie");
        cancion2.setTitulo("Bad guy");
        cancion2.setUri("spotify:track:434f11jsd");
        cancion2.setSpotifyId("dfsdfds$fDAc2");



        HashSet <Cancion> canciones = new HashSet<>();
        canciones.add(cancion);
        canciones.add(cancion2);

        Playlist playlist = new Playlist();
        playlist.setId(1L);
        playlist.setNombre("Playlist 1");
        playlist.setUrlImagen("http://example.com/playlist1.jpg");
        playlist.agregarCanciones(canciones);

        when(repositorioPlaylist.obtenerPlaylistsRelacionadasAUnaComunidad(anyLong()))
                .thenReturn(List.of(playlist));

       List<Playlist> playlists = servicioPlaylist.obtenerPlaylistsRelacionadasAUnaComunidad(2L);

       assertThat(playlists.isEmpty(), equalTo(false));
       assertThat(playlists.get(0).getCanciones(), containsInAnyOrder(cancion,cancion2));
       assertThat(playlists.get(0).getId(), equalTo(1L));

    }

    @Test
    public void seDebenObtenerCancionesDeUnaPlaylist() {

        Cancion cancion = new Cancion();
        cancion.setId(1L);
        cancion.setArtista("Artista 1");
        cancion.setTitulo("Canción 1");
        cancion.setUrlImagen("http://example.com/cancion1.jpg");

        Cancion cancion2 = new Cancion();
        cancion2.setId(2L);
        cancion2.setArtista("Artista 2");
        cancion2.setTitulo("Canción 2");
        cancion2.setUrlImagen("http://example.com/cancion2.jpg");

        Cancion cancion3 = new Cancion();
        cancion3.setId(3L);
        cancion3.setArtista("Artista 3");
        cancion3.setTitulo("Canción 3");
        cancion3.setUrlImagen("http://example.com/cancion3.jpg");

        HashSet <Cancion> canciones = new HashSet<>();
        canciones.add(cancion);
        canciones.add(cancion2);
        canciones.add(cancion3);


        Playlist playlist = new Playlist();
        playlist.setId(1L);
        playlist.setNombre("Playlist de prueba");
        playlist.setUrlImagen("http://example.com/playlist.jpg");
        playlist.agregarCanciones(canciones);


        when(repositorioPlaylist.obtenerCancionesDeLaPlaylist(anyLong()))
                .thenReturn(List.of(cancion,cancion2,cancion3));


        List<CancionDto> cancionesDto = servicioPlaylist.obtenerCancionesDeLaPlaylist(1L);


        assertThat(cancionesDto.size(), equalTo(3));
        assertThat(cancionesDto.get(0).getId(), equalTo(1L));
        assertThat(cancionesDto.get(0).getArtista(), equalTo("Artista 1"));
        assertThat(cancionesDto.get(0).getTitulo(), equalTo("Canción 1"));

        assertThat(cancionesDto.get(1).getId(), equalTo(2L));
        assertThat(cancionesDto.get(1).getArtista(), equalTo("Artista 2"));
        assertThat(cancionesDto.get(1).getTitulo(), equalTo("Canción 2"));

        assertThat(cancionesDto.get(2).getId(), equalTo(3L));
        assertThat(cancionesDto.get(2).getArtista(), equalTo("Artista 3"));
        assertThat(cancionesDto.get(2).getTitulo(), equalTo("Canción 3"));
    }

}
