package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ServicioUsuarioComunidadTest {

    private RepositorioUsuarioComunidad repositorioUsuarioComunidadMock;
    private ServicioUsuarioComunidad servicioUsuarioComunidad;
    private ServicioPosteo servicioPosteoMock;
    private ServicioUsuario servicioUsuario;
    @BeforeEach
    public void setUp() {
        servicioUsuario = mock(ServicioUsuario.class);
        servicioPosteoMock = mock(ServicioPosteo.class);
      repositorioUsuarioComunidadMock = mock(RepositorioUsuarioComunidad.class);
      servicioUsuarioComunidad = new ServicioUsuarioComunidadImpl(repositorioUsuarioComunidadMock, servicioPosteoMock,servicioUsuario);

    }


    @Test
    public void seObtenerRolDelUsuarioEnComunidad() {

        when(repositorioUsuarioComunidadMock.obtenerRolDelUsuarioEnComunidad(anyLong(),anyLong()))
            .thenReturn("admin");

        String rol = servicioUsuarioComunidad.obtenerRolDelUsuarioEnComunidad(2L, 3L);

        assertThat(rol, equalTo("admin"));

    }

    @Test
    public void seDebeAgregarUsuarioAComunidadYRetornarTrue() {

        Usuario usuario = new Usuario();
        Comunidad comunidad = new Comunidad();
        String rol = "admin";

        when(repositorioUsuarioComunidadMock.agregarUsuarioAComunidad(usuario, comunidad, rol))
            .thenReturn(true);

        Boolean resultado = servicioUsuarioComunidad.agregarUsuarioAComunidad(usuario, comunidad, rol);

        assertThat(resultado, equalTo(true));
    }

    @Test
    public void seDebeAgregarUsuarioAComunidadYRetornarFalse() {
        Usuario usuario = new Usuario();
        Comunidad comunidad = new Comunidad();
        String rol = "admin";

        when(repositorioUsuarioComunidadMock.agregarUsuarioAComunidad(usuario, comunidad, rol))
            .thenReturn(false);

        Boolean resultado = servicioUsuarioComunidad.agregarUsuarioAComunidad(usuario, comunidad, rol);

        assertThat(resultado, equalTo(false));
    }

    @Test
    public void seDebeObtenerUsuarioEnComunidad() {

        UsuarioComunidad usuarioComunidad = new UsuarioComunidad();
        when(repositorioUsuarioComunidadMock.obtenerUsuarioEnComunidad(anyLong(), anyLong()))
            .thenReturn(usuarioComunidad);

        UsuarioComunidad resultado = servicioUsuarioComunidad.obtenerUsuarioEnComunidad(1L, 2L);

        assertThat(resultado, equalTo(usuarioComunidad));
    }

    @Test
    public void seDebeEliminarUnUsuarioDeLaComunidadyDevolverTrue() {

        when(repositorioUsuarioComunidadMock.eliminarUsuarioDeComunidad(anyLong(), anyLong()))
            .thenReturn(true);

        Boolean resultado = servicioUsuarioComunidad.eliminarUsuarioDeComunidad(1L, 2L);

        assertThat(resultado, equalTo(true));
    }
    @Test
    public void seDebeEliminarUnUsuarioDeLaComunidadyDevolverFalse() {

        when(repositorioUsuarioComunidadMock.eliminarUsuarioDeComunidad(anyLong(), anyLong()))
                .thenReturn(false);

        Boolean resultado = servicioUsuarioComunidad.eliminarUsuarioDeComunidad(1L, 2L);

        assertThat(resultado, equalTo(false));
    }

    @Test
    public void seDebeCompartirUnPostComoMensajeParaUnaComunidad() {

        Long idPost = 1L;
        Long idComunidad = 5L;
        Long idUsuario = 1L;

        Post postMock = new Post();
        Comunidad comunidadMock = new Comunidad();
        Usuario usuarioMock = new Usuario();

        UsuarioComunidad usuarioComunidadMock = new UsuarioComunidad();
        usuarioComunidadMock.setComunidad(comunidadMock);
        usuarioComunidadMock.setUsuario(usuarioMock);

        when(servicioPosteoMock.obtenerPosteoPorId(idPost)).thenReturn(postMock);
        when(repositorioUsuarioComunidadMock.obtenerUsuarioEnComunidad(idUsuario, idComunidad))
                .thenReturn(usuarioComunidadMock);


        servicioUsuarioComunidad.compartirPosteoEnComunidad(idPost, List.of(idComunidad), idUsuario);


        verify(servicioPosteoMock).obtenerPosteoPorId(idPost);
        verify(repositorioUsuarioComunidadMock).obtenerUsuarioEnComunidad(idUsuario, idComunidad);
        verify(repositorioUsuarioComunidadMock).compartirPosteoEnComunidad(postMock, List.of(comunidadMock), usuarioMock);
    }

    @Test
    public void seDebeObtenerLasComunidadesDondeElUsuarioEsteUnido() {

        Long idUsuario = 1L;
        Usuario usuarioMock = new Usuario();
        Comunidad comunidadMock = new Comunidad();

        when(servicioUsuario.obtenerUsuarioPorId(idUsuario))
            .thenReturn(usuarioMock);
        when(repositorioUsuarioComunidadMock.obtenerComunidadesDondeELUsuarioEsteUnido(usuarioMock))
            .thenReturn(List.of(comunidadMock));

        List<Comunidad> comunidades = servicioUsuarioComunidad.obtenerComunidadesDondeELUsuarioEsteUnido(idUsuario);

        assertThat(comunidades.size(), equalTo(1));
        assertThat(comunidades.get(0), equalTo(comunidadMock));
    }

}
