package com.tallerwebi.dominio;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ServicioAudio {
    void guardar(List<MultipartFile> audios, String url);
    Long duracionDeAudio(String ruta) throws Exception;
}
