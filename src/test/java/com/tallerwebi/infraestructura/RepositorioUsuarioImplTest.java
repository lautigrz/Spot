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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
public class RepositorioUsuarioImplTest {

    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioUsuario repositorioUsuario;

    @BeforeEach
    public void setUp() {
        repositorioUsuario = new RepositorioUsuarioImpl(sessionFactory);
    }

    @Test
    @Transactional
    public void seDebeObtenerElUsuarioPorSuUser(){
        Usuario usuario = new Usuario();
        usuario.setUser("lautigrz");
        usuario.setToken("dad");
        usuario.setRefreshToken("dsdasd");
        usuario.setUrlFoto("ddas");

        sessionFactory.getCurrentSession().save(usuario);

        String hql = "from Usuario where user = :user";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("user", usuario.getUser());
        Usuario usuario2 = (Usuario) query.uniqueResult();

        assertThat(usuario, equalTo(usuario2));

    }



}
