package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Comentario;
import com.tallerwebi.dominio.RepositorioComentario;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioComentarioImpl implements RepositorioComentario {

    private final SessionFactory sessionFactory;


    public RepositorioComentarioImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void guardarComentario(Comentario comentario) {
        sessionFactory.getCurrentSession().save(comentario);
    }

    @Override
    public List<Comentario> obtenerComentariosDePosteo(Long idPosteo) {
        String hql = "FROM Comentario c WHERE c.post.id = :posteoId ORDER BY c.fecha ASC";

        return sessionFactory.getCurrentSession()
                .createQuery(hql, Comentario.class)
                .setParameter("posteoId",idPosteo)
                .getResultList();
    }
}
