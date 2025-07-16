package com.tallerwebi.integracion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.presentacion.ControladorRating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ControladorRatingTest {

    private ServicioRating servicioRating;
    private ServicioSpotify servicioSpotify;
    private ServicioUsuario servicioUsuario;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioCancion repositorioCancion;
    private ControladorRating controladorRating;

    @BeforeEach
    public void setUp(){
        servicioRating = mock(ServicioRating.class);
        servicioSpotify = mock(ServicioSpotify.class);
        servicioUsuario = mock(ServicioUsuario.class);
        repositorioUsuario = mock(RepositorioUsuario.class);
        repositorioCancion = mock(RepositorioCancion.class);

        controladorRating = new ControladorRating(servicioRating, repositorioUsuario, servicioSpotify, servicioUsuario, repositorioCancion);
    }

    @Test
    public void queSePuedaGuardarUnRatingYRedirigirAlPerfil() throws Exception {
        HttpSession session = mock(HttpSession.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        String token = "fake-token";

        when(session.getAttribute("token")).thenReturn(token);

        String vista = controladorRating.guardarRating("123",
                "CancionTest",
                "ArtistaTest",
                "http://imagen",
                "spotify:url",
                9,
                session,
                redirectAttributes);

        verify(servicioRating).guardarRating(token, "123", "CancionTest", "ArtistaTest", "http://imagen", "spotify:url", 9);
        verify(redirectAttributes).addFlashAttribute("exito", "¡Canción calificada!");
        assertEquals("redirect:/perfil", vista);
    }

    @Test
    public void queFalleElRatingYRedirijaAlFormularioConError() throws Exception {
        HttpSession sessionMock = mock(HttpSession.class);
        RedirectAttributes redirectAttributesMock = mock(RedirectAttributes.class);
        String token = "fake-token";

        when(sessionMock.getAttribute("token")).thenReturn(token);
        doThrow(new Exception("Algo salió mal")).when(servicioRating).guardarRating(
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyInt()
        );

        String vista = controladorRating.guardarRating(
                "123",
                "Canción Test",
                "Artista Test",
                "http://imagen",
                "spotify:uri",
                5,
                sessionMock,
                redirectAttributesMock
        );

        verify(redirectAttributesMock).addFlashAttribute(eq("error"), contains("Algo salió mal"));
        assertEquals("redirect:/cancion/ratear", vista);
    }

    @Test
    public void queRetorneLaVistaRatearCancionAlEntrarAlFormulario() {
        String vista = controladorRating.mostrarFormularioRating();
        assertEquals("ratear-cancion", vista);
    }

}
