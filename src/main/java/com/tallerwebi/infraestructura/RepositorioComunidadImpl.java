package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Comunidad;
import com.tallerwebi.dominio.Mensaje;
import com.tallerwebi.dominio.RepositorioComunidad;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class RepositorioComunidadImpl implements RepositorioComunidad {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioComunidadImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardarMensajeDeLaComunidad(String contenido, Long idUsuario,Long idComunidad) {

        Usuario usuario = sessionFactory.getCurrentSession().get(Usuario.class, idUsuario);
        Comunidad comunidad = sessionFactory.getCurrentSession().get(Comunidad.class, idComunidad);

        Mensaje mensaje = new Mensaje();
        mensaje.setTexto(contenido);
        mensaje.setUsuario(usuario);

        comunidad.agregarMensaje(mensaje);

        sessionFactory.getCurrentSession().save(mensaje);
    }

    @Override
    public List<Mensaje> obtenerMensajesDeComunidad(Long id) {
        Comunidad comunidad = sessionFactory.getCurrentSession().get(Comunidad.class, id);
        comunidad.getMensajes().size();  // forzamos la carga
        return comunidad.getMensajes();
    }

    @Override
    public void guardarNuevaComunidad(Comunidad comunidad) {
        sessionFactory.getCurrentSession().save(comunidad);
    }

    @Override
    public List<Comunidad> obtenerComunidades() {
        String hql = "FROM Comunidad";
        TypedQuery<Comunidad> query = sessionFactory.getCurrentSession().createQuery(hql, Comunidad.class);
        return query.getResultList();
    }

    @Override
    public Comunidad obtenerComunidad(Long id) {
        return sessionFactory.getCurrentSession().get(Comunidad.class, id);
    }

    @Override
    public String obtenerTokenDelUsuario(String user) {
        String hql = "SELECT u.token FROM Usuario u WHERE u.user = :usuario";
        TypedQuery<String> query = sessionFactory.getCurrentSession().createQuery(hql, String.class);
        query.setParameter("usuario", user);

        return query.getSingleResult(); // o getResultList() si esperás múltiples resultados

    }
}
