package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.ChatMessage;
import com.tallerwebi.presentacion.dto.UsuarioDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import se.michaelthelin.spotify.SpotifyApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

import static org.mockito.Mockito.*;

public class ServicioComunidadImplTest {

    private RepositorioUsuario repositorioUsuarioMock;

    private RepositorioComunidad repositorioComunidadMock;

    private RepositorioUsuarioComunidad repositorioUsuarioComunidadMock;
    private ServicioComunidad servicioComunidad;


    @BeforeEach
    public void setUp() {
        repositorioUsuarioMock = mock(RepositorioUsuario.class);
        repositorioComunidadMock = mock(RepositorioComunidad.class);
        repositorioUsuarioComunidadMock = mock(RepositorioUsuarioComunidad.class);
        servicioComunidad = new ServicioComunidadImpl(repositorioUsuarioMock,repositorioComunidadMock, repositorioUsuarioComunidadMock);
    }
    @AfterEach
    public void limpiarEstadoGlobal() {
        ServicioComunidadImpl.limpiarCanales();
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

        UsuarioComunidad usuarioComunidad = new UsuarioComunidad();
        usuarioComunidad.setId(3L);
        usuarioComunidad.setUsuario(usuario);
        usuarioComunidad.setComunidad(comunidad);


        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(usuario.getUser());
        chatMessage.setContent("Buenas");


        when(repositorioUsuarioComunidadMock.obtenerUsuarioEnComunidad(anyLong(),anyLong())).thenReturn(usuarioComunidad);
        when(repositorioComunidadMock.obtenerComunidad(anyLong())).thenReturn(comunidad);

       ChatMessage mensaje = servicioComunidad.guardarMensaje(chatMessage,usuario.getId(),comunidad.getId());

        verify(repositorioComunidadMock).guardarMensajeDeLaComunidad(anyString(), any(Comunidad.class), any(Usuario.class));
        assertThat(mensaje.getSender(), equalTo(usuario.getUser()));
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

    @Test
    public void seDebeEliminarUsuarioDelCanal(){

    servicioComunidad.crearCanalSiNoExiste("5");

    servicioComunidad.agregarUserAlCanal("5","Lauti");
    servicioComunidad.agregarUserAlCanal("5","Messi");

    servicioComunidad.eliminarUsuarioDelCanal("Lauti");

    List<String> user = servicioComunidad.obtenerTodosLosUsuariosActivosDeUnaComunidad(5L);

    assertThat(user.size(),equalTo(1));
    assertThat(user.get(0),equalTo("Messi"));


    }

    @Test
    public void seDebeVerificarSiHayAlguienActivoEnLaComunidadExceptoElActual(){
        servicioComunidad.crearCanalSiNoExiste("5");
        servicioComunidad.agregarUserAlCanal("5","Lauti");
        servicioComunidad.agregarUserAlCanal("5","Messi");

        Boolean hayAlguien = servicioComunidad.hayAlguienEnLaComunidad("5", "Lauti");
        assertThat(hayAlguien,equalTo(true));

    }

    @Test
    public void seDebeObtenerTodosLosUsuariosActivosDeUnCanal(){
        servicioComunidad.crearCanalSiNoExiste("5");
        servicioComunidad.agregarUserAlCanal("5","Lauti");
        servicioComunidad.agregarUserAlCanal("5","Messi");

        List<String> users = servicioComunidad.obtenerTodosLosUsuariosActivosDeUnaComunidad(5L);

        assertThat(users.size(),equalTo(2));
        assertThat(users,containsInAnyOrder("Messi","Lauti"));


    }

    @Test
    public void seDebeobtenerUsuarioDeLaComunidadActivoDeLaLista(){
        servicioComunidad.crearCanalSiNoExiste("5");
        servicioComunidad.agregarUserAlCanal("5","Lauti");
        servicioComunidad.agregarUserAlCanal("5","Messi");

        String user = servicioComunidad.obtenerUsuarioDeLaComunidadActivoDeLaLista("5","Lauti");
        assertThat(user,equalTo("Messi"));
    }

    @Test
    public void seDebeRegistrarUnUsuarioEnUnCanal(){

        String idComunidad = "123";
        String usuario = "juanito";

        ChatMessage message = new ChatMessage();
        message.setSender(usuario);

        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();
        Map<String, Object> sessionAttributes = new HashMap<>();
        headerAccessor.setSessionAttributes(sessionAttributes);

        ChatMessage resultado = servicioComunidad.registrarUsuarioEnCanalDeComunidad(message, headerAccessor, idComunidad);


        List<String> usuariosEnCanal = servicioComunidad.obtenerTodosLosUsuariosActivosDeUnaComunidad(Long.valueOf(idComunidad));
        assertThat(usuariosEnCanal, containsInAnyOrder(usuario));
        assertThat(usuario, equalTo(headerAccessor.getSessionAttributes().get("usuario")));
        assertThat(message, equalTo( resultado));
    }
}
