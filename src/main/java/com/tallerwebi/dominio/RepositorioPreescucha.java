package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioPreescucha {
    boolean existeCompra(String albumId, Long usuarioId);
    void guardar(Preescucha preescucha);
    List<Preescucha> obtenerComprasPorUsuario(Long usuarioId);
    boolean existeCompraLocal(int preescuchaId, Long id);
    Preescucha buscarPreescuchaPorId(int preescuchaId);
}
