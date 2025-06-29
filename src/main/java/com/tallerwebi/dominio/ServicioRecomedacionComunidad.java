package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.CancionDto;
import com.tallerwebi.presentacion.dto.RecomendacionDto;

import java.util.List;

public interface ServicioRecomedacionComunidad {
    void agregarRecomendacion(List<CancionDto> cancionDtos, Long idUsuario, Long idComunidad);

    void eliminarRecomendacion(Long idRecomendacion);

    List<Recomendacion> obtenerRecomendacionesPorComunidad(Long idComunidad);

    Recomendacion aceptarRecomendacion(Long idRecomendacion);
}
