package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioUsuarioImplTest {

    private ServicioUsuarioImpl servicioUsuarioImpl;
    private RepositorioUsuario repositorioUsuario;

    @BeforeEach
    public void setUp() {
        repositorioUsuario = mock(RepositorioUsuario.class);
        servicioUsuarioImpl = new ServicioUsuarioImpl(repositorioUsuario);
    }

    @Test
    public void queAlSeguirAUnUsuarioDebeAgregarseALaListaDeSeguidores() throws Exception {
        Usuario u1 = new Usuario();
        Usuario u2 = new Usuario();
        u1.setId(1L);
        u2.setId(2L);
        when(repositorioUsuario.buscarUsuarioPorId(u1.getId())).thenReturn(u1);
        when(repositorioUsuario.buscarUsuarioPorId(u2.getId())).thenReturn(u2);
        servicioUsuarioImpl.seguirUsuario(u1.getId(), u2.getId());
        int cantidadSeguidores = 1;
        assertEquals(cantidadSeguidores, servicioUsuarioImpl.obtenerSeguidores(u2.getId()).size());
    }

    @Test
    public void queAlSeguirUnUsuarioYLuegoDejarDeSeguirloSeElimineDeSuListaDeSeguidores() throws Exception {
        Usuario u1 = new Usuario();
        Usuario u2 = new Usuario();
        u1.setId(1L);
        u2.setId(2L);

        when(repositorioUsuario.buscarUsuarioPorId(u1.getId())).thenReturn(u1);
        when(repositorioUsuario.buscarUsuarioPorId(u2.getId())).thenReturn(u2);

        servicioUsuarioImpl.seguirUsuario(u1.getId(), u2.getId());
        int cantidadSeguidores = 1;
        assertEquals(cantidadSeguidores, servicioUsuarioImpl.obtenerSeguidores(u2.getId()).size());

        servicioUsuarioImpl.dejarDeSeguirUsuario(u1.getId(), u2.getId());
        int cantidadSeguidoresPost = 0;
        assertEquals(cantidadSeguidoresPost, servicioUsuarioImpl.obtenerSeguidores(u2.getId()).size());
    }

    @Test
    public void queAlSeguirAUnUsuarioAumenteMiNumeroDeSeguidosEnUnaUnidad() throws Exception {
        Usuario u1 = new Usuario();
        Usuario u2 = new Usuario();
        u1.setId(1L);
        u2.setId(2L);

        when(repositorioUsuario.buscarUsuarioPorId(u1.getId())).thenReturn(u1);
        when(repositorioUsuario.buscarUsuarioPorId(u2.getId())).thenReturn(u2);

        servicioUsuarioImpl.seguirUsuario(u1.getId(), u2.getId());

        int cantidadSeguidos = 1;

        assertEquals(cantidadSeguidos, servicioUsuarioImpl.obtenerSeguidos(u1.getId()).size());
    }

    @Test
    public void queAlDejarDeSeguirAUnUsuarioMiNumeroDeSeguidosDisminuyaUnaUnidad() throws Exception {
        Usuario u1 = new Usuario();
        Usuario u2 = new Usuario();
        u1.setId(1L);
        u2.setId(2L);

        when(repositorioUsuario.buscarUsuarioPorId(u1.getId())).thenReturn(u1);
        when(repositorioUsuario.buscarUsuarioPorId(u2.getId())).thenReturn(u2);

        servicioUsuarioImpl.seguirUsuario(u1.getId(), u2.getId());
        int cantidadSeguidos = 1;
        assertEquals(cantidadSeguidos, servicioUsuarioImpl.obtenerSeguidos(u1.getId()).size());

        servicioUsuarioImpl.dejarDeSeguirUsuario(u1.getId(), u2.getId());
        int cantidadSeguidoresPost = 0;
        assertEquals(cantidadSeguidoresPost, servicioUsuarioImpl.obtenerSeguidos(u1.getId()).size());
    }
}
