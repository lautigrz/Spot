package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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
public class RepositorioUsuarioImplTest {

    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioUsuario repositorioUsuario;

    @BeforeEach
    public void setUp() {
        repositorioUsuario = new RepositorioUsuarioImpl(sessionFactory);
    }

    @Test
    @Rollback
    public void seDebeObtenerElUsuarioPorSuUser(){
        Usuario usuario = new Usuario();
        usuario.setUser("lautigrz");
        usuario.setToken("dad");
        usuario.setRefreshToken("dsdasd");
        usuario.setUrlFoto("ddas");

        sessionFactory.getCurrentSession().save(usuario);

        Usuario usuarioBuscado = repositorioUsuario.buscar(usuario.getUser());

        assertThat(usuario, equalTo(usuarioBuscado));

    }

    @Test
    @Rollback
    public void seDebeObtenerElUsuarioPorId(){
        Usuario usuario = new Usuario();
        usuario.setUser("lautigrz");
        usuario.setToken("dad");
        usuario.setRefreshToken("dsdasd");
        usuario.setUrlFoto("ddas");

        sessionFactory.getCurrentSession().save(usuario);

        Usuario usuario1 = repositorioUsuario.buscarUsuarioPorId(usuario.getId());

        assertThat(usuario, equalTo(usuario1));


    }



}
