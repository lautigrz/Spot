package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class ServicioComunidadImplTest {

    private RepositorioUsuario repositorioUsuarioMock;

    private RepositorioComunidad repositorioComunidadMock;

    private ServicioComunidad servicioComunidad;
/*
    @BeforeEach
    public void setUp() {
        repositorioUsuarioMock = mock(RepositorioUsuario.class);
        repositorioComunidadMock = mock(RepositorioComunidad.class);
        servicioComunidad = new ServicioComunidadImpl(repositorioUsuarioMock,repositorioComunidadMock);
    }


    @Test
    public void seDebeGuardarUnMensaje(){
        String mensaje = "Hola como estas";
        Long idUsuario = 1L;

        servicioComunidad.guardarMensaje(mensaje,idUsuario);

        verify(repositorioComunidadMock).guardarMensajeDeLaComunidad(mensaje,idUsuario);

    }

    @Test
    public void debeDevolverUnaListaDeMensajes(){
        Usuario usuario1 = new Usuario();
        String texto = "Hola como estas";

        Usuario usuario2 = new Usuario();
        String texto1 = "Hola, bien";

        Mensaje mensaje1 = new Mensaje();
        mensaje1.setTexto(texto);
        mensaje1.setId(1L);
        mensaje1.setUsuario(usuario1);

        Mensaje mensaje2 = new Mensaje();
        mensaje1.setTexto(texto1);
        mensaje1.setId(2L);
        mensaje1.setUsuario(usuario2);

        List<Mensaje> mensajes = new ArrayList<>();
        mensajes.add(mensaje1);
        mensajes.add(mensaje2);

        when(repositorioComunidadMock.obtenerMensajesDeComunidad()).thenReturn( mensajes);

        assertThat(mensajes, equalTo( servicioComunidad.obtenerMensajes()));

    }

 */

}
