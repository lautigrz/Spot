package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioUsuarioComunidadImpl implements ServicioUsuarioComunidad {
    @Autowired
    private RepositorioUsuarioComunidad repositorioUsuarioComunidad;

    public ServicioUsuarioComunidadImpl(RepositorioUsuarioComunidad repositorioUsuarioComunidad) {
        this.repositorioUsuarioComunidad = repositorioUsuarioComunidad;
    }

    @Override
    public Long nuevaComunidad(Comunidad comunidad, Usuario usuario, String rol) {
        return repositorioUsuarioComunidad.nuevaComunidad(comunidad, usuario, rol);
    }

    @Override
    public String obtenerRolDelUsuarioEnComunidad(Long idUsuario, Long idComunidad) {
        return repositorioUsuarioComunidad.obtenerRolDelUsuarioEnComunidad(idUsuario, idComunidad);
    }

    @Override
    public Boolean agregarUsuarioAComunidad(Usuario usuario, Comunidad comunidad, String rol) {
        return repositorioUsuarioComunidad.agregarUsuarioAComunidad(usuario, comunidad, rol);
    }

    @Override
    public UsuarioComunidad obtenerUsuarioEnComunidad(Long idUsuario, Long id) {
        return repositorioUsuarioComunidad.obtenerUsuarioEnComunidad(idUsuario, id);
    }
}
