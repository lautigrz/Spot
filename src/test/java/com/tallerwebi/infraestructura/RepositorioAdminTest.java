package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
@Transactional
public class RepositorioAdminTest {
    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioAdmin repositorioAdmin;
    private RepositorioUsuarioComunidad repositorioUsuarioComunidad;

    @BeforeEach
    public void setUp() {
        repositorioUsuarioComunidad = new RepositorioUsuarioComunidadImpl(sessionFactory);
        repositorioAdmin = new RepositorioAdminImpl(sessionFactory, repositorioUsuarioComunidad);
    }


    @Test
    @Rollback
    public void testHacerAdminAUnMiembro() {
        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Comunidad de Prueba");
        comunidad.setDescripcion("Descripción de la comunidad de prueba");
        sessionFactory.getCurrentSession().save(comunidad);

        Usuario usuario = new Usuario();
        usuario.setUser("prueba");
        sessionFactory.getCurrentSession().save(usuario);

        UsuarioComunidad usuarioComunidad = new UsuarioComunidad();
        usuarioComunidad.setUsuario(usuario);
        usuarioComunidad.setComunidad(comunidad);
        usuarioComunidad.setRol("Miembro");
        sessionFactory.getCurrentSession().save(usuarioComunidad);

        repositorioAdmin.hacerAdminAUnMiembro(comunidad.getId(), usuario.getId());

        UsuarioComunidad usuarioActualizado = repositorioUsuarioComunidad.obtenerUsuarioEnComunidad(usuario.getId(), comunidad.getId());
        assertThat(usuarioActualizado.getRol(), equalTo("Admin"));

    }

    @Test
    @Rollback
    public void testEliminarMiembroDeComunidad() {
        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Comunidad de Prueba");
        comunidad.setDescripcion("Descripción de la comunidad de prueba");
        sessionFactory.getCurrentSession().save(comunidad);

        Usuario usuario = new Usuario();
        usuario.setUser("prueba");
        sessionFactory.getCurrentSession().save(usuario);

        UsuarioComunidad usuarioComunidad = new UsuarioComunidad();
        usuarioComunidad.setUsuario(usuario);
        usuarioComunidad.setComunidad(comunidad);
        usuarioComunidad.setRol("Miembro");
        sessionFactory.getCurrentSession().save(usuarioComunidad);

        repositorioAdmin.eliminarMiembroDeComunidad(comunidad.getId(), usuario.getId());

        UsuarioComunidad usuarioEliminado = repositorioUsuarioComunidad.obtenerUsuarioEnComunidad(usuario.getId(), comunidad.getId());
        assertThat(usuarioEliminado, equalTo(null));
    }

}
