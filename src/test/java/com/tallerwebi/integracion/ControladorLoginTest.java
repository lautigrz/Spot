package com.tallerwebi.integracion;

import com.tallerwebi.dominio.Artista;
import com.tallerwebi.dominio.ServicioArtista;
import com.tallerwebi.dominio.ServicioLoginArtista;
import com.tallerwebi.presentacion.ControladorLogin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ControladorLoginTest {

    private ServicioLoginArtista servicioLoginArtistaMock;
    private ControladorLogin controladorLogin;


    @BeforeEach
    public void setUp() {
        servicioLoginArtistaMock = mock(ServicioLoginArtista.class);
        controladorLogin = new ControladorLogin(servicioLoginArtistaMock);
    }

    @Test
    public void queSePuedaLoguearUnArtistaYGuardarEnSesion(){
        String email = "facu8cabj@gmail.com";
        String password = "123456";
        Artista artistaMock = new Artista();
        artistaMock.setEmail(email);
        HttpSession sessionMock = mock(HttpSession.class);
        Model modelMock = mock(Model.class);

        when(servicioLoginArtistaMock.login(email, password)).thenReturn(artistaMock);

        String resultado = controladorLogin.loginComoArtista(email, password, sessionMock, modelMock);
        verify(sessionMock).setAttribute("artista", artistaMock);
        assertEquals("redirect:/home", resultado);
    }

    @Test
    public void queNoSePuedaLoguearUnArtistaSiTieneInvalidasSusCredenciales(){
        String email = "facu8cabj@gmail.com";
        String password = "123456";

        HttpSession sessionMock = mock(HttpSession.class);
        Model modelMock = mock(Model.class);

        when(servicioLoginArtistaMock.login(email, password)).thenReturn(null);

        String resultado = controladorLogin.loginComoArtista(email, password, sessionMock, modelMock);

       verify(modelMock).addAttribute("error", "Email o contraseña incorrecta");
       verify(sessionMock, never()).setAttribute(eq("artista"), any());
       assertEquals("login", resultado);
    }
}
