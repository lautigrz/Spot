package com.tallerwebi.integracion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallerwebi.dominio.*;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import com.tallerwebi.presentacion.ControladorComunidad;
import com.tallerwebi.presentacion.ControladorHome;
import com.tallerwebi.presentacion.dto.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import static org.mockito.ArgumentMatchers.any;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;


import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;


import java.io.IOException;
import java.text.ParseException;
import java.util.List;


import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ContextConfiguration(classes = {SpringWebTestConfig.class})
public class ControladorComunidadTest {

    private MockMvc mockMvc;
    private ControladorComunidad controladorComunidad;

    private ServicioUsuario servicioUsuarioMock;
    private ServicioComunidad servicioComunidadMock;
    private ServicioSpotify servicioSpotify;
    private ServicioPlaylist servicioPlaylistMock;
    private ServicioReproduccion servicioReproduccion;
    private ServicioGuardarImagen servicioGuardarImagen;
    private ServicioUsuarioComunidad servicioUsuarioComunidadMock;
    private ServicioRecomedacionComunidad servicioRecomedacionComunidadMock;
    private ServicioEventoCombinado servicioEventoCombinadoMock;
    private HttpSession sessionMock;


    @BeforeEach
    public void setUp() {
        servicioComunidadMock = mock(ServicioComunidad.class);
        servicioRecomedacionComunidadMock = mock(ServicioRecomedacionComunidad.class);
        servicioSpotify = mock(ServicioSpotify.class);
        servicioPlaylistMock = mock(ServicioPlaylist.class);
        servicioReproduccion = mock(ServicioReproduccion.class);
        servicioGuardarImagen = mock(ServicioGuardarImagen.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioEventoCombinadoMock = mock(ServicioEventoCombinado.class);
        servicioUsuarioComunidadMock = mock(ServicioUsuarioComunidad.class);
        controladorComunidad = new ControladorComunidad(servicioComunidadMock, servicioSpotify, servicioPlaylistMock, servicioReproduccion, servicioGuardarImagen,
                servicioUsuarioMock, servicioUsuarioComunidadMock, servicioRecomedacionComunidadMock, servicioEventoCombinadoMock);

        mockMvc = MockMvcBuilders.standaloneSetup(controladorComunidad).build();

        sessionMock = mock(HttpSession.class);
    }

    @Test
    public void testComunidadConUsuarioEnComunidad() throws IOException, ParseException, SpotifyWebApiException, org.apache.hc.core5.http.ParseException {
        Long idUsuario = 1L;
        Long idComunidad = 100L;

        // Simulamos datos

        Usuario usuario = mock(Usuario.class);
        when(usuario.getId()).thenReturn(idUsuario);
        when(usuario.getUser()).thenReturn("usuarioTest");
        when(usuario.getUrlFoto()).thenReturn("urlFotoTest");
        when(usuario.getToken()).thenReturn("tokenTest");

        UsuarioDto usuarioDtoMock = new UsuarioDto();
        usuarioDtoMock.setId(idUsuario);
        usuarioDtoMock.setUser("usuarioTest");
        usuarioDtoMock.setUrlFoto("urlFotoTest");
        usuarioDtoMock.setToken("tokenTest");

        UsuarioComunidad usuarioComunidadMock = mock(UsuarioComunidad.class);
        Comunidad comunidadMock = new Comunidad();
        List<Playlist> playlistsMock = List.of(new Playlist());
        List<Mensaje> mensajesMock = List.of(new Mensaje());
        List<UsuarioDto> usuariosActivosMock = List.of(new UsuarioDto());

        when(sessionMock.getAttribute("user")).thenReturn(idUsuario);
        when(servicioUsuarioComunidadMock.obtenerUsuarioEnComunidad(anyLong(), anyLong())).thenReturn(usuarioComunidadMock);
        when(servicioUsuarioComunidadMock.obtenerUsuarioEnComunidad(anyLong(), anyLong()).getUsuario()).thenReturn(usuario);
        when(servicioComunidadMock.obtenerComunidad(idComunidad)).thenReturn(comunidadMock);
        when(servicioComunidadMock.hayAlguienEnLaComunidad(String.valueOf(idComunidad), usuarioDtoMock.getUser())).thenReturn(true);
        when(servicioPlaylistMock.obtenerPlaylistsRelacionadasAUnaComunidad(idComunidad)).thenReturn(playlistsMock);
        when(servicioComunidadMock.obtenerMensajes(idComunidad)).thenReturn(mensajesMock);
        when(servicioUsuarioMock.obtenerUsuarioDtoPorId(idUsuario)).thenReturn(usuarioDtoMock);
        when(servicioComunidadMock.obtenerUsuariosDeLaComunidad(idComunidad)).thenReturn(usuariosActivosMock);
        when(servicioReproduccion.estaEscuchandoMusica(anyString(), anyLong())).thenReturn(true);
        when(servicioRecomedacionComunidadMock.obtenerRecomendacionesPorComunidadQueNoFueronLeidas(anyLong())).thenReturn(List.of(new Recomendacion()));
        when(servicioUsuarioComunidadMock.obtenerComunidadesDondeELUsuarioEsteUnido(anyLong())).thenReturn(List.of(comunidadMock));
        ModelMap model = new ModelMap();
        ModelAndView mav = controladorComunidad.comunidad(sessionMock, idComunidad, model);

        // Verificar nombre de la vista
        assertThat(mav.getViewName(), equalTo("comunidad-general"));

        // Verificar que el modelo tenga los atributos correctos
        assertThat(mav.getModel(), hasEntry("usuario", usuarioDtoMock.getUser()));
        assertThat(mav.getModel(), hasEntry("urlFoto", usuarioDtoMock.getUrlFoto()));
        assertThat(mav.getModel(), hasEntry("id", usuarioDtoMock.getId()));
        assertThat(mav.getModel(), hasEntry("token", usuarioDtoMock.getToken()));
        assertThat(mav.getModel(), hasEntry("hayUsuarios", false));
        assertThat(mav.getModel(), hasEntry("playlistsDeLaComunidad", playlistsMock));
        assertThat(mav.getModel(), hasEntry("mensajes", mensajesMock));
        assertThat(mav.getModel(), hasEntry("fotoUsuario", usuarioDtoMock.getUrlFoto()));
        assertThat(mav.getModel(), hasEntry("recomendaciones", servicioRecomedacionComunidadMock.obtenerRecomendacionesPorComunidadQueNoFueronLeidas(anyLong())));
        assertThat(mav.getModel(), hasEntry("comunidad", comunidadMock));
        assertThat(mav.getModel(), hasEntry("usuarioComunidad", servicioUsuarioComunidadMock.obtenerComunidadesDondeELUsuarioEsteUnido(1L)));
        assertThat(mav.getModel(), hasEntry("estaEnComunidad", true));
        assertThat(mav.getModel(), hasEntry("usuariosActivos", usuariosActivosMock));
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
        when(servicioGuardarImagen.guardarImagenDePlaylist(any(MultipartFile.class)))
                .thenReturn("https://example.com/playlist.jpg");

        // Ejecutar POST
        MockMultipartFile imagen = new MockMultipartFile(
                "imagen", // nombre del parámetro
                "playlist.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image-content".getBytes()
        );

        mockMvc.perform(multipart("/guardar-canciones/{idComunidad}", idComunidad)
                        .file(imagen)
                        .param("nombre", "Mi Playlist")
                        .param("canciones", cancionesJson)
                        .sessionAttr("usuario", new Object()))
                .andExpect(status().isOk())
                .andExpect(content().string("/spring/comunidad/" + idComunidad));

        assertThat(comunidadMock, equalTo(servicioComunidadMock.obtenerComunidad(idComunidad)));

        verify(servicioPlaylistMock).crearNuevaPlaylistConCanciones(
                any(Comunidad.class),
                anyList(),
                anyString(),
                anyString()
        );

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
                .andExpect(redirectedUrl("/comunidad/" + idComunidad));


        verify(servicioReproduccion).reproducirCancion(token, idComunidad, idUsuario);
    }

    @Test
    public void debePoderUnirseAUnaComunidadYRedireccionarALaComunidad() throws Exception {
        Long idComunidad = 10L;
        Long idUsuario = 100L;

        Usuario usuarioMock = mock(Usuario.class);
        Comunidad comunidadMock = mock(Comunidad.class);

        when(servicioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenReturn(usuarioMock);
        when(servicioComunidadMock.obtenerComunidad(anyLong())).thenReturn(comunidadMock);

        when(servicioUsuarioComunidadMock.agregarUsuarioAComunidad(any(Usuario.class), any(Comunidad.class), anyString()))
                .thenReturn(true);

        mockMvc.perform(get("/unirme/{idComunidad}", idComunidad)
                        .sessionAttr("user", idUsuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/comunidad/" + idComunidad));

        verify(servicioUsuarioComunidadMock).agregarUsuarioAComunidad(any(Usuario.class), any(Comunidad.class), anyString());
    }

    @Test
    public void debeTirarErrorAlUnirseAUnaComunidad() throws Exception {
        Long idComunidad = 10L;
        Long idUsuario = 100L;

        Usuario usuarioMock = mock(Usuario.class);
        Comunidad comunidadMock = mock(Comunidad.class);

        when(servicioUsuarioMock.obtenerUsuarioPorId(idUsuario)).thenReturn(usuarioMock);
        when(servicioComunidadMock.obtenerComunidad(idComunidad)).thenReturn(comunidadMock);

        when(servicioUsuarioComunidadMock.agregarUsuarioAComunidad(any(Usuario.class), any(Comunidad.class), anyString()))
                .thenReturn(false);

        mockMvc.perform(get("/unirme/{idComunidad}", idComunidad)
                        .sessionAttr("user", idUsuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));

        verify(servicioUsuarioComunidadMock).agregarUsuarioAComunidad(any(Usuario.class), any(Comunidad.class), anyString());
    }

    @Test
    public void debeEncontrarUsuarioEnComunidad() throws Exception {
        Long idUsuario = 1L;
        Long idComunidad = 2L;

        UsuarioComunidad usuarioComunidadMock = mock(UsuarioComunidad.class);
        when(servicioUsuarioComunidadMock.obtenerUsuarioEnComunidad(idUsuario, idComunidad))
                .thenReturn(usuarioComunidadMock);

        mockMvc.perform(get("/usuario-en-comunidad/{idUsuario}/{idComunidad}", idUsuario, idComunidad))
                .andExpect(status().isOk());

        verify(servicioUsuarioComunidadMock).obtenerUsuarioEnComunidad(idUsuario, idComunidad);
    }

    @Test
    public void usuarioEnComunidadNoEncontrado() throws Exception {
        Long idUsuario = 1L;
        Long idComunidad = 2L;

        UsuarioComunidad usuarioComunidadMock = mock(UsuarioComunidad.class);
        when(servicioUsuarioComunidadMock.obtenerUsuarioEnComunidad(idUsuario, idComunidad))
                .thenReturn(null);

        mockMvc.perform(get("/usuario-en-comunidad/{idUsuario}/{idComunidad}", idUsuario, idComunidad))
                .andExpect(status().isUnauthorized());

        verify(servicioUsuarioComunidadMock).obtenerUsuarioEnComunidad(idUsuario, idComunidad);
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

    @Test
    public void testBuscarComunidadDevuelveListaOk() throws Exception {

        ComunidadDto comunidad = new ComunidadDto();
        comunidad.setId(1L);
        comunidad.setNombre("Test Comunidad");
        comunidad.setUrlFoto("foto.jpg");

        List<ComunidadDto> lista = List.of(comunidad);

        Mockito.when(servicioComunidadMock.buscarComunidadesPorNombre("test"))
                .thenReturn(lista);

        mockMvc.perform(get("/buscar-comunidad/test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void debeAbandonarUnaComunidadYRedireccionaAHome() throws Exception {

        Long idComunidad = 1L;
        Long idUsuario = 2L;

        when(servicioUsuarioComunidadMock.eliminarUsuarioDeComunidad(idUsuario, idComunidad))
                .thenReturn(true);


        mockMvc.perform(post("/abandonar-comunidad/{idComunidad}/{idUsuario}", idComunidad, idUsuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(servicioUsuarioComunidadMock).eliminarUsuarioDeComunidad(idUsuario, idComunidad);
    }


    @Test
    public void compartirPosteoEnComunidad_DeberiaRetornarOk() throws Exception {
        Long postId = 1L;
        Long usuarioId = 2L;
        List<Long> comunidades = List.of(5L, 6L);


        doNothing().when(servicioUsuarioComunidadMock).compartirPosteoEnComunidad(postId, comunidades, usuarioId);

        mockMvc.perform(post("/compartir-post/{idPost}/{idUsuario}", postId, usuarioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[5,6]"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Post compartido correctamente")));

        verify(servicioUsuarioComunidadMock).compartirPosteoEnComunidad(postId, comunidades, usuarioId);
    }

    @Test
   public void compartirPosteoEnComunidad_DeberiaRetornarError() throws Exception {
        Long postId = 1L;
        Long usuarioId = 2L;
        List<Long> comunidades = List.of(5L, 6L);

        doThrow(new RuntimeException("Error")).when(servicioUsuarioComunidadMock)
                .compartirPosteoEnComunidad(postId, comunidades, usuarioId);

        mockMvc.perform(post("/compartir-post/{idPost}/{idUsuario}", postId, usuarioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[5,6]"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error al compartir el post")));

        verify(servicioUsuarioComunidadMock).compartirPosteoEnComunidad(postId, comunidades, usuarioId);
    }
}



