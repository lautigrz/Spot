package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioAuth;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.presentacion.dto.UsuarioDto;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioAuthImpl implements RepositorioAuth {

    @Autowired
    private SessionFactory sessionFactory;

    public RepositorioAuthImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public UsuarioDto guardar(Usuario usuario) {

        this.sessionFactory.getCurrentSession().save(usuario);
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setId(usuario.getId());
        return usuarioDto;

    }

}
