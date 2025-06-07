package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioEstadoDeAnimo {

    List<EstadoDeAnimo> obtenerTodosLosEstadosDeAnimo();
    EstadoDeAnimo obtenerEstadoDeAnimoPorId(Long id);
}
