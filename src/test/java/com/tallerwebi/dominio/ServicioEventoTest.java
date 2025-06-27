package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ServicioEventoTest {

    private ServicioEvento servicioEvento;
    private ServicioComunidad servicioComunidadMock;
    private RepositorioEvento repositorioEventoMock;

    @BeforeEach
    public void setUp() {
        servicioComunidadMock = mock(ServicioComunidad.class);
        repositorioEventoMock = mock(RepositorioEvento.class);
        servicioEvento = new ServicioEventoImpl(servicioComunidadMock, repositorioEventoMock);
    }

    @Test
    public void testPublicarEvento() {
       Comunidad comunidad = mock(Comunidad.class);

       when(servicioComunidadMock.obtenerComunidad(anyLong())).thenReturn(comunidad);

         Evento evento = new Evento();
         evento.setNombre("Evento de prueba");

         servicioEvento.publicarEvento(evento, 1L);

         verify(servicioComunidadMock).obtenerComunidad(1L);
         verify(repositorioEventoMock).agregarEvento(evento);


    }

    @Test
    public void testObtenerEventosDeLaBaseDeDatos() {

       Evento evento = new Evento();
         evento.setNombre("Evento de prueba");
        Evento evento2 = new Evento();
        evento2.setNombre("Otro evento de prueba");

        List<Evento> eventos = List.of(evento, evento2);


        when(repositorioEventoMock.obetenerEventosDeLaComunidad(anyLong())).thenReturn(eventos);

        List<Evento> eventoObtenidos = servicioEvento.obtenerEventosDeLaBaseDeDatos(1L);

        assertThat(eventoObtenidos.size(),equalTo( 2));
        assertThat(eventoObtenidos, containsInAnyOrder(evento, evento2));
        verify(repositorioEventoMock).obetenerEventosDeLaComunidad(1L);
    }

}
