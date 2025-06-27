package com.tallerwebi.integracion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.presentacion.ControladorAdmin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.NotNull;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ControladorAdminTest {
    private ServicioAdmin servicioAdmin;
    private ServicioRecomedacionComunidad servicioRecomedacionComunidad;
    private ServicioPlaylist servicioPlaylist;

    private ControladorAdmin controladorAdmin;

    @BeforeEach
    public void setUp() {
        servicioAdmin = mock(ServicioAdmin.class);
        servicioRecomedacionComunidad = mock(ServicioRecomedacionComunidad.class);
        servicioPlaylist = mock(ServicioPlaylist.class);

        controladorAdmin = new ControladorAdmin(servicioAdmin, servicioRecomedacionComunidad, servicioPlaylist, null);
    }

    @Test
    public void testEliminarMiembroDeComunidadYRetornaRedireccion() {

        servicioAdmin.eliminarMiembroDeComunidad(anyLong(), anyLong());
        String resultado = controladorAdmin.eliminarMiembroDeComunidad(1L, 2L);
        verify(servicioAdmin).eliminarMiembroDeComunidad(1L, 2L);
        assertThat(resultado,equalTo("redirect:/comunidad/1"));

    }

    @Test
    public void testHacerAdminAMiembroDeComunidadYRetornaRedireccion() {

        servicioAdmin.hacerAdminAUnMiembro(anyLong(), anyLong());
        String resultado = controladorAdmin.hacerAdminAUnMiembro(1L, 2L);
        verify(servicioAdmin).hacerAdminAUnMiembro(1L, 2L);
        assertThat(resultado,equalTo("redirect:/comunidad/1"));

    }

    @Test
    public void testEliminarRecomendacionYRetornaRespuestaCorrecta() {

        Long idRecomendacion = 1L;
        Long idComunidad = 2L;


        Map<String, Object> respuesta = controladorAdmin.eliminarRecomendacion(idRecomendacion, idComunidad);

        verify(servicioRecomedacionComunidad).eliminarRecomendacion(eq(idRecomendacion));
        assertThat(respuesta, notNullValue());
        assertThat(respuesta.get("success"), equalTo(true));
        assertThat(respuesta.get("mensaje"),equalTo("Recomendación eliminada"));
        assertThat(respuesta.get("idRecomendacion"),equalTo(idRecomendacion));
    }

    @Test
    public void testAceptarRecomendacionRetornaRespuestaCorrecta() {

        Long idComunidad = 10L;
        Long idRecomendacion = 20L;

        Cancion cancion = new Cancion();
        cancion.setSpotifyId("spotify123");
        cancion.setUri("spotify:track:abc123");

        Recomendacion recomendacion = new Recomendacion();
        recomendacion.setId(idRecomendacion);
        recomendacion.setCancion(cancion);

        Playlist playlist = new Playlist();
        playlist.setId(99L);
        List<Playlist> playlists = List.of(playlist);

        when(servicioRecomedacionComunidad.aceptarRecomendacion(idRecomendacion)).thenReturn(recomendacion);
        when(servicioPlaylist.obtenerPlaylistsRelacionadasAUnaComunidad(idComunidad)).thenReturn(playlists);


        Map<String, Object> respuesta = controladorAdmin.aceptarRecomendacion(idComunidad, idRecomendacion);


        verify(servicioRecomedacionComunidad).aceptarRecomendacion(idRecomendacion);
        verify(servicioPlaylist).obtenerPlaylistsRelacionadasAUnaComunidad(idComunidad);
        verify(servicioPlaylist).agregarCancionALaPlaylist(
                eq(playlist.getId()),
                eq("spotify123"),
                eq("spotify:track:abc123")
        );

        assertThat(respuesta, notNullValue());
        assertThat(respuesta.get("success"),equalTo(true));
        assertThat(respuesta.get("idRecomendacion"),equalTo(idRecomendacion));
    }



}
