package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ServicioLikeTest {

    private RepositorioLike repositorioLikeMock;
    private ServicioUsuario servicioUsuarioMock;
    private ServicioPosteo servicioPosteoMock;
    private ServicioLike servicioLike;


    @BeforeEach
    public void setUp() {
        repositorioLikeMock = mock(RepositorioLike.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioPosteoMock = mock(ServicioPosteo.class);
        servicioLike = new ServicioLikeImpl(repositorioLikeMock, servicioUsuarioMock, servicioPosteoMock);
    }

    @Test
    public void seDebeDarLikeAPosteo() {
        Post posteo = mock(Post.class);
        Usuario usuario = mock(Usuario.class);

        when(servicioPosteoMock.obtenerPosteoPorId(anyLong())).thenReturn(posteo);
        when(servicioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenReturn(usuario);

        servicioLike.darLikeAPosteo(1L, 1L);

        verify(repositorioLikeMock).darLikeAPosteo(any(Post.class), any(Usuario.class));

    }

    @Test
    public void seDebeDarLikeAPosteoPeroNoEncuentraUsuarioYLanzaExcepcion() {
        Post posteo = mock(Post.class);


        when(servicioPosteoMock.obtenerPosteoPorId(anyLong())).thenReturn(posteo);
        when(servicioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenReturn(null);

      assertThrows(IllegalArgumentException.class, () -> {
            servicioLike.darLikeAPosteo(1L, 1L);
        });

      verify(repositorioLikeMock, never()).darLikeAPosteo(any(Post.class), any(Usuario.class));

    }

    @Test
    public void seDebeQuitarLikeAPosteo() {
        Post posteo = mock(Post.class);
        Usuario usuario = mock(Usuario.class);

        when(servicioPosteoMock.obtenerPosteoPorId(anyLong())).thenReturn(posteo);
        when(servicioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenReturn(usuario);

        servicioLike.quitarLikeAPosteo(1L, 1L);

        verify(repositorioLikeMock).quitarLikeAPosteo(any(Post.class), any(Usuario.class));

    }

    @Test
    public void seDebeQuitarLikeAPosteoPeroNoEncuentraUsuarioYLanzaExcepcion() {
        Post posteo = mock(Post.class);

        when(servicioPosteoMock.obtenerPosteoPorId(anyLong())).thenReturn(posteo);
        when(servicioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            servicioLike.quitarLikeAPosteo(1L, 1L);
        });

        verify(repositorioLikeMock, never()).quitarLikeAPosteo(any(Post.class), any(Usuario.class));

    }

    @Test
    public void seDebeObtenerPostConLikeDeUsuario() {
        Usuario usuario = mock(Usuario.class);
        when(servicioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenReturn(usuario);

        when(repositorioLikeMock.obtenerPostConLikeDeUsuario(usuario)).thenReturn(List.of(new Post(), new Post()));

        List<Post> post = servicioLike.obtenerPostConLikeDeUsuario(1L);

        assertThat(post.size(), equalTo(2));

    }

    @Test
    public void seDebeObtenerPostConLikeDeUsuarioRetornaUnaListaVaciaPorUsuarioNull() {

        when(servicioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenReturn(null);


        List<Post> post = servicioLike.obtenerPostConLikeDeUsuario(1L);

        assertThat(post.size(), equalTo(0));

    }

    @Test
    public void seDebeDevolverIdsDePostConLikeDeUsuarioDeUnaListaDePosts() {
        List<Long> ids = List.of(1L, 2L, 3L);
        when(repositorioLikeMock.devolverIdsDePostConLikeDeUsuarioDeUnaListaDePosts(anyLong(), eq(ids))).thenReturn(List.of(1L, 2L));

        List<Long> result = servicioLike.devolverIdsDePostConLikeDeUsuarioDeUnaListaDePosts(1L, ids);

        assertThat(result.size(), equalTo(2));
        assertThat(result, equalTo(List.of(1L, 2L)));
    }

}
