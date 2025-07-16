package com.tallerwebi.integracion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import com.tallerwebi.presentacion.ControladorHome;
import com.tallerwebi.presentacion.dto.PostLikeDto;
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


    private ServicioArtista servicioArtistaMock;
    private ServicioUsuario servicioUsuarioMock;
    private ServicioComunidad servicioComunidadMock;
    private ServicioInstancia servicioInstanciaMock;

    private ServicioPosteo servicioPosteoMock;
    private ServicioNotificacion servicioNotificacionMock;
    private ServicioLike servicioLikeMock;
    private ServicioUsuarioComunidad servicioUsuarioComunidadMock;
    private ServicioComentario servicioComentarioMock;
    private ServicioFavorito servicioFavoritoMock;
    private ServicioUsuarioPreescucha servicioUsuarioPreescuchaMock;
    private ControladorHome controladorHome;

    @BeforeEach
    public void setup() {

        servicioArtistaMock = mock(ServicioArtista.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioComunidadMock = mock(ServicioComunidad.class);
        servicioInstanciaMock = mock(ServicioInstancia.class);
        servicioLikeMock = mock(ServicioLike.class);
        servicioPosteoMock = mock(ServicioPosteo.class);
        servicioComentarioMock = mock(ServicioComentario.class);
        servicioUsuarioComunidadMock = mock(ServicioUsuarioComunidad.class);

        servicioNotificacionMock = mock(ServicioNotificacion.class);
        servicioFavoritoMock = mock(ServicioFavorito.class);
        servicioUsuarioPreescuchaMock = mock(ServicioUsuarioPreescucha.class);

        controladorHome = new ControladorHome(servicioArtistaMock,servicioUsuarioMock, servicioComunidadMock, servicioInstanciaMock, servicioNotificacionMock, servicioPosteoMock, servicioLikeMock, servicioUsuarioComunidadMock,servicioComentarioMock, servicioFavoritoMock, servicioUsuarioPreescuchaMock);

    }

    @Test
    public void debeRetornarVistaHome() {

        Long idUsuario = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);
        usuario.setUser("Ejemplo");

        HttpSession sessionMock = mock(HttpSession.class);
        when(sessionMock.getAttribute("user")).thenReturn(idUsuario);

        Post post = new Post();
        post.setId(10L);
        List<Post> posteos = List.of(post);
        List<Long> idsDePostConLike = List.of(10L);

        when(servicioPosteoMock.obtenerPosteosDeArtistasFavoritos(usuario)).thenReturn(posteos);
        when(servicioLikeMock.devolverIdsDePostConLikeDeUsuarioDeUnaListaDePosts(idUsuario, List.of(10L)))
                .thenReturn(idsDePostConLike);




        List<Comunidad> comunidadesMock = List.of(new Comunidad(), new Comunidad());


        when(servicioUsuarioMock.obtenerUsuarioPorId(idUsuario)).thenReturn(usuario);
        when(servicioComunidadMock.obtenerTodasLasComunidades()).thenReturn(comunidadesMock);
        when(servicioNotificacionMock.elUsuarioTieneNotificaciones(idUsuario)).thenReturn(true);

        ModelAndView modelAndView = controladorHome.vistaHome(sessionMock);

        List<PostLikeDto> posteosConLike = (List<PostLikeDto>) modelAndView.getModel().get("posteos");
        assertThat(posteosConLike.size(), equalTo(1));
        assertThat(posteosConLike.get(0).getPost(), equalTo(post));
        assertThat(posteosConLike.get(0).isLiked(), equalTo(true));
        assertThat(modelAndView.getViewName(), equalTo("home"));
        assertThat(modelAndView.getModel().get("usuario"), equalTo(usuario));
        assertThat(modelAndView.getModel().get("notificacion"), equalTo(true));
        assertThat(((List<?>) modelAndView.getModel().get("comunidades")).size(), equalTo(2));
    }

    @Test
   public void debeRetornarVistaHomeParaArtista() {
        Artista artista = new Artista();
        artista.setNombre("Artista Prueba");

        HttpSession sessionMock = mock(HttpSession.class);
        when(sessionMock.getAttribute("user")).thenReturn(null);
        when(sessionMock.getAttribute("artista")).thenReturn(artista);

        List<Post> posteos = List.of(new Post(), new Post());
        when(servicioPosteoMock.obtenerPosteosDeArtista(artista)).thenReturn(posteos);
        when(servicioComunidadMock.obtenerTodasLasComunidades()).thenReturn(List.of());

        ModelAndView modelAndView = controladorHome.vistaHome(sessionMock);

        assertThat(modelAndView.getViewName(), equalTo("home"));
        assertThat(modelAndView.getModel().get("artista"), equalTo(artista));

        List<PostLikeDto> posteosConLike = (List<PostLikeDto>) modelAndView.getModel().get("posteos");
        assertThat(posteosConLike.size(), equalTo(2));
        assertThat(posteosConLike.stream().allMatch(p -> !p.isLiked()), equalTo(true));
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
