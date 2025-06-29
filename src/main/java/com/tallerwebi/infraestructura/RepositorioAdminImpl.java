package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioAdmin;
import com.tallerwebi.dominio.RepositorioUsuarioComunidad;
import com.tallerwebi.dominio.UsuarioComunidad;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioAdminImpl implements RepositorioAdmin {
    private SessionFactory sessionFactory;
    private RepositorioUsuarioComunidad repositorioUsuarioComunidad;
    public RepositorioAdminImpl(SessionFactory sessionFactory, RepositorioUsuarioComunidad repositorioUsuarioComunidad) {
        this.sessionFactory = sessionFactory;
        this.repositorioUsuarioComunidad = repositorioUsuarioComunidad;
    }


    @Override
    public void hacerAdminAUnMiembro(Long idComunidad, Long idMiembro) {

        UsuarioComunidad usuarioComunidad = repositorioUsuarioComunidad.obtenerUsuarioEnComunidad(idMiembro, idComunidad);

        if (usuarioComunidad != null) {
            usuarioComunidad.setRol("Admin");
            repositorioUsuarioComunidad.actualizar(usuarioComunidad);
        } else {
            throw new IllegalArgumentException("El miembro no pertenece a la comunidad");
        }

    }

    @Override
    public void eliminarMiembroDeComunidad(Long idComunidad, Long idMiembro) {
        UsuarioComunidad usuarioComunidad = repositorioUsuarioComunidad.obtenerUsuarioEnComunidad(idMiembro, idComunidad);
        if (usuarioComunidad != null) {
            sessionFactory.getCurrentSession().remove(usuarioComunidad);
        } else {
            throw new IllegalArgumentException("El miembro no pertenece a la comunidad");
        }
    }

    @Override
    public void eliminarMensajeDeComundad(Long idComunidad, Long idMensaje) {

    }
}
