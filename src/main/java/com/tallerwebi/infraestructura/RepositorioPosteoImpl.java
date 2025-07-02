package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Post;
import com.tallerwebi.dominio.RepositorioPosteo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class RepositorioPosteoImpl implements RepositorioPosteo {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioPosteoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void guardar(Post post) {
        sessionFactory.getCurrentSession().save(post);
    }

    @Override
    public List<Post> obtenerPostsDeArtistasFavoritos(List<Long> idsLocales) {
        String hql = "FROM Post p WHERE p.artista.id IN :ids ORDER BY p.fecha DESC";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Post.class)
                .setParameter("ids", idsLocales)
                .list();
    }

    @Override
    public List<Post> obtenerPostsPorArtista(Long artistaId) {
        String hql = "FROM Post p WHERE p.artista.id = :id ORDER BY p.fecha DESC";
        Query<Post> query = sessionFactory.getCurrentSession().createQuery(hql, Post.class);
        query.setParameter("id", artistaId);
        return query.list();
    }
}
