package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioUsuarioPreescucha {
    UsuarioPreescucha guardar(Usuario usuario, Preescucha preescucha);

    UsuarioPreescucha buscarPorId(Long id);
    Boolean existePorUsuarioYPreescucha(Long usuarioId, Long preescuchaId);
    void eliminar(Long id);

    List<UsuarioPreescucha> buscarPorUsuario(Long id);

    List<UsuarioPreescucha> buscarPorPreescucha(Long id);
}
