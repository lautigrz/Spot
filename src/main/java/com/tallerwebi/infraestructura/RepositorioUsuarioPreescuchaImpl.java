package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Repository
public class RepositorioUsuarioPreescuchaImpl implements RepositorioUsuarioPreescucha {

    @Autowired
    private SessionFactory sessionFactory;

    public RepositorioUsuarioPreescuchaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public UsuarioPreescucha guardar(Usuario usuario, Preescucha preescucha) {
        UsuarioPreescucha usuarioPreescucha = new UsuarioPreescucha();

        usuarioPreescucha.setUsuario(usuario);
        usuarioPreescucha.setPreescucha(preescucha);
        usuarioPreescucha.setFechaCompra(LocalDateTime.now());
        usuario.getPreescuchasCompradas().add(usuarioPreescucha);
        preescucha.getUsuariosQueCompraron().add(usuarioPreescucha);

        sessionFactory.getCurrentSession().save(usuarioPreescucha);
        return usuarioPreescucha;
    }

    @Override
    public Boolean existePorUsuarioYPreescucha(Long usuarioId, Long preescuchaId) {

        String hql = "SELECT COUNT(up) FROM UsuarioPreescucha up WHERE up.usuario.id = :usuarioId AND up.preescucha.id = :preescuchaId";
        Query<Long> query = sessionFactory.getCurrentSession().createQuery(hql, Long.class);
        query.setParameter("usuarioId", usuarioId);
        query.setParameter("preescuchaId", preescuchaId);
        Long count = query.uniqueResult();
        return count != null && count > 0;
    }

    @Override
    public List<UsuarioPreescucha> buscarPorUsuario(Long id) {
        String hql = "FROM UsuarioPreescucha up WHERE up.usuario.id = :id";

        Query<UsuarioPreescucha> query = sessionFactory.getCurrentSession().createQuery(hql, UsuarioPreescucha.class);

        query.setParameter("id", id);
        List<UsuarioPreescucha> resultados = query.list();

        return resultados != null ? resultados : Collections.emptyList();
    }


    @Override
    public List<UsuarioPreescucha> buscarPorUsuarioOrdenado(Long idUsuario, String orden) {
        String hql = "FROM UsuarioPreescucha up WHERE up.usuario.id = :idUsuario ORDER BY up.fechaCompra " + orden;

        Query<UsuarioPreescucha> query = sessionFactory.getCurrentSession().createQuery(hql, UsuarioPreescucha.class);
        query.setParameter("idUsuario", idUsuario);

        return query.list();
    }

}
