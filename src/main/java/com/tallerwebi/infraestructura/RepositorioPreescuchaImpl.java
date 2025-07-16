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
    public Long guardar(Preescucha preescucha) {
        sessionFactory.getCurrentSession().save(preescucha);
        return preescucha.getId();
    }

    @Override
    public boolean existeCompra(String albumId, Long usuarioId) {
     return false;
    }


    @Override
    public List<Preescucha> obtenerComprasPorUsuario(Long usuarioId){

        return List.of();
    }

    @Override
    public boolean existeCompraLocal(int preescuchaId, Long usuarioId){

        return false;
    }

    @Override
    public Preescucha buscarPreescuchaPorId(Long preescuchaId){
        String hql = "FROM Preescucha p WHERE p.id = :id";
        Query query = sessionFactory.getCurrentSession().createQuery(hql, Preescucha.class);
        query.setParameter("id", preescuchaId);
        return (Preescucha) query.uniqueResult();
    }

    @Override
    public List<Preescucha> obtenerPreescuchasLocalesCompradasPorUsuario(Long usuarioId) {

        return List.of();
    }

    @Override
    public List<Preescucha> obtenerPreescuchasPorArtista(Long idArtista) {
        String hql = "FROM Preescucha p WHERE p.artista.id = :idArtista";
        Query<Preescucha> query = sessionFactory.getCurrentSession().createQuery(hql, Preescucha.class);
        query.setParameter("idArtista", idArtista);
        return query.getResultList();

    }

}
