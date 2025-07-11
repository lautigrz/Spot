package com.tallerwebi.integracion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.presentacion.ControladorAdmin;
import com.tallerwebi.presentacion.dto.ChatMessage;
import com.tallerwebi.presentacion.dto.UsuarioDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.matchers.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;

import javax.persistence.Access;
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
    private ServicioEvento servicioEvento;
    private ServicioNotificacion servicioNotificacion;
    private ServicioMensaje servicioMensaje;
    private ControladorAdmin controladorAdmin;
    private SimpMessagingTemplate messagingTemplate;
    @BeforeEach
    public void setUp() {
        servicioAdmin = mock(ServicioAdmin.class);
        messagingTemplate = mock(SimpMessagingTemplate.class);
        servicioRecomedacionComunidad = mock(ServicioRecomedacionComunidad.class);
        servicioNotificacion = mock(ServicioNotificacion.class);
        servicioPlaylist = mock(ServicioPlaylist.class);
        servicioEvento = mock(ServicioEvento.class);
        servicioMensaje = mock(ServicioMensaje.class);
        controladorAdmin = new ControladorAdmin(servicioAdmin, servicioRecomedacionComunidad, servicioPlaylist, servicioEvento, servicioNotificacion, servicioMensaje);
        ReflectionTestUtils.setField(controladorAdmin, "messagingTemplate", messagingTemplate);
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
        verify(servicioNotificacion).generarNotificacionSobreRecomendacion(anyLong(), eq(idRecomendacion), eq(false));
        assertThat(respuesta, notNullValue());
        assertThat(respuesta.get("success"), equalTo(true));
        assertThat(respuesta.get("mensaje"),equalTo("Recomendación eliminada"));
        assertThat(respuesta.get("idRecomendacion"),equalTo(idRecomendacion));
    }

    @Test
    public void testAceptarRecomendacionRetornaRespuestaCorrecta() {

        Long idComunidad = 10L;
        Long idRecomendacion = 20L;

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUser("Usuario Test");

        Cancion cancion = new Cancion();
        cancion.setSpotifyId("spotify123");
        cancion.setUri("spotify:track:abc123");

        Recomendacion recomendacion = new Recomendacion();
        recomendacion.setId(idRecomendacion);
        recomendacion.setCancion(cancion);
        recomendacion.setUsuario(usuario);

        Playlist playlist = new Playlist();
        playlist.setId(99L);
        List<Playlist> playlists = List.of(playlist);

        when(servicioRecomedacionComunidad.aceptarRecomendacion(idRecomendacion)).thenReturn(recomendacion);

        when(servicioPlaylist.obtenerPlaylistsRelacionadasAUnaComunidad(idComunidad)).thenReturn(playlists);


        Map<String, Object> respuesta = controladorAdmin.aceptarRecomendacion(idComunidad, idRecomendacion);

        verify(servicioNotificacion).generarNotificacionSobreRecomendacion(anyLong(), eq(idRecomendacion), eq(true));
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

    @Test
    public void crearEvento_debeAgregarModeloYRetornarVista() {
        Long idComunidad = 123L;
        Model modelMock = mock(Model.class);

        String vista = controladorAdmin.crearEvento(idComunidad, modelMock);

        verify(modelMock).addAttribute(eq("evento"), any(Evento.class));
        verify(modelMock).addAttribute("id", idComunidad);

        assertThat(vista, equalTo("crear-evento"));
    }

    @Test
    public void crearEventoComunidad_debeLlamarServicioYRedirigir() {
        Long idComunidad = 123L;
        Evento evento = new Evento();

        String vista = controladorAdmin.crearEventoComunidad(idComunidad, evento);

        verify(servicioEvento).publicarEvento(evento, idComunidad);
        assertThat(vista, equalTo("redirect:/comunidad/" + idComunidad));
    }

    @Test
    public void eliminarMiembroDeComunidad_deberiaLlamarServiciosYRedirigir() {

        Long idComunidad = 1L;
        Long idMiembro = 2L;


        String resultado = controladorAdmin.eliminarMiembroDeComunidad(idComunidad, idMiembro);


        verify(servicioNotificacion).generarNotificacionDeEliminacionDeUsuarioDeLaComunidad(idMiembro, idComunidad);
        verify(servicioAdmin).eliminarMiembroDeComunidad(idComunidad, idMiembro);
        assertThat("redirect:/comunidad/" + idComunidad, equalTo(resultado));
    }
    @Test
    public void testDelete_deberiaEliminarMensajeGenerarNotificacionYEnviarMensaje() {


        ChatMessage inputMessage = new ChatMessage();
        inputMessage.setId("123");
        inputMessage.setType(ChatMessage.MessageType.DELETE);

        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setId(1L);
        usuarioDto.setUrlFoto("foto.jpg");

        when(servicioMensaje.eliminarMensaje(123L)).thenReturn(usuarioDto);

        String idComunidad = "10";


        controladorAdmin.delete(inputMessage, idComunidad);


        verify(servicioMensaje).eliminarMensaje(123L);
        verify(servicioNotificacion).generarNotificacionDeMensajeEliminacionDeUsuarioDeLaComunidad(1L, 10L);

        ArgumentCaptor<ChatMessage> messageCaptor = ArgumentCaptor.forClass(ChatMessage.class);

        verify(messagingTemplate).convertAndSend(eq("/topic/" + idComunidad), messageCaptor.capture());

        ChatMessage eliminado = messageCaptor.getValue();

        assertThat("123",equalTo(eliminado.getId()));
        assertThat("foto.jpg", equalTo(eliminado.getImage()));
        assertThat(ChatMessage.MessageType.DELETE, equalTo(eliminado.getType()));
    }
}
