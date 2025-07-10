package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Preescucha;
import com.tallerwebi.dominio.RepositorioPreescucha;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class RepositorioPreescuchaImpl  implements RepositorioPreescucha {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioPreescuchaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public void guardar(Preescucha preescucha) {
        sessionFactory.getCurrentSession().save(preescucha);
    }

    @Override
    public boolean existeCompra(String albumId, Long usuarioId) {
        String hql = "SELECT COUNT(p) FROM Preescucha p WHERE p.spotifyAlbumId = :albumId AND p.usuario.id = :usuarioId";
        Query<Long> query = sessionFactory.getCurrentSession().createQuery(hql, Long.class);
        query.setParameter("albumId", albumId);
        query.setParameter("usuarioId", usuarioId);
        return query.uniqueResult() > 0;
    }


    @Override
    public List<Preescucha> obtenerComprasPorUsuario(Long usuarioId){
        String hql = "FROM Preescucha p WHERE p.usuario.id = :usuarioId";
        Query<Preescucha> query = sessionFactory.getCurrentSession().createQuery(hql, Preescucha.class);
        query.setParameter("usuarioId", usuarioId);
        return query.list();
    }

    @Override
    public boolean existeCompraLocal(int preescuchaId, Long usuarioId){
        String hql = "SELECT COUNT(p) FROM Preescucha p WHERE p.id = :preescuchaId AND p.usuario.id = :usuarioId";
        Query<Long> query = sessionFactory.getCurrentSession().createQuery(hql, Long.class);
        query.setParameter("preescuchaId", preescuchaId);
        query.setParameter("usuarioId", usuarioId);
        return query.uniqueResult() > 0;
    }

    @Override
    public Preescucha buscarPreescuchaPorId(int preescuchaId){
        String hql = "FROM Preescucha p WHERE p.id = :id";
        Query query = sessionFactory.getCurrentSession().createQuery(hql, Preescucha.class);
        query.setParameter("id", preescuchaId);
        return (Preescucha) query.uniqueResult();
    }

    @Override
    public List<Preescucha> obtenerPreescuchasLocalesCompradasPorUsuario(Long usuarioId) {
        String hql = "FROM Preescucha p WHERE p.usuario.id = :usuarioId AND p.fechaCompra IS NOT NULL";
        Query<Preescucha> query = sessionFactory.getCurrentSession().createQuery(hql, Preescucha.class);
        query.setParameter("usuarioId", usuarioId);
        return query.list();
    }

}
