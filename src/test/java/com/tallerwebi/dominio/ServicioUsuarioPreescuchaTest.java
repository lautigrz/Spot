package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.UsuarioPreescuchaDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ServicioUsuarioPreescuchaTest {

    private RepositorioUsuarioPreescucha repositorioUsuarioPreescuchaMock;
    private ServicioUsuario servicioUsuarioMock;
    private ServicioPreescucha servicioPreescuchaMock;
    private ServicioUsuarioPreescucha servicioUsuarioPreescucha;


    @BeforeEach
    public void setUp() {
        repositorioUsuarioPreescuchaMock = mock(RepositorioUsuarioPreescucha.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioPreescuchaMock = mock(ServicioPreescucha.class);

        servicioUsuarioPreescucha = new ServicioUsuarioPreescuchaImpl(
            repositorioUsuarioPreescuchaMock,
            servicioUsuarioMock,
            servicioPreescuchaMock
        );
    }

    @Test
    public void queSePuedaGuardarUnaPreescuchaParaUnUsuario() {


            Long idUsuario = 1L;
            Long idPreescucha = 2L;

            Usuario usuario = new Usuario();
            usuario.setId(idUsuario);

            Preescucha preescucha = new Preescucha();
            preescucha.setId(idPreescucha);

            UsuarioPreescucha usuarioPreescuchaGuardado = new UsuarioPreescucha();
            usuarioPreescuchaGuardado.setUsuario(usuario);
            usuarioPreescuchaGuardado.setPreescucha(preescucha);


            when(repositorioUsuarioPreescuchaMock.existePorUsuarioYPreescucha(idUsuario, idPreescucha))
                    .thenReturn(false);
            when(servicioUsuarioMock.obtenerUsuarioPorId(idUsuario)).thenReturn(usuario);
            when(servicioPreescuchaMock.obtenerPreescuchaLocal(idPreescucha)).thenReturn(preescucha);
            when(repositorioUsuarioPreescuchaMock.guardar(usuario, preescucha))
                    .thenReturn(usuarioPreescuchaGuardado);


            UsuarioPreescucha resultado = servicioUsuarioPreescucha.guardar(idUsuario, idPreescucha);


            assertThat(resultado, notNullValue());
            assertThat(idUsuario, equalTo(resultado.getUsuario().getId()));
            assertThat(idPreescucha, equalTo(resultado.getPreescucha().getId()));


            verify(repositorioUsuarioPreescuchaMock).existePorUsuarioYPreescucha(idUsuario, idPreescucha);
            verify(servicioUsuarioMock).obtenerUsuarioPorId(idUsuario);
            verify(servicioPreescuchaMock).obtenerPreescuchaLocal(idPreescucha);
            verify(repositorioUsuarioPreescuchaMock).guardar(usuario, preescucha);


    }

    @Test
    public void queLanceExcepcionSiYaComproLaPreescucha() {

        Long idUsuario = 1L;
        Long idPreescucha = 2L;

        when(repositorioUsuarioPreescuchaMock.existePorUsuarioYPreescucha(idUsuario, idPreescucha))
                .thenReturn(true);


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicioUsuarioPreescucha.guardar(idUsuario, idPreescucha);
        });

        assertThat(exception.getMessage(), equalTo("El usuario ya compró esta preescucha"));


        verify(repositorioUsuarioPreescuchaMock).existePorUsuarioYPreescucha(idUsuario, idPreescucha);
        verifyNoMoreInteractions(servicioUsuarioMock, servicioPreescuchaMock, repositorioUsuarioPreescuchaMock);
    }

    @Test
    public void queDevuelvaTrueSiUsuarioYaCompro() {
        Long idUsuario = 1L;
        Long idPreescucha = 2L;


        when(repositorioUsuarioPreescuchaMock.existePorUsuarioYPreescucha(idUsuario, idPreescucha))
                .thenReturn(true);

        Boolean resultado = servicioUsuarioPreescucha.comprobarSiYaCompro(idUsuario, idPreescucha);

        assertThat(resultado, equalTo(true));

        verify(repositorioUsuarioPreescuchaMock).existePorUsuarioYPreescucha(idUsuario, idPreescucha);
    }

    @Test
    public void queDevuelvaFalseSiUsuarioNoCompro() {
        Long idUsuario = 1L;
        Long idPreescucha = 2L;


        when(repositorioUsuarioPreescuchaMock.existePorUsuarioYPreescucha(idUsuario, idPreescucha))
                .thenReturn(false);

        Boolean resultado = servicioUsuarioPreescucha.comprobarSiYaCompro(idUsuario, idPreescucha);


        assertThat(resultado, equalTo(false));

        verify(repositorioUsuarioPreescuchaMock).existePorUsuarioYPreescucha(idUsuario, idPreescucha);
    }

    @Test
    public void queDevuelvaListaDeDtosOrdenada() {
        Long idUsuario = 1L;
        String orden = "ASC";

        UsuarioPreescucha up1 = new UsuarioPreescucha();
        Preescucha p1 = new Preescucha();
        p1.setFechaEscucha(LocalDateTime.now().minusDays(1));
        p1.setId(10L);
        p1.setTitulo("Titulo 1");
        Artista artista1 = new Artista();
        artista1.setNombre("Artista 1");
        p1.setArtista(artista1);
        up1.setPreescucha(p1);
        up1.setFechaCompra(java.time.LocalDateTime.now().minusDays(1));

        UsuarioPreescucha up2 = new UsuarioPreescucha();
        Preescucha p2 = new Preescucha();
        p2.setFechaEscucha(LocalDateTime.now().minusDays(1));
        p2.setId(20L);
        p2.setTitulo("Titulo 2");
        Artista artista2 = new Artista();
        artista2.setNombre("Artista 2");
        p2.setArtista(artista2);
        up2.setPreescucha(p2);
        up2.setFechaCompra(java.time.LocalDateTime.now());

        List<UsuarioPreescucha> listaMock = new ArrayList<>();
        listaMock.add(up1);
        listaMock.add(up2);

        when(repositorioUsuarioPreescuchaMock.buscarPorUsuarioOrdenado(idUsuario, orden))
                .thenReturn(listaMock);

        List<UsuarioPreescuchaDto> resultado = servicioUsuarioPreescucha.buscarPorUsuarioOrdenado(idUsuario, orden);

        assertThat(resultado, notNullValue());
        assertThat(resultado.size(), equalTo(2));

        assertThat(resultado.get(0).getNombrePreescucha(), equalTo("Titulo 1"));
        assertThat(resultado.get(0).getArtistaNombre(), equalTo("Artista 1"));

        assertThat(resultado.get(1).getNombrePreescucha(), equalTo("Titulo 2"));
        assertThat(resultado.get(1).getArtistaNombre(), equalTo("Artista 2"));

        verify(repositorioUsuarioPreescuchaMock).buscarPorUsuarioOrdenado(idUsuario, orden);
    }

}

