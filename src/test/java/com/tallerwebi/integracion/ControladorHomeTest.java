package com.tallerwebi.integracion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import com.tallerwebi.presentacion.ControladorHome;
import com.tallerwebi.presentacion.dto.UsuarioDto;
import org.apache.maven.model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ModelAndView;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;

import javax.servlet.http.HttpSession;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ControladorHomeTest {


    private RepositorioArtista repositorioArtistaMock;
    private ServicioUsuario servicioUsuarioMock;
    private ServicioComunidad servicioComunidadMock;
    private ServicioInstancia servicioInstanciaMock;

    private ServicioPosteo servicioPosteoMock;
    private ServicioNotificacion servicioNotificacionMock;

    private ControladorHome controladorHome;

    @BeforeEach
    public void setup() {
        repositorioArtistaMock = mock(RepositorioArtista.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioComunidadMock = mock(ServicioComunidad.class);
        servicioInstanciaMock = mock(ServicioInstancia.class);

        servicioPosteoMock = mock(ServicioPosteo.class);

        controladorHome = new ControladorHome(repositorioArtistaMock,servicioUsuarioMock,servicioComunidadMock,servicioInstanciaMock,servicioPosteoMock);

        servicioNotificacionMock = mock(ServicioNotificacion.class);

        controladorHome = new ControladorHome(servicioUsuarioMock, servicioComunidadMock, servicioInstanciaMock, servicioNotificacionMock, servicioPosteoMock);

    }

    @Test
    public void debeRetornarVistaHome() {

        Long idUsuario = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);
        usuario.setUser("Ejemplo");

        HttpSession sessionMock = mock(HttpSession.class);
        when(sessionMock.getAttribute("user")).thenReturn(idUsuario);

        List<Comunidad> comunidadesMock = List.of(new Comunidad(), new Comunidad());


        when(servicioUsuarioMock.obtenerUsuarioPorId(idUsuario)).thenReturn(usuario);
        when(servicioComunidadMock.obtenerTodasLasComunidades()).thenReturn(comunidadesMock);
        when(servicioNotificacionMock.elUsuarioTieneNotificaciones(idUsuario)).thenReturn(true);

        ModelAndView modelAndView = controladorHome.vistaHome(sessionMock);


        assertThat(modelAndView.getViewName(), equalTo("home"));
        assertThat(modelAndView.getModel().get("usuario"), equalTo(usuario));
        assertThat(modelAndView.getModel().get("notificacion"), equalTo(true));
        assertThat(((List<?>) modelAndView.getModel().get("comunidades")).size(), equalTo(2));
    }


    @Test
    public void debeCerrarSesionYRedirigirAlLogin() {
        // Mockear HttpSession
        HttpSession sessionMock = mock(HttpSession.class);


        String resultado = controladorHome.cerrarSesion(sessionMock);


        verify(sessionMock).invalidate();


        assertThat("redirect:/login", equalTo(resultado));
    }


}
