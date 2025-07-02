package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Mensaje;
import com.tallerwebi.dominio.RepositorioMensaje;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioMensajeImpl implements RepositorioMensaje {

    @Autowired
    private SessionFactory sessionFactory;

    public RepositorioMensajeImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Mensaje obtenerMensaje(Long id) {
        return sessionFactory.getCurrentSession().get(Mensaje.class, id);
    }

    @Override
    public void eliminarMensaje(Long idMensaje) {
        Mensaje mensaje = obtenerMensaje(idMensaje);
        mensaje.setEstadoMensaje(false);
        sessionFactory.getCurrentSession().update(mensaje);
    }
}
