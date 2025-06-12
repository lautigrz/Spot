package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.EstadoDeAnimo;
import com.tallerwebi.dominio.RepositorioEstadoDeAnimo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class RepositorioEstadoDeAnimoImpl implements RepositorioEstadoDeAnimo {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioEstadoDeAnimoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<EstadoDeAnimo> obtenerTodosLosEstadosDeAnimo() {
        String hql = "FROM EstadoDeAnimo";
        TypedQuery<EstadoDeAnimo> query = sessionFactory.getCurrentSession().createQuery(hql, EstadoDeAnimo.class);
        return query.getResultList();
    }

    public EstadoDeAnimo obtenerEstadoDeAnimoPorId(Long id) {
        return (EstadoDeAnimo) sessionFactory.getCurrentSession().get(EstadoDeAnimo.class, id);
    }
}
