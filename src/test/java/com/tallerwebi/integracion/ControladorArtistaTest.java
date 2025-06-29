package com.tallerwebi.integracion;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.ServicioFavorito;
import com.tallerwebi.dominio.ServicioPreescucha;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.presentacion.ControladorArtista;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.michaelthelin.spotify.SpotifyApi;

import javax.servlet.http.HttpSession;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ControladorArtistaTest {

    private ServicioFavorito servicioFavoritoMock;
    private SpotifyApi spotifyApiMock;
    private RepositorioUsuario repositorioUsuarioMock;
    private ServicioPreescucha servicioPreescuchaMock;
    private ControladorArtista controladorArtista;

    @BeforeEach
    public void setUp() {
        servicioFavoritoMock = mock(ServicioFavorito.class);
        spotifyApiMock = mock(SpotifyApi.class);
        repositorioUsuarioMock = mock(RepositorioUsuario.class);
        servicioPreescuchaMock = mock(ServicioPreescucha.class);

        controladorArtista = new ControladorArtista(servicioFavoritoMock,spotifyApiMock,repositorioUsuarioMock,servicioPreescuchaMock);
    }


    @Test
    public void queSePuedaComprarUnAlbumDePreescucha(){
        String artistaId = "4xt9SaC07YZvUxwzNEMkyS"; // Ruben Blades
        String albumId = "3KuXEGcqLcnEYWnn3OEGy0";
        Long usuarioId = 10L;


        HttpSession sessionMock = mock(HttpSession.class);
        when(sessionMock.getAttribute("user")).thenReturn(usuarioId);

        Usuario usuarioMock = mock(Usuario.class);
        when(repositorioUsuarioMock.buscarUsuarioPorId(usuarioId)).thenReturn(usuarioMock);

        when(servicioPreescuchaMock.yaComproPreescucha(albumId,usuarioMock)).thenReturn(false);

        String resultado = controladorArtista.comprarPreescucha(artistaId, sessionMock, albumId);

        verify(repositorioUsuarioMock).buscarUsuarioPorId(usuarioId);
        verify(servicioPreescuchaMock).yaComproPreescucha(albumId,usuarioMock);
        verify(servicioPreescuchaMock).comprarPreescucha(albumId,usuarioMock);
        assertEquals("redirect:/perfil", resultado);
        }


    @Test
    public void queNoSePuedaComprarAlbumPreescuchaSiYaLoCompro(){
        String artistaId = "4xt9SaC07YZvUxwzNEMkyS"; // Ruben Blades
        String albumId = "3KuXEGcqLcnEYWnn3OEGy0";
        Long usuarioId = 10L;

        HttpSession sessionMock = mock(HttpSession.class);
        when(sessionMock.getAttribute("user")).thenReturn(usuarioId);

        Usuario usuarioMock = mock(Usuario.class);
        when(repositorioUsuarioMock.buscarUsuarioPorId(usuarioId)).thenReturn(usuarioMock);

        when(servicioPreescuchaMock.yaComproPreescucha(albumId, usuarioMock)).thenReturn(true);

        String resultado = controladorArtista.comprarPreescucha(artistaId, sessionMock, albumId);

        verify(repositorioUsuarioMock).buscarUsuarioPorId(usuarioId);
        verify(servicioPreescuchaMock).yaComproPreescucha(albumId, usuarioMock);
        verify(servicioPreescuchaMock, never()).comprarPreescucha(anyString(), any()); //Nunca se ejecuta el metodo de que compre la preescucha
        assertEquals("redirect:/perfil", resultado);
    }

    @Test
    public void queSePuedaAgregarUnArtistaAFavoritos(){
        String artistaId = "4xt9SaC07YZvUxwzNEMkyS";
        Long usuarioId = 5L;

        HttpSession sessionMock = mock(HttpSession.class);
        when(sessionMock.getAttribute("user")).thenReturn(usuarioId);

        Usuario usuarioMock = mock(Usuario.class);
        when(repositorioUsuarioMock.buscarUsuarioPorId(usuarioId)).thenReturn(usuarioMock);

        String resultado = controladorArtista.agregarAFavoritos(artistaId, sessionMock);

        verify(repositorioUsuarioMock).buscarUsuarioPorId(usuarioId);
        verify(servicioFavoritoMock).agregarFavorito(artistaId, usuarioMock);
        assertEquals("redirect:/perfil", resultado);
    }
    }


