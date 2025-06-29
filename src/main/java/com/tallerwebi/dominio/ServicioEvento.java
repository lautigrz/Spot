package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.EventoDto;

import java.util.List;

public interface ServicioEvento {
    void publicarEvento(Evento evento, Long idComunidad);
    List<Evento> obtenerEventosDeLaBaseDeDatos(Long idComunidad);

}
