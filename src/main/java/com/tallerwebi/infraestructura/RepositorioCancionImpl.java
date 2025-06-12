package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Cancion;
import com.tallerwebi.dominio.RepositorioCancion;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioCancionImpl implements RepositorioCancion {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioCancionImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardarCancion(Cancion cancion) {
        sessionFactory.getCurrentSession().save(cancion);
    }

    @Override
    public Cancion obtenerPorId(Long id) {
        return sessionFactory.getCurrentSession().get(Cancion.class, id);
    }

    @Override
    public List<Cancion> obtenerTodas() {
        Query query = sessionFactory.getCurrentSession().createQuery("from Cancion");
        return query.list();
    }

    @Override
    public void eliminarCancion(Long id) {
        sessionFactory.getCurrentSession().delete(obtenerPorId(id));
    }

    @Override
    public Cancion buscarCancionPorElIdDeSpotify(String sporifyId) {
        String hql = "FROM Cancion c WHERE c.spotifyId = :spotifyId";
        Query<Cancion> query = sessionFactory.getCurrentSession().createQuery(hql, Cancion.class);
        query.setParameter("spotifyId", sporifyId);

        return query.uniqueResult();
    }
}
