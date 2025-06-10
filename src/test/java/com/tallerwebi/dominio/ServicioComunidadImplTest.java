package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.ChatMessage;
import com.tallerwebi.presentacion.dto.UsuarioDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.michaelthelin.spotify.SpotifyApi;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class ServicioComunidadImplTest {

    private RepositorioUsuario repositorioUsuarioMock;

    private RepositorioComunidad repositorioComunidadMock;

    private ServicioComunidad servicioComunidad;

    private SpotifyApi spotifyApiMock;
    @BeforeEach
    public void setUp() {
        repositorioUsuarioMock = mock(RepositorioUsuario.class);
        repositorioComunidadMock = mock(RepositorioComunidad.class);
        spotifyApiMock = mock(SpotifyApi.class);
        servicioComunidad = new ServicioComunidadImpl(repositorioUsuarioMock,repositorioComunidadMock);
    }


    @Test
    public void seDebeGuardarUnMensajeDelUsuarioEnUnaComunidad(){
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUser("lauti");
        usuario.setUrlFoto("htt://fds");

        Comunidad comunidad = new Comunidad();
        comunidad.setId(2L);
        comunidad.setNombre("Rock");
        comunidad.setDescripcion("descripcion");

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(usuario.getUser());
        chatMessage.setContent("Buenas");

        when(repositorioComunidadMock.obtenerUsuarioEnComunidad(anyLong(), anyLong())).thenReturn(usuario);
        when(repositorioComunidadMock.obtenerComunidad(anyLong())).thenReturn(comunidad);

       ChatMessage mensaje = servicioComunidad.guardarMensaje(chatMessage,usuario.getId(),comunidad.getId());

        verify(repositorioComunidadMock).guardarMensajeDeLaComunidad(anyString(), anyLong(), anyLong());
        assertThat(mensaje.getSender(), equalTo(usuario.getUser()));
    }

    @Test
    public void debeRetornarTrueAlGuardarUsuarioEnUnaComunidad(){
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUser("lauti");
        usuario.setUrlFoto("htt://fds");

        Comunidad comunidad = new Comunidad();
        comunidad.setId(2L);
        comunidad.setNombre("Rock");
        comunidad.setDescripcion("descripcion");

        when(repositorioUsuarioMock.buscarUsuarioPorId(anyLong())).thenReturn(usuario);
        when(repositorioComunidadMock.guardarUsuarioEnComunidad(any(Usuario.class),anyLong())).thenReturn(true);
        when(repositorioComunidadMock.obtenerComunidad(anyLong())).thenReturn(comunidad);

        Boolean seAgregoElUsuarioALaComunidad = servicioComunidad.guardarUsuarioEnComunidad(usuario.getId(),comunidad.getId());

        assertThat(seAgregoElUsuarioALaComunidad, equalTo(true));
    }
    @Test
    public void debeRetornarFalseAlGuardarUsuarioEnUnaComunidad(){
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUser("lauti");
        usuario.setUrlFoto("htt://fds");

        Comunidad comunidad = new Comunidad();
        comunidad.setId(2L);
        comunidad.setNombre("Rock");
        comunidad.setDescripcion("descripcion");

        when(repositorioUsuarioMock.buscarUsuarioPorId(anyLong())).thenReturn(null);

        Boolean seAgregoElUsuarioALaComunidad = servicioComunidad.guardarUsuarioEnComunidad(usuario.getId(),comunidad.getId());

        assertThat(seAgregoElUsuarioALaComunidad, equalTo(false));
    }

    @Test
    public void debeObtenerUsuarioDeLaComunidad(){
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUser("lauti");
        usuario.setUrlFoto("htt://fds");

        Comunidad comunidad = new Comunidad();
        comunidad.setId(2L);
        comunidad.setNombre("Rock");
        comunidad.setDescripcion("descripcion");

        when(repositorioComunidadMock.obtenerUsuarioEnComunidad(anyLong(), anyLong())).thenReturn(usuario);

        UsuarioDto usuarioDto = servicioComunidad.obtenerUsuarioDeLaComunidad(usuario.getId(),comunidad.getId());
        assertThat(usuarioDto.getId(), equalTo(usuario.getId()));
        assertThat(usuarioDto.getUser(), equalTo(usuario.getUser()));
        assertThat(usuarioDto.getUrlFoto(), equalTo(usuario.getUrlFoto()));

    }

    @Test
    public void seDebeObtenerUnaListaDeMensajeDeUnaComundad(){
        Comunidad comunidad = new Comunidad();
        comunidad.setId(2L);
        comunidad.setNombre("Rock");
        comunidad.setDescripcion("descripcion");

        Mensaje mensaje = new Mensaje();
        mensaje.setId(5L);
        mensaje.setTexto("hola");
        Mensaje mensaje2 = new Mensaje();
        mensaje2.setId(6L);
        mensaje2.setTexto("holamns");

        comunidad.getMensajes().add(mensaje);
        comunidad.getMensajes().add(mensaje2);

        List<Mensaje> mensajes = List.of(mensaje,mensaje2);


        when(repositorioComunidadMock.obtenerComunidad(anyLong())).thenReturn(comunidad);
        when(repositorioComunidadMock.obtenerMensajesDeComunidad(anyLong())).thenReturn(mensajes);
        List<Mensaje> mensajeDeComunidad = servicioComunidad.obtenerMensajes(comunidad.getId());

        // containsInAnyOrder para verificar que una colección contiene exactamente
        // todos los elementos especificados, sin importar el orden en que estén
        assertThat(mensajeDeComunidad,containsInAnyOrder(comunidad.getMensajes().toArray()));
    }

    @Test
    public void seDebeObtenerUnaComunidadPorId(){
        Comunidad comunidad = new Comunidad();
        comunidad.setId(2L);
        comunidad.setNombre("Rock");
        comunidad.setDescripcion("descripcion");

        when(repositorioComunidadMock.obtenerComunidad(anyLong())).thenReturn(comunidad);

        Comunidad comunidadObtenida = servicioComunidad.obtenerComunidad(comunidad.getId());
        assertThat(comunidadObtenida, equalTo(comunidad));



    }

    @Test
    public void seDebeObtenerUnaListaDeComunidades(){
        Comunidad comunidad = new Comunidad();
        comunidad.setId(2L);
        comunidad.setNombre("Rock");
        comunidad.setDescripcion("descripcion");

        Comunidad comunidad2 = new Comunidad();
        comunidad2.setId(3L);
        comunidad2.setNombre("Rock");
        comunidad2.setDescripcion("descripcion2");

        List<Comunidad> comunidades = List.of(comunidad,comunidad2);

        when(repositorioComunidadMock.obtenerComunidades()).thenReturn(comunidades);

        List<Comunidad> listaDeComunidades = servicioComunidad.obtenerTodasLasComunidades();

        assertThat(listaDeComunidades,containsInAnyOrder(comunidades.toArray()));

    }
}
