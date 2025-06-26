package com.tallerwebi.integracion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.presentacion.ControladorNuevaComunidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ControladorNuevaComunidadTest {

    private MockMvc mockMvc;
    private ServicioNuevaComunidad servicioNuevaComunidadMock;
    private ServicioGuardarImagen servicioGuardarImagenMock;
    private ServicioUsuario servicioUsuarioMock;
    private ControladorNuevaComunidad controladorNuevaComunidad;

    @BeforeEach
    public void setup() {
        servicioNuevaComunidadMock = org.mockito.Mockito.mock(ServicioNuevaComunidad.class);
        servicioGuardarImagenMock = org.mockito.Mockito.mock(ServicioGuardarImagen.class);
        servicioUsuarioMock = org.mockito.Mockito.mock(ServicioUsuario.class);
        controladorNuevaComunidad = new ControladorNuevaComunidad(servicioNuevaComunidadMock, servicioGuardarImagenMock, servicioUsuarioMock);
        mockMvc = MockMvcBuilders.standaloneSetup(controladorNuevaComunidad).build();

    }

    @Test
    public void testMostrarFormularioNuevaComunidad() throws Exception {
        Model model = new ConcurrentModel();

        String vista = controladorNuevaComunidad.mostrarFormularioNuevaComunidad(model);


        assertThat("nueva-comunidad",equalTo(vista));
        assertTrue(model.containsAttribute("comunidad"));

    }

    @Test
    public void testCrearComunidad() throws Exception {
        Comunidad comunidad = new Comunidad();
        MockMultipartFile fotoPerfil = new MockMultipartFile("fotoPerfil", "foto.jpg", "image/jpeg", "fake-image".getBytes());
        MockMultipartFile fotoPortada = new MockMultipartFile("fotoPortada", "portada.jpg", "image/jpeg", "fake-image".getBytes());

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", 1L);

        when(servicioGuardarImagenMock.guardarImagenPerfilDeComunidad(fotoPerfil)).thenReturn("perfil.jpg");
        when(servicioGuardarImagenMock.guardarImagenPortadaDeComunidad(fotoPortada)).thenReturn("portada.jpg");
        Usuario usuario = new Usuario();
        when(servicioUsuarioMock.obtenerUsuarioPorId(1L)).thenReturn(usuario);
        when(servicioNuevaComunidadMock.nuevaComunidad(comunidad, usuario, "Admin")).thenReturn(42L);

        // Act
        String vista = controladorNuevaComunidad.crearComunidad(comunidad, fotoPerfil, fotoPortada, session);

       assertThat(vista,equalTo("redirect:/comunidad/" + 42));

    }


}
