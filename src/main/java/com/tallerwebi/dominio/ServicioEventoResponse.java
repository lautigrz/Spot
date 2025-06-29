package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.EventoDto;

import java.util.List;

public interface ServicioEventoResponse {
    List<EventoResponse.Evento> eventosobtenerEventosDeLaApi(String artista);

}
