package com.tallerwebi.dominio;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Transactional
public class ServicioBanderasImpl implements ServicioBanderas {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ServicioBanderasImpl(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public String getBandera(String codigoPais) {
        String baseUrl = "https://flagsapi.com/" + codigoPais.toUpperCase() + "/flat/64.png";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .GET()
                .build();

        try {
            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            if (response.statusCode() == 200) {
                return baseUrl;
            } else {
                return null;
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Pais getInfoPais(String codigoPais) {
        String url = "https://restcountries.com/v3.1/alpha/" + codigoPais;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Pais[] result = objectMapper.readValue(response.body(), Pais[].class);
                return result.length > 0 ? result[0] : null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}