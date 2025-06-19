package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Comunidad;
import com.tallerwebi.dominio.RepositorioNuevaComunidad;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioNuevaComunidadImpl implements RepositorioNuevaComunidad {
    @Autowired
    private SessionFactory sessionFactory;
    public RepositorioNuevaComunidadImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Comunidad nuevaComunidad(Comunidad comunidad) {
        this.sessionFactory.getCurrentSession().save(comunidad);
        return comunidad;
    }
}
