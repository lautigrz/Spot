package com.tallerwebi.dominio;

public interface ServicioUsuarioComunidad {
    Long nuevaComunidad(Comunidad comunidad, Usuario usuario, String rol);
    String obtenerRolDelUsuarioEnComunidad(Long idUsuario, Long idComunidad);
    Boolean agregarUsuarioAComunidad(Usuario usuario, Comunidad comunidad, String rol);
    UsuarioComunidad obtenerUsuarioEnComunidad(Long idUsuario, Long id);
}
