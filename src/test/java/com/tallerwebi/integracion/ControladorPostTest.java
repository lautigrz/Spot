package com.tallerwebi.integracion;

import com.tallerwebi.dominio.Artista;
import com.tallerwebi.dominio.ServicioLike;
import com.tallerwebi.dominio.ServicioPosteo;
import com.tallerwebi.presentacion.ControladorPost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class ControladorPostTest {

    private ServicioPosteo servicioPosteoMock;
    private ServicioLike servicioLikeMock;
    private ControladorPost controladorPost;
    @BeforeEach
    public void setUp() {
        servicioPosteoMock = mock(ServicioPosteo.class);
        servicioLikeMock = mock(ServicioLike.class);
        controladorPost = new ControladorPost(servicioPosteoMock, servicioLikeMock);

    }

    @Test
    public void debePublicarPosteoYRedirigirCuandoArtistaEstaEnSesion() {

        HttpSession sessionMock = mock(HttpSession.class);
        Artista artista = new Artista();
        when(sessionMock.getAttribute("artista")).thenReturn(artista);

        String texto = "Hola mundo";


        String resultado = controladorPost.postearTexto(texto, sessionMock);


        assertThat(resultado, equalTo("redirect:/home"));
        verify(servicioPosteoMock).publicarPosteo(eq(artista), eq(texto));
    }

    @Test
    public void debeRedirigirCuandoArtistaEsNull() {

        HttpSession sessionMock = mock(HttpSession.class);
        when(sessionMock.getAttribute("artista")).thenReturn(null);

        String texto = "Texto cualquiera";


        String resultado = controladorPost.postearTexto(texto, sessionMock);


        assertThat(resultado, equalTo("redirect:/home"));
        verify(servicioPosteoMock, never()).publicarPosteo(any(), any());
    }

    @Test
    public void debeDarLikeYRetornarRespuestaCorrecta() {

        Long idPosteo = 1L;
        Long idUsuario = 2L;


        ResponseEntity<?> response = controladorPost.darLike(idPosteo, idUsuario);


        verify(servicioLikeMock).darLikeAPosteo(idPosteo, idUsuario);


        assertThat(response.getStatusCodeValue(), equalTo(200));
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertThat(body.get("mensaje"), equalTo("Like dado correctamente"));
    }

    @Test
    void debeQuitarLikeYRetornarRespuestaCorrecta() {

        Long idPosteo = 3L;
        Long idUsuario = 4L;


        ResponseEntity<?> response = controladorPost.quitarLike(idPosteo, idUsuario);


        verify(servicioLikeMock).quitarLikeAPosteo(idPosteo, idUsuario);


        assertThat(response.getStatusCodeValue(), equalTo(200));
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertThat(body.get("mensaje"), equalTo("dislike dado correctamente"));
    }
}
