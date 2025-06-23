package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Comunidad;
import com.tallerwebi.dominio.RepositorioUsuarioComunidad;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.UsuarioComunidad;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioUsuarioComunidadImpl implements RepositorioUsuarioComunidad {
    @Autowired
    private SessionFactory sessionFactory;

    public RepositorioUsuarioComunidadImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Boolean agregarUsuarioAComunidad(Usuario usuario, Comunidad comunidad, String rol) {

        Usuario usuarioPersistente =  sessionFactory.getCurrentSession().get(Usuario.class, usuario.getId()); // Cargar usuario persistente
        Comunidad comunidadPersistente =  sessionFactory.getCurrentSession().get(Comunidad.class, comunidad.getId());
        UsuarioComunidad usuarioComunidad = new UsuarioComunidad();
        usuarioComunidad.setUsuario(usuario);
        usuarioComunidad.setComunidad(comunidad);
        usuarioComunidad.setRol(rol);

        usuarioPersistente.getComunidades().add(usuarioComunidad);
        comunidadPersistente.getUsuarios().add(usuarioComunidad);

        sessionFactory.getCurrentSession().save(usuarioComunidad);
        return true;
    }

    public Long nuevaComunidad(Comunidad comunidad, Usuario usuario, String rol) {


        sessionFactory.getCurrentSession().save(comunidad);

        Usuario usuarioPersistente =  sessionFactory.getCurrentSession().get(Usuario.class, usuario.getId()); // Cargar usuario persistente

        UsuarioComunidad usuarioComunidad = new UsuarioComunidad();
        usuarioComunidad.setUsuario(usuarioPersistente);
        usuarioComunidad.setComunidad(comunidad);
        usuarioComunidad.setRol(rol);

        comunidad.getUsuarios().add(usuarioComunidad);
        usuarioPersistente.getComunidades().add(usuarioComunidad);

        sessionFactory.getCurrentSession().save(usuarioComunidad);
        return comunidad.getId();
    }



    @Override
    public String obtenerRolDelUsuarioEnComunidad(Long idUsuario, Long idComunidad) {
        String hql = "FROM UsuarioComunidad uc WHERE uc.usuario.id = :idUsuario AND uc.comunidad.id = :idComunidad";
        Query <UsuarioComunidad> query = sessionFactory.getCurrentSession().createQuery(hql, UsuarioComunidad.class);
        query.setParameter("idUsuario", idUsuario);
        query.setParameter("idComunidad", idComunidad);
        return query.uniqueResult().getRol();
    }

    @Override
    public UsuarioComunidad obtenerUsuarioEnComunidad(Long idUsuario, Long idComunidad) {
        String hql = "FROM UsuarioComunidad uc WHERE uc.usuario.id = :idUsuario AND uc.comunidad.id = :idComunidad";
        Query<UsuarioComunidad> query = sessionFactory.getCurrentSession().createQuery(hql, UsuarioComunidad.class);
        query.setParameter("idUsuario", idUsuario);
        query.setParameter("idComunidad", idComunidad);
        return query.uniqueResult();
    }
}
