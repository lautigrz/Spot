package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ServicioComentarioTest {

    private RepositorioComentario repositorioComentarioMock;
    private ServicioUsuario servicioUsuarioMock;
    private ServicioPosteo servicioPosteoMock;
    private ServicioComentario servicioComentario;

    @BeforeEach
    public void setUp(){
        repositorioComentarioMock = mock(RepositorioComentario.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioPosteoMock = mock(ServicioPosteo.class);
        servicioComentario = new ServicioComentarioImpl(repositorioComentarioMock, servicioUsuarioMock, servicioPosteoMock);
    }

    @Test
    public void seDebeComentarUnPostCorrectamente(){
        Long idUsuario = 1L;
        Long idPosteo = 1L;
        String texto = "Comentario de prueba";

        Usuario usuarioMock = mock(Usuario.class);
        Post postMock = mock(Post.class);

        when(servicioUsuarioMock.obtenerUsuarioPorId(idUsuario)).thenReturn(usuarioMock);
        when(servicioPosteoMock.obtenerPosteoPorId(idPosteo)).thenReturn(postMock);

        servicioComentario.comentarEnPosteo(idUsuario, idPosteo, texto);

        verify(repositorioComentarioMock).guardarComentario(argThat(comentario ->
                comentario.getTexto().equals(texto)
        ));
    }

    @Test
    public void noSeDebePoderComentarUnPosteoSiElMismoEsNulo(){
        when(servicioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenReturn(mock(Usuario.class));
        when(servicioPosteoMock.obtenerPosteoPorId(anyLong())).thenReturn(null);


        assertThrows(IllegalArgumentException.class, () ->
                servicioComentario.comentarEnPosteo(1L, 2L, "Texto")
        );

        verify(repositorioComentarioMock, never()).guardarComentario(any());
    }

    @Test
    public void seDeberiaPoderObtenerComentariosDeUnPosteo(){
        Long idPosteo = 5l;
        List<Comentario> comentariosMock = List.of(new Comentario(), new Comentario());

        when(repositorioComentarioMock.obtenerComentariosDePosteo(idPosteo)).thenReturn(comentariosMock);

        List<Comentario> resultado = servicioComentario.obtenerComentariosDePosteo(idPosteo);

        assertThat(resultado, hasSize(2));
        verify(repositorioComentarioMock).obtenerComentariosDePosteo(idPosteo);
    }

}
