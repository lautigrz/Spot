package com.tallerwebi.integracion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallerwebi.dominio.*;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import com.tallerwebi.presentacion.ControladorComunidad;
import com.tallerwebi.presentacion.dto.CancionDto;
import com.tallerwebi.presentacion.dto.ChatMessage;
import com.tallerwebi.presentacion.dto.Sincronizacion;
import com.tallerwebi.presentacion.dto.UsuarioDto;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ContextConfiguration(classes = {SpringWebTestConfig.class})
public class ControladorComunidadTest {

    private MockMvc mockMvc;
    private ControladorComunidad controladorComunidad;

    private ServicioComunidad servicioComunidadMock;
    private ServicioSpotify servicioSpotify;
    private ServicioPlaylist servicioPlaylist;
    private ServicioReproduccion servicioReproduccion;

    private SimpMessageHeaderAccessor headerAccessorMock;

    @BeforeEach
    public void setUp() {
        servicioComunidadMock = mock(ServicioComunidad.class);
        servicioSpotify = mock(ServicioSpotify.class);
        servicioPlaylist = mock(ServicioPlaylist.class);
        servicioReproduccion = mock(ServicioReproduccion.class);

        controladorComunidad = new ControladorComunidad(servicioComunidadMock, servicioSpotify, servicioPlaylist, servicioReproduccion);
        headerAccessorMock = mock(SimpMessageHeaderAccessor.class);
        mockMvc = MockMvcBuilders.standaloneSetup(controladorComunidad).build();

    }

    @Test
    public void debeMostrarVistaComunidadConDatosDelUsuario() throws Exception {
        // Preparar datos mock
        Usuario usuarioMock = new Usuario();
        usuarioMock.setUser("lauti");
        usuarioMock.setUrlFoto("https://foto.jpg");
        usuarioMock.setToken("token123");
        usuarioMock.setId(42L);

        Comunidad comunidadMock = new Comunidad();
        comunidadMock.setNombre("Rock");
        comunidadMock.setId(4L);
        comunidadMock.getUsuarios().add(usuarioMock);
        usuarioMock.getComunidades().add(comunidadMock);

        List<Mensaje> mensajesMock = List.of(
                new Mensaje(2L, "Hola", usuarioMock, comunidadMock),
                new Mensaje(3L, "Hola", usuarioMock, comunidadMock)
        );

        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setId(usuarioMock.getId());
        usuarioDto.setUser(usuarioMock.getUser());
        usuarioDto.setUrlFoto(usuarioMock.getUrlFoto());
        usuarioDto.setToken(usuarioMock.getToken());

        // Definir comportamiento del mock
        when(servicioComunidadMock.obtenerUsuarioDeLaComunidad(usuarioMock.getId(), comunidadMock.getId()))
                .thenReturn(usuarioDto);

        when(servicioComunidadMock.hayAlguienEnLaComunidad(String.valueOf(comunidadMock.getId()), usuarioMock.getUser()))
                .thenReturn(true);

        when(servicioComunidadMock.obtenerMensajes(comunidadMock.getId()))
                .thenReturn(mensajesMock);

        // Ejecutar y verificar
        mockMvc.perform(get("/comunidad/{id}", comunidadMock.getId())
                        .sessionAttr("user", usuarioMock.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("comunidad-general"))
                .andExpect(model().attribute("usuario", usuarioMock.getUser()))
                .andExpect(model().attribute("urlFoto", usuarioMock.getUrlFoto()))
                .andExpect(model().attribute("id", usuarioMock.getId()))
                .andExpect(model().attribute("token", usuarioMock.getToken()))
                .andExpect(model().attribute("mensajes", mensajesMock))
                .andExpect(model().attribute("hayUsuarios", true))
                .andExpect(model().attribute("comunidad", comunidadMock.getId()))
                .andExpect(model().attribute("estaEnComunidad", true));

        UsuarioDto usuarioObtenido = servicioComunidadMock.obtenerUsuarioDeLaComunidad(usuarioMock.getId(), comunidadMock.getId());
        List<Mensaje> mensajesObtenidos = servicioComunidadMock.obtenerMensajes(comunidadMock.getId());

        assertThat(usuarioDto, equalTo(usuarioObtenido));
        assertThat(mensajesMock, equalTo(mensajesObtenidos));
    }

    @Test
    public void debeBuscarCancionesYRetornarUnaLista() throws Exception {
        // Arrange
        String textoBusqueda = "john";
        String token = "token123";

        CancionDto cancionDto = new CancionDto();
        cancionDto.setId(2L);
        cancionDto.setTitulo("Imagine");
        cancionDto.setArtista("John Lennon");

        List<CancionDto> listaSimulada = List.of(cancionDto);

        when(servicioSpotify.obtenerCancionesDeSpotify(anyString(), eq(textoBusqueda)))
                .thenReturn(listaSimulada);

        // Act
        MvcResult resultado = mockMvc.perform(get("/busqueda-cancion/" + textoBusqueda)
                        .sessionAttr("token", token))
                .andExpect(status().isOk())
                .andReturn();

        String jsonRespuesta = resultado.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        List<CancionDto> cancionesRespuesta = mapper.readValue(jsonRespuesta, new TypeReference<List<CancionDto>>() {
        });

        CancionDto resultadoCancion = cancionesRespuesta.get(0);
        assertThat(resultadoCancion.getId(), equalTo(2L));
        assertThat(resultadoCancion.getTitulo(), equalTo("Imagine"));
        assertThat(resultadoCancion.getArtista(), equalTo("John Lennon"));

    }


    @Test
    public void debeGuardarLasCancionesYRetornarLaVistaDeLaComunidad() throws Exception {
        Long idComunidad = 2L;
        Comunidad comunidadMock = new Comunidad();
        comunidadMock.setId(idComunidad);

        CancionDto cancionDto = new CancionDto();
        cancionDto.setId(2L);
        cancionDto.setTitulo("Bioe");
        cancionDto.setArtista("John Lennon");

        // Mockear el servicio para que devuelva una lista simulada
        List<CancionDto> listaSimulada = List.of(
                cancionDto
        );

        // JSON de canciones
        ObjectMapper objectMapper = new ObjectMapper();
        String cancionesJson = objectMapper.writeValueAsString(listaSimulada);

        System.out.println(cancionesJson);

        // Mocks
        when(servicioComunidadMock.obtenerComunidad(anyLong())).thenReturn(comunidadMock);

        // Ejecutar POST
        mockMvc.perform(post("/guardar-canciones/{idComunidad}", idComunidad)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cancionesJson)
                        .sessionAttr("usuario", new Object())) // si necesitás algo en sesión
                .andExpect(status().isOk())
                .andExpect(content().string("/spring/comunidad/" + idComunidad));


        assertThat(comunidadMock, equalTo(servicioComunidadMock.obtenerComunidad(idComunidad)));

        verify(servicioPlaylist).crearNuevaPlaylistConCanciones(comunidadMock, listaSimulada);
    }

    @Test
    public void debeSincronizarYRetornarSincronizacion() throws Exception {
        // Arrange
        Long idComunidad = 1L;
        String sender = "pepe";
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(sender);

        String usuarioActivo = "pepeActivo";

        Sincronizacion sincronizacionEsperada = new Sincronizacion();
        sincronizacionEsperada.setUriInicio("spotify:track:1");

        // Mock servicios
        when(servicioComunidadMock.obtenerUsuarioDeLaComunidadActivoDeLaLista(String.valueOf(idComunidad), sender))
                .thenReturn(usuarioActivo);

        when(servicioReproduccion.obtenerSincronizacion(usuarioActivo, idComunidad))
                .thenReturn(sincronizacionEsperada);

        ObjectMapper mapper = new ObjectMapper();
        String jsonMensaje = mapper.writeValueAsString(chatMessage);

        // Act
        MvcResult resultado = mockMvc.perform(post("/sincronizarme/{idComunidad}", idComunidad)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMensaje))
                .andExpect(status().isOk())
                .andReturn();


        // Assert
        String jsonRespuesta = resultado.getResponse().getContentAsString();
        Sincronizacion respuesta = mapper.readValue(jsonRespuesta, Sincronizacion.class);

        assertThat(respuesta, is(notNullValue()));
        assertThat(respuesta.getUriInicio(), equalTo("spotify:track:1"));

        verify(servicioComunidadMock).obtenerUsuarioDeLaComunidadActivoDeLaLista(String.valueOf(idComunidad), sender);
        verify(servicioReproduccion).obtenerSincronizacion(usuarioActivo, idComunidad);
    }

    @Test
    public void debeRetornarErrorCuandoFallaLaSincronizacion() throws Exception {
        // Arrange
        Long idComunidad = 2L;
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender("errorSender");

        when(servicioComunidadMock.obtenerUsuarioDeLaComunidadActivoDeLaLista(anyString(), anyString()))
                .thenThrow(new RuntimeException("Fallo"));

        ObjectMapper mapper = new ObjectMapper();
        String jsonMensaje = mapper.writeValueAsString(chatMessage);

        // Act
        MvcResult resultado = mockMvc.perform(post("/sincronizarme/{idComunidad}", idComunidad)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMensaje))
                .andExpect(status().isInternalServerError())  // Esperamos error 500
                .andReturn();


        String jsonRespuesta = resultado.getResponse().getContentAsString();
        Sincronizacion sincronizacion = mapper.readValue(jsonRespuesta, Sincronizacion.class);

        assertThat(sincronizacion, notNullValue());

        assertThat(sincronizacion.getUriInicio(), anyOf(nullValue(), equalTo("")));


    }
    @Test
    public void debeReproducirMusicaYDarStatusok() throws Exception {
        Long idComunidad = 5L;
        String token = "token-abc";
        Long idUsuario = 42L;

        mockMvc.perform(get("/reproducir/{idComunidad}", idComunidad)
                        .sessionAttr("token", token)
                        .sessionAttr("user", idUsuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/comunidad/" + idComunidad));

        // Verificamos que se llamó al servicio con los parámetros correctos
        verify(servicioReproduccion).reproducirCancion(token, idComunidad, idUsuario);
    }

    @Test
   public void debeTirarErrorAlReproducir() throws Exception {
        Long idComunidad = 5L;
        String token = "token-abc";
        Long idUsuario = 42L;

        // Simulamos que el servicio lanza excepción
        doThrow(new RuntimeException("Error de prueba"))
                .when(servicioReproduccion).reproducirCancion(token, idComunidad, idUsuario);

        mockMvc.perform(get("/reproducir/{idComunidad}", idComunidad)
                        .sessionAttr("token", token)
                        .sessionAttr("user", idUsuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));


        verify(servicioReproduccion).reproducirCancion(token, idComunidad, idUsuario);
    }
    @Test
    public void debePoderUnirseAUnaComunidadYRedireccionarALaComunidad() throws Exception {
        Long idComunidad = 10L;
        Long idUsuario = 100L;


        when(servicioComunidadMock.guardarUsuarioEnComunidad(idUsuario, idComunidad))
                .thenReturn(true);

        mockMvc.perform(get("/unirme/{idComunidad}", idComunidad)
                        .sessionAttr("user", idUsuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/comunidad/" + idComunidad));

        verify(servicioComunidadMock).guardarUsuarioEnComunidad(idUsuario, idComunidad);
    }

    @Test
    public void debeTirarErrorAlUnirseAUnaComunidad() throws Exception {
        Long idComunidad = 10L;
        Long idUsuario = 100L;

        // Simulamos que falla el guardado
        when(servicioComunidadMock.guardarUsuarioEnComunidad(idUsuario, idComunidad))
                .thenReturn(false);

        mockMvc.perform(get("/unirme/{idComunidad}", idComunidad)
                        .sessionAttr("user", idUsuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));

        verify(servicioComunidadMock).guardarUsuarioEnComunidad(idUsuario, idComunidad);
    }
    @Test
    public void debeEncontrarUsuarioEnComunidad() throws Exception {
        Long idUsuario = 1L;
        Long idComunidad = 2L;


        when(servicioComunidadMock.obtenerUsuarioDeLaComunidad(idUsuario, idComunidad))
                .thenReturn(new UsuarioDto());

        mockMvc.perform(get("/usuario-en-comunidad/{idUsuario}/{idComunidad}", idUsuario, idComunidad))
                .andExpect(status().isOk());

        verify(servicioComunidadMock).obtenerUsuarioDeLaComunidad(idUsuario, idComunidad);
    }

    @Test
    public void usuarioEnComunidadNoEncontrado() throws Exception {
        Long idUsuario = 1L;
        Long idComunidad = 2L;


        when(servicioComunidadMock.obtenerUsuarioDeLaComunidad(idUsuario, idComunidad))
                .thenReturn(null);

        mockMvc.perform(get("/usuario-en-comunidad/{idUsuario}/{idComunidad}", idUsuario, idComunidad))
                .andExpect(status().isUnauthorized());

        verify(servicioComunidadMock).obtenerUsuarioDeLaComunidad(idUsuario, idComunidad);
    }

    @Test
    public void seDebeObtenerLaCancionSonandoEnUnaComunidad() throws Exception {
        Long idComunidad = 1L;
        CancionDto cancionDto = new CancionDto();
        cancionDto.setId(7L);
        cancionDto.setUri("spotify:trackda11");

        ObjectMapper mapper = new ObjectMapper();

        when(servicioReproduccion.obtenerCancionSonandoEnLaComunidad(idComunidad)).thenReturn(cancionDto);

        MvcResult resultado = mockMvc.perform(get("/cancion/{idComunidad}", idComunidad))
                .andExpect(status().isOk())
                .andReturn();

        String jsonRespuesta = resultado.getResponse().getContentAsString();

        CancionDto respuesta = mapper.readValue(jsonRespuesta, CancionDto.class);

        assertThat(respuesta, equalTo(cancionDto));

        verify(servicioReproduccion).obtenerCancionSonandoEnLaComunidad(idComunidad);
    }

}



