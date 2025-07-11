package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.UsuarioDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioMensajeImpl implements ServicioMensaje {

    private RepositorioMensaje repositorioMensaje;

    public ServicioMensajeImpl(RepositorioMensaje repositorioMensaje) {
       this.repositorioMensaje=repositorioMensaje;
    }

    @Override
    public UsuarioDto eliminarMensaje(Long idMensaje) {
        return this.repositorioMensaje.eliminarMensaje(idMensaje);
    }
}
