package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.UsuarioDto;

public interface ServicioMensaje {

    UsuarioDto eliminarMensaje(Long idMensaje);
}
