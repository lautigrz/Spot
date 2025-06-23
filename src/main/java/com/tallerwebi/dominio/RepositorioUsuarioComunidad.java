package com.tallerwebi.dominio;

public interface RepositorioUsuarioComunidad {
    Boolean agregarUsuarioAComunidad(Usuario usuario, Comunidad comunidad, String rol);
    Long nuevaComunidad(Comunidad comunidad, Usuario usuario, String rol);
    String obtenerRolDelUsuarioEnComunidad(Long idUsuario, Long idComunidad);
    UsuarioComunidad obtenerUsuarioEnComunidad(Long idUsuario, Long idComunidad);
}
