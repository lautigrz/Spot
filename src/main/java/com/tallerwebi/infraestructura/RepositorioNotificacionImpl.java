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
    public void cambiarEstadoNotificacion(List<Long> idsNotificaciones) {

        if (idsNotificaciones != null && !idsNotificaciones.isEmpty()) {
            String hql = "UPDATE Notificacion n SET n.leido = true WHERE n.id IN (:ids)";
            sessionFactory.getCurrentSession()
                    .createQuery(hql)
                    .setParameterList("ids", idsNotificaciones)
                    .executeUpdate();
        }
    }

    @Override
    public Boolean elUsuarioTieneNotificaciones(Long idUsuario) {
        String hql = "SELECT COUNT(n) FROM Notificacion n WHERE n.usuario.id = :idUsuario AND n.leido = false";
        Query<Long> query = sessionFactory.getCurrentSession().createQuery(hql, Long.class);
        query.setParameter("idUsuario", idUsuario);
        Long count = query.uniqueResult();

        System.out.println("el usuario tiene: " + count);

        return count != null && count > 0;

    }
}
