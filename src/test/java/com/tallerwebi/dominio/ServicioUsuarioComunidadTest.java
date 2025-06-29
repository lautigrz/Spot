package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioUsuarioComunidadTest {

    private RepositorioUsuarioComunidad repositorioUsuarioComunidadMock;
    private ServicioUsuarioComunidad servicioUsuarioComunidad;
    @BeforeEach
    public void setUp() {
      repositorioUsuarioComunidadMock = mock(RepositorioUsuarioComunidad.class);
      servicioUsuarioComunidad = new ServicioUsuarioComunidadImpl(repositorioUsuarioComunidadMock);

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
}
