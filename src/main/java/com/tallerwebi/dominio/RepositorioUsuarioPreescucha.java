package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioUsuarioPreescucha {
    UsuarioPreescucha guardar(Usuario usuario, Preescucha preescucha);

    Boolean existePorUsuarioYPreescucha(Long usuarioId, Long preescuchaId);

    List<UsuarioPreescucha> buscarPorUsuario(Long id);

    List<UsuarioPreescucha> buscarPorUsuarioOrdenado(Long idUsuario, String orden);
}
