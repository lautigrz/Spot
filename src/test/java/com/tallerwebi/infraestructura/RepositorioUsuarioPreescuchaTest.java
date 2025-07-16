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

import java.time.LocalDateTime;
import java.util.List;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
@Transactional
public class RepositorioUsuarioPreescuchaTest {

    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioUsuarioPreescucha repositorioUsuarioPreescucha;

    @BeforeEach
    public void setUp() {
        repositorioUsuarioPreescucha = new RepositorioUsuarioPreescuchaImpl(sessionFactory);
    }

    @Test
    @Rollback
    public void seDebeGuardarUsuarioPreescucha() {
      Usuario usuario = new Usuario();
      usuario.setUser("prueba");

      sessionFactory.getCurrentSession().save(usuario);

        Artista artista  = new Artista();
        artista.setEmail("prueba@gmail.com");
        artista.setNombre("pedrito");

        sessionFactory.getCurrentSession().save(artista);

        Preescucha preescucha = new Preescucha();
        preescucha.setArtista(artista);
        preescucha.setTitulo("Preescucha de prueba");
        preescucha.setPrecio(100.0);

        sessionFactory.getCurrentSession().save(preescucha);

        UsuarioPreescucha usuarioPreescucha = repositorioUsuarioPreescucha.guardar(usuario, preescucha);

        String hql = "FROM UsuarioPreescucha up WHERE up.id = :id";
        UsuarioPreescucha resultado = sessionFactory.getCurrentSession().createQuery(hql, UsuarioPreescucha.class)
                .setParameter("id", usuarioPreescucha.getId())
                .uniqueResult();
        assertThat(resultado, notNullValue());
        assertThat(resultado.getPreescucha(), equalTo(preescucha));
        assertThat(resultado.getUsuario(), equalTo(usuario));



    }

    @Test
    @Rollback
    public void verificarSiExistePorUsuarioYPreescucha() {
        Usuario usuario = new Usuario();
        usuario.setUser("usuarioPrueba");
        sessionFactory.getCurrentSession().save(usuario);

        Preescucha preescucha = new Preescucha();
        preescucha.setTitulo("Preescucha de prueba");
        preescucha.setPrecio(50.0);
        sessionFactory.getCurrentSession().save(preescucha);

        repositorioUsuarioPreescucha.guardar(usuario, preescucha);

        Boolean existe = repositorioUsuarioPreescucha.existePorUsuarioYPreescucha(usuario.getId(), preescucha.getId());
        assertThat(existe, equalTo(true));
    }

    @Test
    @Rollback
    public void buscarUsuarioPreescuchaPorUsuario() {

        Usuario usuario = new Usuario();
        usuario.setUser("prueba");

        sessionFactory.getCurrentSession().save(usuario);

        Artista artista  = new Artista();
        artista.setEmail("prueba@gmail.com");
        artista.setNombre("pedrito");

        sessionFactory.getCurrentSession().save(artista);

        Preescucha preescucha = new Preescucha();
        preescucha.setArtista(artista);
        preescucha.setTitulo("Preescucha de prueba");
        preescucha.setPrecio(100.0);

        sessionFactory.getCurrentSession().save(preescucha);

        repositorioUsuarioPreescucha.guardar(usuario, preescucha);

        UsuarioPreescucha usuarioPreescucha = repositorioUsuarioPreescucha.buscarPorUsuario(usuario.getId()).get(0);

        assertThat(usuarioPreescucha.getUsuario(), equalTo(usuario));

    }

    @Test
    @Rollback
    public void buscarUsuarioPreescuchaPorUsuarioOrdenadoASC() {

        Usuario usuario = new Usuario();
        usuario.setUser("prueba");
        sessionFactory.getCurrentSession().save(usuario);

        Artista artista = new Artista();
        artista.setEmail("prueba@gmail.com");
        artista.setNombre("pedrito");
        sessionFactory.getCurrentSession().save(artista);

        Preescucha preescucha1 = new Preescucha();
        preescucha1.setArtista(artista);
        preescucha1.setTitulo("Preescucha de prueba 1");
        preescucha1.setPrecio(100.0);

        Preescucha preescucha2 = new Preescucha();
        preescucha2.setArtista(artista);
        preescucha2.setTitulo("Preescucha de prueba 2");
        preescucha2.setPrecio(200.0);

        sessionFactory.getCurrentSession().save(preescucha1);
        sessionFactory.getCurrentSession().save(preescucha2);


        UsuarioPreescucha up1 = new UsuarioPreescucha();
        up1.setUsuario(usuario);
        up1.setPreescucha(preescucha1);
        up1.setFechaCompra(LocalDateTime.of(2024, 1, 1, 10, 0));

        UsuarioPreescucha up2 = new UsuarioPreescucha();
        up2.setUsuario(usuario);
        up2.setPreescucha(preescucha2);
        up2.setFechaCompra(LocalDateTime.of(2025, 1, 1, 10, 0));

        sessionFactory.getCurrentSession().save(up1);
        sessionFactory.getCurrentSession().save(up2);


        List<UsuarioPreescucha> usuarioPreescuchas = repositorioUsuarioPreescucha.buscarPorUsuarioOrdenado(usuario.getId(), "ASC");

        assertThat(usuarioPreescuchas, notNullValue());
        assertThat(usuarioPreescuchas.size(), equalTo(2));
        assertThat(usuarioPreescuchas.get(0).getPreescucha().getTitulo(), equalTo("Preescucha de prueba 1"));
        assertThat(usuarioPreescuchas.get(1).getPreescucha().getTitulo(), equalTo("Preescucha de prueba 2"));
    }

    @Test
    @Rollback
    public void buscarUsuarioPreescuchaPorUsuarioOrdenadoDESC() {

        Usuario usuario = new Usuario();
        usuario.setUser("prueba");
        sessionFactory.getCurrentSession().save(usuario);

        Artista artista = new Artista();
        artista.setEmail("prueba@gmail.com");
        artista.setNombre("pedrito");
        sessionFactory.getCurrentSession().save(artista);

        Preescucha preescucha1 = new Preescucha();
        preescucha1.setArtista(artista);
        preescucha1.setTitulo("Preescucha de prueba 1");
        preescucha1.setPrecio(100.0);

        Preescucha preescucha2 = new Preescucha();
        preescucha2.setArtista(artista);
        preescucha2.setTitulo("Preescucha de prueba 2");
        preescucha2.setPrecio(200.0);

        sessionFactory.getCurrentSession().save(preescucha1);
        sessionFactory.getCurrentSession().save(preescucha2);


        UsuarioPreescucha up1 = new UsuarioPreescucha();
        up1.setUsuario(usuario);
        up1.setPreescucha(preescucha1);
        up1.setFechaCompra(LocalDateTime.of(2024, 1, 1, 10, 0));

        UsuarioPreescucha up2 = new UsuarioPreescucha();
        up2.setUsuario(usuario);
        up2.setPreescucha(preescucha2);
        up2.setFechaCompra(LocalDateTime.of(2025, 1, 1, 10, 0));

        sessionFactory.getCurrentSession().save(up1);
        sessionFactory.getCurrentSession().save(up2);


        List<UsuarioPreescucha> usuarioPreescuchas = repositorioUsuarioPreescucha.buscarPorUsuarioOrdenado(usuario.getId(), "DESC");

        assertThat(usuarioPreescuchas, notNullValue());
        assertThat(usuarioPreescuchas.size(), equalTo(2));
        assertThat(usuarioPreescuchas.get(0).getPreescucha().getTitulo(), equalTo("Preescucha de prueba 2"));
        assertThat(usuarioPreescuchas.get(1).getPreescucha().getTitulo(), equalTo("Preescucha de prueba 1"));

    }


    }
