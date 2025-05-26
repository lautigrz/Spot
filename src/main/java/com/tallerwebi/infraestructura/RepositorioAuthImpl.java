package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioAuth;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioAuthImpl implements RepositorioAuth {

    @Autowired
    private SessionFactory sessionFactory;

    public RepositorioAuthImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public void guardar(Usuario usuario) {
        this.sessionFactory.getCurrentSession().save(usuario);
    }

}
