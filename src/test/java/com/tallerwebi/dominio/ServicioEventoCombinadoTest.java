package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.EventoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioEventoCombinadoTest {

    private ServicioEventoResponse servicioEventoResponseMock;
    private RepositorioEvento repositorioEventoMock;
    private ServicioEventoCombinadoImpl servicio;

    @BeforeEach
    void setUp() {
        servicioEventoResponseMock = mock(ServicioEventoResponse.class);
        repositorioEventoMock = mock(RepositorioEvento.class);
        servicio = new ServicioEventoCombinadoImpl(servicioEventoResponseMock, repositorioEventoMock);
    }

    @Test
    public void obtenerEventos_combinaEventosLocalesYApi_yMapeaCorrectamente() {
        // Evento local de BD
        Evento eventoLocal = new Evento();
        eventoLocal.setNombre("Evento Local");
        eventoLocal.setFecha(LocalDate.of(2025, 6, 27));
        eventoLocal.setLugar("Lugar Local");
        eventoLocal.setCiudad("Ciudad Local");
        eventoLocal.setPais("Pais Local");
        eventoLocal.setImagen("imgLocal.jpg");
        eventoLocal.setUrl("http://local.com/evento");

        List<Evento> eventosLocales = List.of(eventoLocal);
        when(repositorioEventoMock.obetenerEventosDeLaComunidad(anyLong())).thenReturn(eventosLocales);


        EventoResponse.Evento eventoApi = mock(EventoResponse.Evento.class);


        EventoResponse.Fecha fechaMock = mock(EventoResponse.Fecha.class);
        EventoResponse.Start startMock = mock(EventoResponse.Start.class);

        when(eventoApi.getName()).thenReturn("Evento API");
        when(eventoApi.getDates()).thenReturn(fechaMock);
        when(fechaMock.getStart()).thenReturn(startMock);
        when(startMock.getLocalDate()).thenReturn("2025-06-28");
        when(eventoApi.getUrl()).thenReturn("http://api.com/evento");


        EventoResponse.EmbeddedVenue embeddedMock = mock(EventoResponse.EmbeddedVenue.class);
        when(eventoApi.get_embedded()).thenReturn(embeddedMock);

        EventoResponse.Venue venueMock = mock(EventoResponse.Venue.class);
        EventoResponse.City cityMock = mock(EventoResponse.City.class);
        EventoResponse.Country countryMock = mock(EventoResponse.Country.class);

        when(embeddedMock.getVenues()).thenReturn(List.of(venueMock));
        when(venueMock.getName()).thenReturn("Venue API");
        when(venueMock.getCity()).thenReturn(cityMock);
        when(cityMock.getName()).thenReturn("City API");
        when(venueMock.getCountry()).thenReturn(countryMock);
        when(countryMock.getName()).thenReturn("Country API");


        EventoResponse.Imagen imageMock = mock(EventoResponse.Imagen.class);
        when(imageMock.getUrl()).thenReturn("http://image.api/event.jpg");
        when(eventoApi.getImages()).thenReturn(List.of(imageMock));


        List<EventoResponse.Evento> eventosApi = List.of(eventoApi);
        when(servicioEventoResponseMock.eventosobtenerEventosDeLaApi(anyString())).thenReturn(eventosApi);



        List<EventoDto> resultado = servicio.obtenerEventos("Artista", 1L);


        assertThat(resultado.size(), equalTo(2));

        EventoDto dtoLocal = resultado.get(0);
        assertThat(dtoLocal.getNombre(), equalTo("Evento Local"));
        assertThat(dtoLocal.getFecha(), equalTo("2025-06-27"));
        assertThat(dtoLocal.getLugar(), equalTo("Lugar Local"));
        assertThat(dtoLocal.getCiudad(), equalTo("Ciudad Local"));
        assertThat(dtoLocal.getPais(), equalTo("Pais Local"));
        assertThat(dtoLocal.getImagen(), equalTo("imgLocal.jpg"));
        assertThat(dtoLocal.getUrl(), equalTo("http://local.com/evento"));

// Evento API
        EventoDto dtoApi = resultado.get(1);
        assertThat(dtoApi.getNombre(), equalTo("Evento API"));
        assertThat(dtoApi.getFecha(), equalTo("2025-06-28"));
        assertThat(dtoApi.getLugar(), equalTo("Venue API"));
        assertThat(dtoApi.getCiudad(), equalTo("City API"));
        assertThat(dtoApi.getPais(), equalTo("Country API"));
        assertThat(dtoApi.getImagen(), equalTo("http://image.api/event.jpg"));
        assertThat(dtoApi.getUrl(), equalTo("http://api.com/evento"));

    }

    @Test
    public void obtenerEventos_eventoApiSinEmbedded_yColocaDefaults() {
        when(repositorioEventoMock.obetenerEventosDeLaComunidad(anyLong())).thenReturn(new ArrayList<>());

        EventoResponse.Evento eventoApi = mock(EventoResponse.Evento.class);
        when(eventoApi.getName()).thenReturn("Evento API");

        EventoResponse.Fecha fechaMock = mock(EventoResponse.Fecha.class);
        EventoResponse.Start startMock = mock(EventoResponse.Start.class);
        when(eventoApi.getDates()).thenReturn(fechaMock);
        when(fechaMock.getStart()).thenReturn(startMock);
        when(startMock.getLocalDate()).thenReturn("2025-06-28");

        when(eventoApi.getUrl()).thenReturn("http://api.com/evento");

        when(eventoApi.get_embedded()).thenReturn(null); // Sin embedded
        when(eventoApi.getImages()).thenReturn(new ArrayList<>());

        when(servicioEventoResponseMock.eventosobtenerEventosDeLaApi(anyString())).thenReturn(List.of(eventoApi));


        List<EventoDto> resultado = servicio.obtenerEventos("Artista", 1L);

        assertThat(resultado.size(), equalTo(1));

        EventoDto dto = resultado.get(0);
        assertThat(dto.getLugar(), equalTo("Lugar desconocido"));
        assertThat(dto.getCiudad(), equalTo("Ciudad desconocida"));
        assertThat(dto.getPais(), equalTo("País desconocido"));
        assertThat(dto.getImagen(), equalTo("https://via.placeholder.com/400x200?text=Sin+imagen"));
    }

}
