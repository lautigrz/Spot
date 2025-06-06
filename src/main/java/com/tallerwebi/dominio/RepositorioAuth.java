package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.UsuarioDto;

public interface RepositorioAuth {
    UsuarioDto guardar(Usuario usuario);

}
