package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Notificacion;
import com.tallerwebi.dominio.Recomendacion;
import com.tallerwebi.dominio.RepositorioNotificacion;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioNotificacionImpl implements RepositorioNotificacion {

    private SessionFactory sessionFactory;
    public RepositorioNotificacionImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Notificacion> obtenerNotificacionesPorUsuario(Long idUsuario) {

        String hql = "SELECT n FROM Notificacion n JOIN FETCH n.usuario u WHERE u.id = :idUsuario";


        Query<Notificacion> query = sessionFactory.getCurrentSession().createQuery(hql, Notificacion.class);
        query.setParameter("idUsuario", idUsuario);

        return query.getResultList();
    }

    @Override
    public void guardarNotificacion(Notificacion notificacion, Usuario usuario, Recomendacion recomendacion) {
        notificacion.setUsuario(usuario);
        notificacion.setRecomendacion(recomendacion);
        sessionFactory.getCurrentSession().save(notificacion);
    }

    @Override
    public void cambiarEstadoNotificacion(Long idNotificacion) {

    }
}
