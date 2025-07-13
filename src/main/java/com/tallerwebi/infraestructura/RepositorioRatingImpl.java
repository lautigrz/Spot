package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Rating;
import com.tallerwebi.dominio.RepositorioRating;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
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
    sessionFactory.getCurrentSession().save(rating);
    }

    @Override
    public List<Rating> obtenerRating(Usuario usuario){
        return sessionFactory.getCurrentSession()
                .createCriteria(Rating.class)
                .add(Restrictions.eq("usuario", usuario))
                .list();
    }
}
