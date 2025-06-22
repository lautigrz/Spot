package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.UsuarioDto;

public interface ServicioUsuario {
    UsuarioDto obtenerUsuarioPorId(Long idUsuario);
}
