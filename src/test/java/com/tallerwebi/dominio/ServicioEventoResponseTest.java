package com.tallerwebi.dominio;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioEventoResponseTest {
    private HttpClient httpClientMock;
    private ObjectMapper objectMapperMock;
    private ServicioEventoResponseImpl servicio;

    @BeforeEach
    void setUp() {
        httpClientMock = mock(HttpClient.class);
        objectMapperMock = mock(ObjectMapper.class);
        servicio = new ServicioEventoResponseImpl(httpClientMock, objectMapperMock);
    }

    @Test
   public void eventosobtenerEventosDeLaApi_retornaListaEventos() throws Exception {
        // Preparar mocks

        // Mock del HttpResponse<String> que devuelve un JSON simulado
        HttpResponse<String> httpResponseMock = mock(HttpResponse.class);
        when(httpResponseMock.body()).thenReturn("{ \"_embedded\": { \"events\": [ { /* datos evento */ } ] } }");

        // Cuando httpClient.send se llame, devolver este httpResponseMock
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponseMock);

        // Crear un EventoResponse y sus eventos para devolver
        EventoResponse eventoResponseMock = mock(EventoResponse.class);
        EventoResponse.Embedded embeddedMock = mock(EventoResponse.Embedded.class);
        EventoResponse.Evento eventoMock = mock(EventoResponse.Evento.class);
        List<EventoResponse.Evento> listaEventos = List.of(eventoMock);

        when(eventoResponseMock.get_embedded()).thenReturn(embeddedMock);
        when(embeddedMock.getEvents()).thenReturn(listaEventos);

        // Cuando objectMapper.readValue se llame con cualquier string y clase EventoResponse,
        // devolver nuestro eventoResponseMock preparado
        when(objectMapperMock.readValue(anyString(), eq(EventoResponse.class))).thenReturn(eventoResponseMock);


        List<EventoResponse.Evento> resultado = servicio.eventosobtenerEventosDeLaApi("Coldplay");

        // Verificar resultado esperado


        assertThat(resultado, notNullValue());
        assertThat(resultado.size(), equalTo(1));


    }

    @Test
    public void eventosobtenerEventosDeLaApi_retornaListaVacia_siEmbeddedEsNull() throws Exception {
        // Mock HttpResponse con body JSON
        HttpResponse<String> httpResponseMock = mock(HttpResponse.class);
        when(httpResponseMock.body()).thenReturn("{ }"); // JSON vacío para simplificar

        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponseMock);

        EventoResponse eventoResponseMock = mock(EventoResponse.class);

        when(eventoResponseMock.get_embedded()).thenReturn(null);

        when(objectMapperMock.readValue(anyString(), eq(EventoResponse.class))).thenReturn(eventoResponseMock);

        List<EventoResponse.Evento> resultado = servicio.eventosobtenerEventosDeLaApi("Coldplay");

        assertNotNull(resultado);

    }

    @Test
    public void eventosobtenerEventosDeLaApi_retornaListaVacia_siException() throws Exception {
        // Simular excepción en httpClient.send
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenThrow(new RuntimeException("Error HTTP"));

        List<EventoResponse.Evento> resultado = servicio.eventosobtenerEventosDeLaApi("Coldplay");

        assertNotNull(resultado);

    }

}
