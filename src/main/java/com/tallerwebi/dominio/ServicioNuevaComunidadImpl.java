package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.UsuarioDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioNuevaComunidadImpl implements ServicioNuevaComunidad {
    @Autowired
    private RepositorioNuevaComunidad repositorioNuevaComunidad;
    public ServicioNuevaComunidadImpl(RepositorioNuevaComunidad repositorioNuevaComunidad) {
        this.repositorioNuevaComunidad = repositorioNuevaComunidad;
    }

    @Override
    public Long nuevaComunidad(Comunidad comunidad, Usuario usuario, String rol) {
        return repositorioNuevaComunidad.nuevaComunidad(comunidad, usuario, rol);
    }
}
