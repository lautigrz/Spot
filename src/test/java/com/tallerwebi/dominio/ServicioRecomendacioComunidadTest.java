package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.CancionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ServicioRecomendacioComunidadTest {

    private RepositorioRecomendacion repositorioRecomendacionMock;
    private RepositorioUsuarioComunidad repositorioUsuarioComunidadMock;
    private RepositorioCancion repositorioCancionMock;
    private ServicioRecomedacionComunidad servicioRecomedacionComunidad;

    @BeforeEach
    public void setUp() {
        repositorioRecomendacionMock = mock(RepositorioRecomendacion.class);
        repositorioUsuarioComunidadMock = mock(RepositorioUsuarioComunidad.class);
        repositorioCancionMock = mock(RepositorioCancion.class);
        servicioRecomedacionComunidad = new ServicioRecomendacionComunidadImpl(
                repositorioRecomendacionMock,
                repositorioUsuarioComunidadMock,
                repositorioCancionMock);
    }

    @Test
    public void seDebeAgregarRecomendaciones() {
        CancionDto cancionDto = new CancionDto();
        cancionDto.setSpotifyId("spotify:track:1234567890");
        cancionDto.setTitulo("Test Song");

        CancionDto cancionDto2 = new CancionDto();
        cancionDto2.setSpotifyId("spotify:track:0987654321");
        cancionDto2.setTitulo("Another Test Song");

        List<CancionDto> cancionDtos = List.of(cancionDto, cancionDto2);

        UsuarioComunidad usuarioComunidad = new UsuarioComunidad();
        usuarioComunidad.setId(1L);
        usuarioComunidad.setComunidad(new Comunidad());
        usuarioComunidad.setUsuario(new Usuario());

        when(repositorioUsuarioComunidadMock.obtenerUsuarioEnComunidad(anyLong(), anyLong()))
                .thenReturn(usuarioComunidad);

        servicioRecomedacionComunidad.agregarRecomendacion(cancionDtos, 1L, 1L);

        verify(repositorioRecomendacionMock, times(2)).agregarRecomendacion(any(Recomendacion.class));


    }

    @Test
    public void seDebeEliminarRecomendacion() {
        Long idRecomendacion = 1L;

        servicioRecomedacionComunidad.eliminarRecomendacion(idRecomendacion);

        verify(repositorioRecomendacionMock, times(1)).eliminarRecomendacion(idRecomendacion);
    }

    @Test
    public void seDebeObtenerRecomendacionesPorComunidad() {
        Cancion cancionDto = new Cancion();
        cancionDto.setSpotifyId("spotify:track:1234567890");
        cancionDto.setTitulo("Test Song");

        Cancion cancion = new Cancion();
        cancion.setSpotifyId("spotify:track:0987654321");
        cancion.setTitulo("Another Test Song");

        Recomendacion recomendacion = new Recomendacion();
        recomendacion.setCancion(cancion);
        recomendacion.setEstado(false);

        Recomendacion recomendacion2 = new Recomendacion();
        recomendacion2.setCancion(cancionDto);
        recomendacion2.setEstado(false);

        List<Recomendacion> recomendaciones = List.of(recomendacion, recomendacion2);

        when(repositorioRecomendacionMock.obtenerRecomendacionesPorComunidad(anyLong()))
                .thenReturn(recomendaciones);

       List<Recomendacion> reco = servicioRecomedacionComunidad.obtenerRecomendacionesPorComunidad(anyLong());
        assertThat(reco.size(),equalTo(2));
    }


}
