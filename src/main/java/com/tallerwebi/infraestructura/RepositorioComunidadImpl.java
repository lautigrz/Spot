package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Mensaje;
import com.tallerwebi.dominio.RepositorioComunidad;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioComunidadImpl implements RepositorioComunidad {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioComunidadImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardarMensajeDeLaComunidad(String contenido, Long idUsuario) {
        // Obtener usuario desde la BD
        Usuario usuario = sessionFactory.getCurrentSession().get(Usuario.class, idUsuario);

        Mensaje mensaje = new Mensaje();
        mensaje.setTexto(contenido);
        mensaje.setUsuario(usuario);

        sessionFactory.getCurrentSession().save(mensaje);
    }

    @Override
    public List<Mensaje> obtenerMensajesDeComunidad() {
        String hql = "SELECT m FROM Mensaje m JOIN FETCH m.usuario";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Mensaje.class)
                .getResultList();
    }
}
