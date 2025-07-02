package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioRecomendacion {

    void agregarRecomendacion(Recomendacion recomendacion);

    Long eliminarRecomendacion(Long idRecomendacion);

    List<Recomendacion> obtenerRecomendacionesPorComunidad(Long idComunidad);
    List<Recomendacion> obtenerRecomendacionesPorComunidadQueNoFueronLeidas(Long idComunidad);
    Recomendacion obtenerRecomendacionPorId(Long idRecomendacion);
    Recomendacion aceptarRecomendacion(Long idRecomendacion);
}
