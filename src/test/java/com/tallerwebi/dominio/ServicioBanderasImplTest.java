package com.tallerwebi.dominio;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServicioBanderasImplTest {

    private HttpClient mockHttpClient;
    private ObjectMapper mockObjectMapper;
    private ServicioBanderasImpl servicio;

    @BeforeEach
    void setUp() {
        mockHttpClient = mock(HttpClient.class);
        mockObjectMapper = mock(ObjectMapper.class);
        servicio = new ServicioBanderasImpl(mockHttpClient, mockObjectMapper);
    }

    @Test
    void deberiaDevolverLaInfoDeArgentina_CuandoLaConsultaPorArgentinaEsExitosa() throws Exception {
        HttpClient mockHttpClient = mock(HttpClient.class);
        ObjectMapper objectMapper = new ObjectMapper();
        ServicioBanderasImpl servicio = new ServicioBanderasImpl(mockHttpClient, objectMapper);

        String jsonDeRespuesta = "[\n" +
                "  {\n" +
                "    \"name\": {\n" +
                "      \"common\": \"Argentina\",\n" +
                "      \"official\": \"Argentine Republic\"\n" +
                "    },\n" +
                "    \"capital\": [\"Buenos Aires\"],\n" +
                "    \"population\": 45376763,\n" +
                "    \"flags\": {\n" +
                "      \"png\": \"https://flagcdn.com/w320/ar.png\"\n" +
                "    }\n" +
                "  }\n" +
                "]";

        @SuppressWarnings("unchecked")
        HttpResponse<String> mockResponse = mock(HttpResponse.class);

        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonDeRespuesta);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);

        Pais resultado = servicio.getInfoPais("ar");

        assertNotNull(resultado);
        assertEquals("Argentina", resultado.getNombre().getComun());
        assertEquals("Buenos Aires", resultado.getCapitales().get(0));
        assertEquals("https://flagcdn.com/w320/ar.png", resultado.getBanderas().getPng());
    }
}
