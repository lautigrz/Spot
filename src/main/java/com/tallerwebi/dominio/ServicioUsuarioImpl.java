package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.UsuarioDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioUsuarioImpl implements ServicioUsuario {
    @Autowired
    private final RepositorioUsuario repositorioUsuario;

    public ServicioUsuarioImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public Usuario obtenerUsuarioPorId(Long idUsuario) {
    Usuario usuario = repositorioUsuario.buscarUsuarioPorId(idUsuario);
        if (usuario == null) {
            return null; // O lanzar una excepción si el usuario no se encuentra
        }

        return usuario;

    }
}
