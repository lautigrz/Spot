package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioEvento {
    List<Evento> obetenerEventosDeLaComunidad(Long id);
    void agregarEvento(Evento evento);
}
