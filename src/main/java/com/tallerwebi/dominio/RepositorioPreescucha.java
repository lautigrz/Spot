package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioPreescucha {
    boolean existeCompra(String albumId, Long usuarioId);
    Long guardar(Preescucha preescucha);
    List<Preescucha> obtenerComprasPorUsuario(Long usuarioId);
    boolean existeCompraLocal(int preescuchaId, Long id);
    Preescucha buscarPreescuchaPorId(Long preescuchaId);
    List<Preescucha> obtenerPreescuchasLocalesCompradasPorUsuario(Long usuarioId);
    List<Preescucha> obtenerPreescuchasPorArtista(Long idArtista);
}
