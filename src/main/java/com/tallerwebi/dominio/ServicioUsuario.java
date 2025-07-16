package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.UsuarioDto;

import java.util.Set;

public interface ServicioUsuario {
    Usuario obtenerUsuarioPorId(Long idUsuario);
    void seguirUsuario(Long seguidorId, Long aSeguirId) throws Exception;
    void dejarDeSeguirUsuario(Long seguidorId, Long aDejarId) throws Exception;
    Set<UsuarioDto> obtenerSeguidores(Long usuarioId);
    Set<UsuarioDto> obtenerSeguidos(Long usuarioId);
    boolean yaSigo(Long seguidorId, Long seguidoId) throws Exception;
    UsuarioDto obtenerUsuarioDtoPorId(Long idUsuario);
    void actualizarFotoPerfil(Long idUsuario, String urlFotoPerfil);

    void actualizarFotoPortada(Long idUsuario, String urlFoto);
}
