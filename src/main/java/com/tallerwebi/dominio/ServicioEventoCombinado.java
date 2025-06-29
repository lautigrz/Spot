package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.EventoDto;

import java.util.List;

public interface ServicioEventoCombinado {
    List<EventoDto> obtenerEventos(String artista, Long idComunidad);
}
