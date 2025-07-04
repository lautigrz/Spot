package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.UsuarioDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioMensajeTest {
    private RepositorioMensaje repositorioMensajeMock;
    private ServicioMensaje servicioMensaje;


    @BeforeEach
    public void setUp() {
        repositorioMensajeMock = mock(RepositorioMensaje.class);
        servicioMensaje = new ServicioMensajeImpl(repositorioMensajeMock);
    }

    @Test
    public void testEliminarMensajeYRetornaUsuarioDtoDelMensaje() {

        String url = "http://example.com/image.jpg";
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setUrlFoto(url);
        usuarioDto.setId(2L);
        when(repositorioMensajeMock.eliminarMensaje(anyLong())).thenReturn(usuarioDto);
        UsuarioDto resultado = servicioMensaje.eliminarMensaje(1L);

        assertThat(resultado, equalTo(usuarioDto));

    }
}
