package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioRecomendacion {

    void agregarRecomendacion(Recomendacion recomendacion);

    void eliminarRecomendacion(Long idRecomendacion);

    List<Recomendacion> obtenerRecomendacionesPorComunidad(Long idComunidad);

    Recomendacion aceptarRecomendacion(Long idRecomendacion);
}
