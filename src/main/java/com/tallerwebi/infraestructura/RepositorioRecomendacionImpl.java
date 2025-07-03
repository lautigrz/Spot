package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Recomendacion;
import com.tallerwebi.dominio.RepositorioRecomendacion;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioRecomendacionImpl implements RepositorioRecomendacion {
    private SessionFactory sessionFactory;

    public RepositorioRecomendacionImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void agregarRecomendacion(Recomendacion recomendacion) {
        this.sessionFactory.getCurrentSession().save(recomendacion);
    }

    @Override
    public Long eliminarRecomendacion(Long idRecomendacion) {
        Recomendacion recomendacion = this.sessionFactory.getCurrentSession().get(Recomendacion.class, idRecomendacion);
        Long idUsuario = recomendacion.getUsuario().getId();

        recomendacion.setEstado(false);
        recomendacion.setLeida(true);
        sessionFactory.getCurrentSession().update(recomendacion);

        return idUsuario;
    }

    @Override
    public List<Recomendacion> obtenerRecomendacionesPorComunidad(Long idComunidad) {

        String hql = "FROM Recomendacion r WHERE r.comunidad.id = :idComunidad AND r.estado = false";
        return this.sessionFactory.getCurrentSession()
                .createQuery(hql, Recomendacion.class)
                .setParameter("idComunidad", idComunidad)
                .getResultList();
    }

    @Override
    public List<Recomendacion> obtenerRecomendacionesPorComunidadQueNoFueronLeidas(Long idComunidad) {
        String hql = "FROM Recomendacion r WHERE r.comunidad.id = :idComunidad AND r.leida = false";
        return this.sessionFactory.getCurrentSession()
                .createQuery(hql, Recomendacion.class)
                .setParameter("idComunidad", idComunidad)
                .getResultList();
    }

    @Override
    public Recomendacion obtenerRecomendacionPorId(Long idRecomendacion) {
        return sessionFactory.getCurrentSession().get(Recomendacion.class, idRecomendacion);
    }

    @Override
    public Recomendacion aceptarRecomendacion(Long idRecomendacion) {

        Recomendacion recomendacion = sessionFactory.getCurrentSession().get(Recomendacion.class, idRecomendacion);

        if (recomendacion != null) {
            recomendacion.setEstado(true); // Actualizar estado
            recomendacion.setLeida(true);
           sessionFactory.getCurrentSession().update(recomendacion); // Guardar el cambio
        }

        return recomendacion;
    }

}
