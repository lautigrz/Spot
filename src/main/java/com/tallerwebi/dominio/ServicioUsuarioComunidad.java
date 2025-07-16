package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioUsuarioComunidad {

    String obtenerRolDelUsuarioEnComunidad(Long idUsuario, Long idComunidad);
    Boolean agregarUsuarioAComunidad(Usuario usuario, Comunidad comunidad, String rol);
    UsuarioComunidad obtenerUsuarioEnComunidad(Long idUsuario, Long id);
    Boolean eliminarUsuarioDeComunidad(Long idUsuario, Long idComunidad);
    void compartirPosteoEnComunidad(Long idPost, List<Long> comunidades, Long idUsuario);
    Boolean agregarUsuarioAComunidadDePreescucha(Long id, Long idPreescucha, String rol);
    List<Comunidad> obtenerComunidadesDondeELUsuarioEsteUnido(Long idUsuario);
}
