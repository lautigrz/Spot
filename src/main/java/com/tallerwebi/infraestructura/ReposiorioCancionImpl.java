package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Cancion;
import com.tallerwebi.dominio.RepositorioCancion;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReposiorioCancionImpl implements RepositorioCancion {

    private SessionFactory sessionFactory;

    @Autowired
    public ReposiorioCancionImpl(SessionFactory sessionFactory) {
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
        return List.of();
    }

    @Override
    public void eliminarCancion(Long id) {

    }
}
