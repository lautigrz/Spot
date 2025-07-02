package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.NotificacionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ServicioNotificacionTest {

    private RepositorioNotificacion repositorioNotificacionMock;
    private ServicioUsuario servicioUsuarioMock;
    private ServicioRecomedacionComunidad servicioRecomedacionComunidad;
    private ServicioNotificacion servicioNotificacion;
    @BeforeEach
    public void setUp() {
        repositorioNotificacionMock = mock(RepositorioNotificacion.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioRecomedacionComunidad = mock(ServicioRecomedacionComunidad.class);
        servicioNotificacion = new ServicioNotificacionImpl(repositorioNotificacionMock,servicioUsuarioMock,servicioRecomedacionComunidad);

    }

    @Test
    public void seDebeObtenerNotificacionesDto(){

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(new Usuario());
        notificacion.setMensaje("prueba11");

        Notificacion notificacion1 = new Notificacion();
        notificacion1.setUsuario(new Usuario());
        notificacion1.setMensaje("prueba12");

        when(repositorioNotificacionMock.obtenerNotificacionesPorUsuario(anyLong())).thenReturn(List.of(notificacion,notificacion1));

        List<NotificacionDto> notis = servicioNotificacion.obtenerNotificacionesPorUsuario(anyLong());

        assertThat(notis.size(),equalTo(2));
        assertThat(notis,
                containsInAnyOrder(
                        hasProperty("mensaje", equalTo(notificacion.getMensaje())),
                        hasProperty("mensaje", equalTo(notificacion1.getMensaje()))
                )
        );

    }

    @Test
    public void testGenerarNotificacion_Aceptada_GuardaNotificacionCorrectamente() {

        Long idUsuario = 1L;
        Long idRecomendacion = 2L;
        Boolean estado = true;

        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        Cancion cancion = new Cancion();
        cancion.setTitulo("Cancion Test");

        Recomendacion recomendacion = new Recomendacion();
        recomendacion.setId(idRecomendacion);
        recomendacion.setCancion(cancion);

        when(servicioUsuarioMock.obtenerUsuarioPorId(idUsuario)).thenReturn(usuario);
        when(servicioRecomedacionComunidad.obtenerRecomendacionPorId(idRecomendacion)).thenReturn(recomendacion);

        servicioNotificacion.generarNotificacion(idUsuario, idRecomendacion, estado);


        verify(repositorioNotificacionMock).guardarNotificacion(
                argThat(notificacion ->
                        notificacion.getLeido().equals(false)
                                && notificacion.getMensaje().equals("Tu recomendación de la cancion 'Cancion Test' ha sido aceptada.")
                                && notificacion.getRecomendacion().equals(recomendacion)
                ),
                eq(usuario),
                eq(recomendacion)
        );


        verify(servicioUsuarioMock).obtenerUsuarioPorId(idUsuario);
        verify(servicioRecomedacionComunidad).obtenerRecomendacionPorId(idRecomendacion);
    }

    @Test
    public void testGenerarNotificacion_Rechazada_GuardaNotificacionCorrectamente() {

        Long idUsuario = 1L;
        Long idRecomendacion = 2L;
        Boolean estado = false;

        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        Cancion cancion = new Cancion();
        cancion.setTitulo("Cancion Test");

        Recomendacion recomendacion = new Recomendacion();
        recomendacion.setId(idRecomendacion);
        recomendacion.setCancion(cancion);

        when(servicioUsuarioMock.obtenerUsuarioPorId(idUsuario)).thenReturn(usuario);
        when(servicioRecomedacionComunidad.obtenerRecomendacionPorId(idRecomendacion)).thenReturn(recomendacion);

        servicioNotificacion.generarNotificacion(idUsuario, idRecomendacion, estado);


        verify(repositorioNotificacionMock).guardarNotificacion(
                argThat(notificacion ->
                        notificacion.getLeido().equals(false)
                                && notificacion.getMensaje().equals("Tu recomendación de la cancion 'Cancion Test' no ha sido aceptada.")
                                && notificacion.getRecomendacion().equals(recomendacion)
                ),
                eq(usuario),
                eq(recomendacion)
        );


        verify(servicioUsuarioMock).obtenerUsuarioPorId(idUsuario);
        verify(servicioRecomedacionComunidad).obtenerRecomendacionPorId(idRecomendacion);
    }

    @Test
    public void testElUsuarioTieneNotificaciones(){
        when(repositorioNotificacionMock.elUsuarioTieneNotificaciones(anyLong())).thenReturn(true);
        Boolean tieneNotificaciones = servicioNotificacion.elUsuarioTieneNotificaciones(1L);
        assertThat(tieneNotificaciones,equalTo(true));

    }

    @Test
    public void seDebeCambiarEstadoNotificacion_LlamaRepositorioConIdsCorrectos() {

        List<Long> ids = List.of(1L, 2L, 3L);


        servicioNotificacion.cambiarEstadoNotificacion(ids);

        verify(repositorioNotificacionMock).cambiarEstadoNotificacion(ids);
    }



}
