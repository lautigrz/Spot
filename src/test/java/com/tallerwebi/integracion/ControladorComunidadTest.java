package com.tallerwebi.integracion;

import com.tallerwebi.dominio.Mensaje;
import com.tallerwebi.dominio.ServicioComunidad;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import com.tallerwebi.presentacion.ControladorComunidad;
import com.tallerwebi.presentacion.dto.ChatMessage;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import se.michaelthelin.spotify.SpotifyApi;


import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ContextConfiguration(classes = {SpringWebTestConfig.class})
public class ControladorComunidadTest {

    private MockMvc mockMvc;
    private ServicioComunidad servicioComunidadMock;
    private ControladorComunidad controladorComunidad;

    private SimpMessageHeaderAccessor headerAccessorMock;

    @BeforeEach
    public void setUp() {
        servicioComunidadMock = mock(ServicioComunidad.class);
        controladorComunidad = new ControladorComunidad(servicioComunidadMock);
        headerAccessorMock = mock(SimpMessageHeaderAccessor.class);
        mockMvc = MockMvcBuilders.standaloneSetup(controladorComunidad).build();

    }


/*
    @Test
    public void debeMostrarVistaComunidadConDatosDelUsuario() throws Exception {
        // Preparar datos mock
        Usuario usuarioMock = new Usuario();
        usuarioMock.setUser("lauti");
        usuarioMock.setUrlFoto("https://foto.jpg");
        usuarioMock.setToken("token123");
        usuarioMock.setId(42L);  // fijate que seteas id también

        List<Mensaje> mensajesMock = List.of(
                new Mensaje(1L, "Hola", usuarioMock),
                new Mensaje(2L, "¿Qué tal?", usuarioMock)
        );

        // Definir comportamiento del mock
        when(servicioComunidadMock.obtenerUsuarioDeLaComunidad(anyString())).thenReturn(usuarioMock);
        when(servicioComunidadMock.obtenerMensajes()).thenReturn(mensajesMock);

        // Ejecutar y verificar
        mockMvc.perform(get("/comunidad")
                        .sessionAttr("user", usuarioMock.getUser()))
                .andExpect(status().isOk())
                .andExpect(view().name("comunidad-general"))
                .andExpect(model().attribute("usuario", usuarioMock.getUser()))
                .andExpect(model().attribute("urlFoto", usuarioMock.getUrlFoto()))
                .andExpect(model().attribute("id", usuarioMock.getId()))
                .andExpect(model().attribute("token", usuarioMock.getToken()))
                .andExpect(model().attribute("mensajes", mensajesMock));

        // Verificar que los métodos mock fueron llamados
        verify(servicioComunidadMock).obtenerUsuarioDeLaComunidad("lauti");
        verify(servicioComunidadMock).obtenerMensajes();
    }



   /* @Test
    public void debeDelegarEnServicioYRetornarElMensaje() {
        // Arrange
        ChatMessage mensajeEnviado = new ChatMessage();
        mensajeEnviado.setSender("lauti");
        mensajeEnviado.setContent("Hola");

        ChatMessage mensajeEsperado = new ChatMessage();
        mensajeEsperado.setSender("lauti");
        mensajeEsperado.setContent("Hola");

        when(servicioComunidadMock.register(mensajeEnviado, headerAccessorMock)).thenReturn(mensajeEsperado);
        // Act
        ChatMessage resultado = controladorComunidad.register(mensajeEnviado, headerAccessorMock);

        // Assert
        assertThat("lauti", equalTo(resultado.getSender()));

        assertThat("Hola", equalTo(resultado.getContent()));

    }

    */
/*
    @Test
    public void debeDelegarEnServicioYRetornarElMensajeEsperado() {
        // Arrange
        ChatMessage mensajeEnviado = new ChatMessage();
        mensajeEnviado.setContent("Hola");
        mensajeEnviado.setSender("lauti");

        String idUsuarioHeader = "5";
        Long idUsuarioEsperado = 5L;

        ChatMessage mensajeEsperado = new ChatMessage();
        mensajeEsperado.setContent("Hola");
        mensajeEsperado.setSender("lauti");

        when(servicioComunidadMock.send(mensajeEnviado, idUsuarioEsperado)).thenReturn(mensajeEsperado);

        // Act
        ChatMessage resultado = controladorComunidad.send(mensajeEnviado, idUsuarioHeader);

        // Assert
        assertThat("Hola", equalTo(resultado.getContent()));
        assertThat("lauti", equalTo(resultado.getSender()));

        verify(servicioComunidadMock).send(mensajeEnviado, idUsuarioEsperado);
    }


 */

}
