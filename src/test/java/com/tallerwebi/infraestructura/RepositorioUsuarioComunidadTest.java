package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Comunidad;
import com.tallerwebi.dominio.RepositorioUsuarioComunidad;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.UsuarioComunidad;
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
public class RepositorioUsuarioComunidadTest {
    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioUsuarioComunidad repositorioUsuarioComunidad;

    @BeforeEach
    public void setUp() {
        repositorioUsuarioComunidad = new RepositorioUsuarioComunidadImpl(sessionFactory);
    }

    @Test
    @Rollback
    public void seDebeAgregarUsuarioAComunidad() {
       Usuario usuario = new Usuario();
       usuario.setUser("testUser");
       usuario.setUrlFoto("http://example.com/test.jpg");

       sessionFactory.getCurrentSession().save(usuario);
       Comunidad comunidad = new Comunidad();
         comunidad.setNombre("Test Comunidad");
         comunidad.setUrlFoto("http://example.com/comunidad.jpg");
         sessionFactory.getCurrentSession().save(comunidad);

         Boolean resultado = repositorioUsuarioComunidad.agregarUsuarioAComunidad(usuario, comunidad, "ADMIN");

        assertThat(resultado, equalTo(true));

    }

    @Test
    @Rollback
    public void seDebeObtenerRolDelUsuarioEnComunidad() {
        Usuario usuario = new Usuario();
        usuario.setUser("testUser");
        usuario.setUrlFoto("http://example.com/test.jpg");

        sessionFactory.getCurrentSession().save(usuario);
        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Test Comunidad");
        comunidad.setUrlFoto("http://example.com/comunidad.jpg");
        sessionFactory.getCurrentSession().save(comunidad);

        repositorioUsuarioComunidad.agregarUsuarioAComunidad(usuario, comunidad, "ADMIN");

        String rol = repositorioUsuarioComunidad.obtenerRolDelUsuarioEnComunidad(usuario.getId(), comunidad.getId());

        assertThat(rol, equalTo("ADMIN"));
    }

    @Test
    @Rollback
    public void seDebeObtenerUsuarioEnComunidad() {
        Usuario usuario = new Usuario();
        usuario.setUser("testUser");
        usuario.setUrlFoto("http://example.com/test.jpg");

        sessionFactory.getCurrentSession().save(usuario);
        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Test Comunidad");
        comunidad.setUrlFoto("http://example.com/comunidad.jpg");
        sessionFactory.getCurrentSession().save(comunidad);

        repositorioUsuarioComunidad.agregarUsuarioAComunidad(usuario, comunidad, "ADMIN");

        var usuarioComunidad = repositorioUsuarioComunidad.obtenerUsuarioEnComunidad(usuario.getId(), comunidad.getId());

        assertThat(usuarioComunidad.getRol(), equalTo("ADMIN"));
        assertThat(usuarioComunidad.getUsuario().getUser(), equalTo(usuario.getUser()));
        assertThat(usuarioComunidad.getUsuario().getId(), equalTo(usuario.getId()));
    }

    @Test
    @Rollback
    public void seDebeActualizarUsuarioEnComunidad() {
        Usuario usuario = new Usuario();
        usuario.setUser("testUser");
        usuario.setUrlFoto("http://example.com/test.jpg");

        sessionFactory.getCurrentSession().save(usuario);

        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Test Comunidad");
        comunidad.setUrlFoto("http://example.com/comunidad.jpg");
        sessionFactory.getCurrentSession().save(comunidad);


        repositorioUsuarioComunidad.agregarUsuarioAComunidad(usuario, comunidad, "Admin");

        UsuarioComunidad usuarioComunidadOb = repositorioUsuarioComunidad.obtenerUsuarioEnComunidad(usuario.getId(), comunidad.getId());
        usuarioComunidadOb.setRol("Miembro");

        repositorioUsuarioComunidad.actualizar(usuarioComunidadOb);

        UsuarioComunidad usuarioActualizado = repositorioUsuarioComunidad.obtenerUsuarioEnComunidad(usuario.getId(), comunidad.getId());

        assertThat(usuarioActualizado.getRol(), equalTo("Miembro"));
    }



}
