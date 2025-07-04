package com.tallerwebi.dominio;

public interface ServicioUsuarioComunidad {

    String obtenerRolDelUsuarioEnComunidad(Long idUsuario, Long idComunidad);
    Boolean agregarUsuarioAComunidad(Usuario usuario, Comunidad comunidad, String rol);
    UsuarioComunidad obtenerUsuarioEnComunidad(Long idUsuario, Long id);
    Boolean eliminarUsuarioDeComunidad(Long idUsuario, Long idComunidad);
}
