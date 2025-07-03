package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Artista;
import com.tallerwebi.dominio.Preescucha;
import com.tallerwebi.dominio.RepositorioArtista;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class RepositorioArtistaImpl implements RepositorioArtista {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioArtistaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Artista> buscarPorEmail(String email) {
        String hql = "FROM Artista a WHERE a.email = :email";
        Query<Artista> query = sessionFactory.getCurrentSession().createQuery(hql, Artista.class);
        query.setParameter("email", email);
        return query.uniqueResultOptional();
    }

    @Override
    public void guardar(Artista artista) {
        sessionFactory.getCurrentSession().save(artista);
    }

    @Override
    public Artista buscarPorNombre(String nombre) {
        String hql = "FROM Artista a WHERE nombre=:nombre";
        Query<Artista> query = sessionFactory.getCurrentSession().createQuery(hql, Artista.class);
        query.setParameter("nombre", nombre);
        return query.uniqueResult();
    }

    @Override
    public Artista buscarPorId(Long id) {
        Artista artista = sessionFactory.getCurrentSession().get(Artista.class, id);
        Hibernate.initialize(artista.getPreescuchas());
        return artista;
    }

    @Override
    public List<Preescucha> obtenerPreescuchasDeArtista(Long artistaId){
        String hql = "FROM Preescucha p WHERE P.artista.id = :artistaId";
        Query <Preescucha> query = sessionFactory.getCurrentSession().createQuery(hql, Preescucha.class);
        query.setParameter("artistaId", artistaId);
        return query.list();
    }
}
