package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Cancion;
import com.tallerwebi.dominio.Rating;
import com.tallerwebi.dominio.RepositorioRating;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class RepositorioRatingImpl implements RepositorioRating {

    public SessionFactory sessionFactory;

    @Autowired
    public RepositorioRatingImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardarRating(Rating rating){
        sessionFactory.getCurrentSession().saveOrUpdate(rating);
    }


    @Override
    public List<Rating> obtenerRating(Usuario usuario){
        return sessionFactory.getCurrentSession()
                .createCriteria(Rating.class)
                .add(Restrictions.eq("usuario", usuario))
                .list();
    }

    @Override
    public Rating buscarPorUsuarioYCancion(Usuario usuario, Cancion cancion) {
        String hql = "FROM Rating r WHERE r.usuario = :usuario AND r.cancion = :cancion";
        Query<Rating> query = sessionFactory.getCurrentSession().createQuery(hql, Rating.class);
        query.setParameter("usuario", usuario);
        query.setParameter("cancion", cancion);
        return query.uniqueResult();
    }

}
