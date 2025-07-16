package com.tallerwebi.integracion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.presentacion.ControladorPerfil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Image;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.User;

import javax.servlet.http.HttpSession;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.springframework.ui.Model;
public class ControladorPerfilTest {

    private ControladorPerfil controladorPerfil;
    private ServicioPerfil servicioPerfil;
    private HttpSession session;
    private Model model;
    private ServicioEstadoDeAnimo servicioEstadoDeAnimo;
    private ServicioRecomendaciones servicioRecomendaciones;
    private ServicioUsuario servicioUsuario;
    private ServicioReproduccion servicioReproduccion;
    private ServicioLike servicioLike;
    private ServicioRating servicioRating;
    @BeforeEach
    public void setup() {
        servicioLike = mock(ServicioLike.class);
        servicioPerfil = mock(ServicioPerfil.class);
        servicioEstadoDeAnimo = mock(ServicioEstadoDeAnimo.class);
        servicioRecomendaciones = mock(ServicioRecomendaciones.class);
        servicioUsuario = mock(ServicioUsuario.class);
        servicioReproduccion = mock(ServicioReproduccion.class);
        servicioRating = mock(ServicioRating.class);
        controladorPerfil = new ControladorPerfil(servicioPerfil, servicioEstadoDeAnimo, servicioRecomendaciones, servicioUsuario, servicioReproduccion, servicioLike, servicioRating);
        session = mock(HttpSession.class);
        model = mock(Model.class);
    }
    /*
    @Test
    public void perfilDebeAgregarAtributosAlModeloYRetornarVistaPerfil() throws Exception {
        // Tokens simulados
        String token = "mockToken";
        String refreshToken = "mockRefresh";

        when(session.getAttribute("token")).thenReturn(token);
        when(session.getAttribute("refreshToken")).thenReturn(refreshToken);

        // User simulado
        User user = mock(User.class);
        Image image = mock(Image.class);
        when(image.getUrl()).thenReturn("http://foto.com");
        when(user.getDisplayName()).thenReturn("UsuarioMock");
        when(user.getImages()).thenReturn(new Image[]{image});
        when(servicioPerfil.obtenerPerfilUsuario(token, refreshToken)).thenReturn(user);

        // Datos adicionales
        when(servicioPerfil.obtenerCantidadDeArtistaQueSigueElUsuario(token, refreshToken)).thenReturn(42);
        when(servicioPerfil.obtenerMejoresArtistasDelUsuario(token, refreshToken)).thenReturn(List.of("Artista1", "Artista2"));
        when(servicioPerfil.obtenerNombreDePlaylistDelUsuario(token, refreshToken)).thenReturn(List.of("Playlist1", "Playlist2"));
        when(servicioPerfil.obtenerCantidadDePlaylistDelUsuario(token, refreshToken)).thenReturn(3);

        Track track = mock(Track.class);
        ArtistSimplified artist = mock(ArtistSimplified.class);
        when(artist.getName()).thenReturn("ArtistaActual");
        when(track.getArtists()).thenReturn(new ArtistSimplified[]{artist});
        when(servicioPerfil.obtenerReproduccionActualDelUsuario(token, refreshToken)).thenReturn(track);

        // Ejecutar
        String vista = controladorPerfil.perfil(session, model);

        // Verificar modelo y vista
        verify(model).addAttribute("inicio", "Se inicio correctamente");
        verify(model).addAttribute("nombre", "UsuarioMock");
        verify(model).addAttribute("foto", "http://foto.com");
        verify(model).addAttribute("seguidos", 42);
        verify(model).addAttribute("mejores", List.of("Artista1", "Artista2"));
        verify(model).addAttribute("playlist", List.of("Playlist1", "Playlist2"));
        verify(model).addAttribute("totalPlaylist", 3);
        verify(model).addAttribute("escuchando", track);
        verify(model).addAttribute("artista", "ArtistaActual");

        assertEquals("perfil", vista);
        */

    }


