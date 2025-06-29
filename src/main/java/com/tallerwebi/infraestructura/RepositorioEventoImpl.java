package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Evento;
import com.tallerwebi.dominio.RepositorioEvento;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioEventoImpl implements RepositorioEvento {

    private SessionFactory sessionFactory;

    public RepositorioEventoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Evento> obetenerEventosDeLaComunidad(Long id) {
        String hql = "FROM Evento e WHERE e.comunidad.id = :idComunidad";

        return sessionFactory.getCurrentSession()
                .createQuery(hql, Evento.class)
                .setParameter("idComunidad", id)
                .getResultList();

    }

    @Override
    public void agregarEvento(Evento evento) {
        sessionFactory.getCurrentSession().save(evento);
    }
}
