package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Comunidad;
import com.tallerwebi.dominio.RepositorioNuevaComunidad;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.UsuarioComunidad;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioNuevaComunidadImpl implements RepositorioNuevaComunidad {
    @Autowired
    private SessionFactory sessionFactory;
    public RepositorioNuevaComunidadImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
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
}
