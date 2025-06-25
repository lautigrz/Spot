package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.UsuarioDto;

public interface ServicioUsuario {
    UsuarioDto obtenerUsuarioDtoPorId(Long idUsuario);
    Usuario obtenerUsuarioPorId(Long idUsuario);
}
