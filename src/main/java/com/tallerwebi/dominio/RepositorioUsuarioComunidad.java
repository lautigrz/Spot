package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.UsuarioDto;

import java.util.List;

public interface RepositorioUsuarioComunidad {
    Boolean agregarUsuarioAComunidad(Usuario usuario, Comunidad comunidad, String rol);

    String obtenerRolDelUsuarioEnComunidad(Long idUsuario, Long idComunidad);
    UsuarioComunidad obtenerUsuarioEnComunidad(Long idUsuario, Long idComunidad);
    List<UsuarioDto> obtenerUsuariosDeLaComunidad(Long idComunidad);
    void actualizar(UsuarioComunidad usuarioComunidad);
    Boolean eliminarUsuarioDeComunidad(Long idUsuario, Long idComunidad);

    UsuarioComunidad obtenerUsuarioPorNombreEnComunidad(String usuario, Long idComunidad);
}
