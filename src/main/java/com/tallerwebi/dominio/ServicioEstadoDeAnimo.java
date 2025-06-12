package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioEstadoDeAnimo {

    List<EstadoDeAnimo> obtenerTodosLosEstadosDeAnimo();
    EstadoDeAnimo obtenerEstadoDeAnimoPorId(Long id);
}
