package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioAdminImpl implements ServicioAdmin {

    private RepositorioAdmin repositorioAdmin;

    public ServicioAdminImpl(RepositorioAdmin repositorioAdmin) {
        this.repositorioAdmin = repositorioAdmin;
    }

    @Override
    public void hacerAdminAUnMiembro(Long idComunidad, Long idMiembro) {
        repositorioAdmin.hacerAdminAUnMiembro(idComunidad, idMiembro);
    }

    @Override
    public void eliminarMiembroDeComunidad(Long idComunidad, Long idMiembro) {
        repositorioAdmin.eliminarMiembroDeComunidad(idComunidad, idMiembro);
    }

    @Override
    public void eliminarMensajeDeComundad(Long idComunidad, Long idMensaje) {

    }
}
