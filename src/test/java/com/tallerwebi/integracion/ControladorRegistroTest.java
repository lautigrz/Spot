package com.tallerwebi.integracion;

import com.tallerwebi.dominio.Artista;
import com.tallerwebi.dominio.ServicioArtista;
import com.tallerwebi.dominio.ServicioGuardarImagen;
import com.tallerwebi.presentacion.ControladorRegistro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;


import static org.mockito.Mockito.*;

public class ControladorRegistroTest {

    private ServicioArtista servicioArtistaMock;
    private ServicioGuardarImagen servicioGuardarImagenMock;
    private ControladorRegistro controladorRegistro;

    @BeforeEach
    public void setUp() {
        servicioArtistaMock = mock(ServicioArtista.class);
        servicioGuardarImagenMock = mock(ServicioGuardarImagen.class);
        controladorRegistro = new ControladorRegistro(servicioArtistaMock, servicioGuardarImagenMock);
    }


    @Test
    public void queSePuedaRegistrarUnArtistaNuevo() throws IOException{
        String nombre = "Facu";
        String email = "facu8cabj@gmail.com";
        String password = "123456";
        MultipartFile fotoMock = mock(MultipartFile.class);
        HttpSession sessionMock = mock(HttpSession.class);
        Model modelMock = mock(Model.class);


        when(servicioArtistaMock.buscarPorEmail(email)).thenReturn(Optional.empty());

        String vista = controladorRegistro.registrarArtista(nombre, email, password, fotoMock, sessionMock, modelMock);

        //Verifica que en el servicio se guarde cualquiera que sea de clase artista
        verify(servicioArtistaMock).guardar(any(Artista.class));
        //Que si lo registro lo redirija al login
        assertEquals("redirect:/login", vista);
    }

    @Test
    public void queNoSePuedaRegistrarSiYaExisteElEmail() throws IOException{
        String nombre = "Facu";
        String email = "facu8cabj@gmail.com";
        String password = "123456";
        MultipartFile fotoMock = mock(MultipartFile.class);
        HttpSession sessionMock = mock(HttpSession.class);
        Model modelMock = mock(Model.class);

        //Al primer llamado todo ok, al segundo ya existe
        when(servicioArtistaMock.buscarPorEmail(email)).thenReturn(Optional.empty()).thenReturn(Optional.of(new Artista()));

        //Primer registro
        String primerRegistro = controladorRegistro.registrarArtista(nombre, email, password, fotoMock, sessionMock, modelMock);
        verify(servicioArtistaMock).guardar(any(Artista.class));
        assertEquals("redirect:/login", primerRegistro);

        //Segundo registro
        String segundoRegistro = controladorRegistro.registrarArtista(nombre, email, password, fotoMock, sessionMock, modelMock);
        verify(servicioArtistaMock, times(1)).guardar(any(Artista.class)); // No se guarda de nuevo
        assertEquals("registro", segundoRegistro);

    }

}
