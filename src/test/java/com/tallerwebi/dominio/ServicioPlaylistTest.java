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
    @Rollback
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
       // verify(repositorioPlaylist).crearNuevaPlaylistConCanciones(eq(comunidad), anySet());
    }


}
