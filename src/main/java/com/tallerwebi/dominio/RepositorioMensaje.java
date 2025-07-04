package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.UsuarioDto;

public interface RepositorioMensaje {

    UsuarioDto eliminarMensaje(Long idMensaje);
}
