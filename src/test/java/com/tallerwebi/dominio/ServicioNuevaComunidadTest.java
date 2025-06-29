package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioNuevaComunidadTest {

    private RepositorioNuevaComunidad repositorioNuevaComunidadMock;
    private ServicioNuevaComunidad servicioNuevaComunidad;

    @BeforeEach
    public void setUp() {
        repositorioNuevaComunidadMock = mock(RepositorioNuevaComunidad.class);
        servicioNuevaComunidad = new ServicioNuevaComunidadImpl(repositorioNuevaComunidadMock);

    }

    @Test
    public void seDebeGuardarUnaNuevaComunidadYRetornarSuID() {
     Comunidad comunidad = new Comunidad();
        comunidad.setId(3L);
        comunidad.setNombre("Comunidad de prueba");
        comunidad.setDescripcion("Descripción de prueba");
        comunidad.setUrlPortada("http://comunidad-de-prueba.com");
        comunidad.setUrlFoto("http://comunidad-de-prueba.com/imagen.jpg");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUser("Usuario de prueba");

        UsuarioComunidad usuarioComunidad = new UsuarioComunidad();
        usuarioComunidad.setId(2L);
        usuarioComunidad.setUsuario(usuario);
        usuarioComunidad.setComunidad(comunidad);




        when(repositorioNuevaComunidadMock.nuevaComunidad(any(Comunidad.class), any(Usuario.class), anyString())).thenReturn(usuarioComunidad.getId());
        Long idComunidad = servicioNuevaComunidad.nuevaComunidad(comunidad, usuario, "ADMIN");

        assertThat(idComunidad, equalTo(usuarioComunidad.getId()));

    }

}
