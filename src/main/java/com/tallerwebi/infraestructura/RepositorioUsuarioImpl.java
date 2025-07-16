package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class RepositorioUsuarioImpl implements RepositorioUsuario {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioUsuarioImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Usuario buscar(String user) {
        return (Usuario) sessionFactory.getCurrentSession().createCriteria(Usuario.class)
                .add(Restrictions.eq("user", user))
                .uniqueResult();
    }

    @Override
    public Usuario buscarUsuarioPorId(Long id) {

        return sessionFactory.getCurrentSession().get(Usuario.class, id);
    }

    @Override
    public Usuario buscarUsuarioPorSpotifyID(String spotifyID) {
        return (Usuario) sessionFactory.getCurrentSession().createCriteria(Usuario.class)
                .add(Restrictions.eq("spotifyID", spotifyID))
                .uniqueResult();
    }

    @Override
    public void actualizarUsuario(Usuario usuario) {
        sessionFactory.getCurrentSession().update(usuario);
    }

    @Override
    public void actualizarFotoPerfil(Usuario usuario, String urlFotoPerfil) {
        usuario.setUrlFoto(urlFotoPerfil);
        sessionFactory.getCurrentSession().update(usuario);
    }

    @Override
    public void actualizarFotoPortada(Usuario usuario, String urlFotoPortada) {
        usuario.setUrlPortada(urlFotoPortada);
        sessionFactory.getCurrentSession().update(usuario);
    }
}
