package com.tallerwebi.integracion;

import com.tallerwebi.dominio.*;
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
    private ServicioUsuario servicioUsuarioMock;
    private ServicioPreescucha servicioPreescuchaMock;
    private ControladorArtista controladorArtista;
    private ServicioArtista servicioArtistaMock;

    @BeforeEach
    public void setUp() {
        servicioFavoritoMock = mock(ServicioFavorito.class);
        spotifyApiMock = mock(SpotifyApi.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioPreescuchaMock = mock(ServicioPreescucha.class);
        servicioArtistaMock = mock(ServicioArtista.class);

        controladorArtista = new ControladorArtista(servicioFavoritoMock,spotifyApiMock,servicioUsuarioMock,servicioPreescuchaMock, servicioArtistaMock);
    }


    /*

    @Test
    public void queSePuedaComprarUnAlbumDePreescucha(){
        String artistaId = "4xt9SaC07YZvUxwzNEMkyS"; // Ruben Blades
        String albumId = "3KuXEGcqLcnEYWnn3OEGy0";
        Long usuarioId = 10L;


        HttpSession sessionMock = mock(HttpSession.class);
        when(sessionMock.getAttribute("user")).thenReturn(usuarioId);

        Usuario usuarioMock = mock(Usuario.class);
        when(servicioUsuarioMock.obtenerUsuarioPorId(usuarioId)).thenReturn(usuarioMock);

        when(servicioPreescuchaMock.yaComproPreescucha(albumId,usuarioMock)).thenReturn(false);

        String resultado = controladorArtista.comprarPreescucha(artistaId, sessionMock, albumId);

        verify(servicioUsuarioMock).obtenerUsuarioPorId(usuarioId);
        verify(servicioPreescuchaMock).yaComproPreescucha(albumId,usuarioMock);
        verify(servicioPreescuchaMock).comprarPreescucha(albumId,usuarioMock);
        assertEquals("redirect:/perfil", resultado);



    @Test
    public void queNoSePuedaComprarAlbumPreescuchaSiYaLoCompro(){
        String artistaId = "4xt9SaC07YZvUxwzNEMkyS"; // Ruben Blades
        String albumId = "3KuXEGcqLcnEYWnn3OEGy0";
        Long usuarioId = 10L;

        HttpSession sessionMock = mock(HttpSession.class);
        when(sessionMock.getAttribute("user")).thenReturn(usuarioId);

        Usuario usuarioMock = mock(Usuario.class);
        when(servicioUsuarioMock.obtenerUsuarioPorId(usuarioId)).thenReturn(usuarioMock);

        when(servicioPreescuchaMock.yaComproPreescucha(albumId, usuarioMock)).thenReturn(true);

        String resultado = controladorArtista.comprarPreescucha(artistaId, sessionMock, albumId);

        verify(servicioUsuarioMock).obtenerUsuarioPorId(usuarioId);
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
        when(servicioUsuarioMock.obtenerUsuarioPorId(usuarioId)).thenReturn(usuarioMock);

        String resultado = controladorArtista.agregarAFavoritos(artistaId, sessionMock);

        verify(servicioUsuarioMock).obtenerUsuarioPorId(usuarioId);
        verify(servicioFavoritoMock).agregarFavorito(artistaId, usuarioMock);
        assertEquals("redirect:/perfil", resultado);
    }

    @Test
    public void queSePuedaAgregarUnArtistaLocalAFavoritos(){
        Long artistaLocalId = 10L;
        String idLocalEsperado = "LOCAL_" + artistaLocalId;
        Long usuarioId = 5L;

        HttpSession sessionMock = mock(HttpSession.class);
        when(sessionMock.getAttribute("user")).thenReturn(usuarioId);

        Usuario usuarioMock = mock(Usuario.class);
        when(servicioUsuarioMock.obtenerUsuarioPorId(usuarioId)).thenReturn(usuarioMock);

        String resultado = controladorArtista.agregarFavoritoLocal(artistaLocalId, sessionMock);

        verify(servicioUsuarioMock).obtenerUsuarioPorId(usuarioId);
        verify(servicioFavoritoMock).agregarFavorito(idLocalEsperado, usuarioMock);
        assertEquals("redirect:/perfil", resultado);
    }

     */
    }


