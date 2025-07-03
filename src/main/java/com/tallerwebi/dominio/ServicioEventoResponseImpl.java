package com.tallerwebi.dominio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallerwebi.presentacion.dto.EventoDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class ServicioEventoResponseImpl implements ServicioEventoResponse {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ServicioEventoResponseImpl(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }


    @Override
    public List<EventoResponse.Evento> eventosobtenerEventosDeLaApi(String artista) {
        if (artista == null) {
            return Collections.emptyList();
        }
        try {
            String apikey = "aiBgBScq3Ao1yYqDOQJ6rGx0iKUJzmBL";
            String artistaCodificado = URLEncoder.encode(artista, StandardCharsets.UTF_8);
            String url = "https://app.ticketmaster.com/discovery/v2/events.json?keyword="
                    + artistaCodificado + "&apikey=" + apikey;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            EventoResponse eventoResponse = objectMapper.readValue(response.body(), EventoResponse.class);

            if (eventoResponse.get_embedded() != null) {
                System.out.println("Eventos obtenidos de la API: " + eventoResponse.get_embedded().getEvents().size());
                return eventoResponse.get_embedded().getEvents();
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }




}
