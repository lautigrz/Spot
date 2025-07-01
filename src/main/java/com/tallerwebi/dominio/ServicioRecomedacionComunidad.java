package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.CancionDto;


import java.util.List;

public interface ServicioRecomedacionComunidad {
    void agregarRecomendacion(List<CancionDto> cancionDtos, Long idUsuario, Long idComunidad);

    Long eliminarRecomendacion(Long idRecomendacion);

    List<Recomendacion> obtenerRecomendacionesPorComunidad(Long idComunidad);
    List<Recomendacion> obtenerRecomendacionesPorComunidadQueNoFueronLeidas(Long idComunidad);
    Recomendacion aceptarRecomendacion(Long idRecomendacion);

    Recomendacion obtenerRecomendacionPorId(Long idRecomendacion);
}
