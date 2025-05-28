package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioAuth;
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
public class RepositorioAuthImplTest {
    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioAuth repositorioAuth;

    @BeforeEach
    public void setUp() {
        repositorioAuth = new RepositorioAuthImpl(sessionFactory);
    }

    @Test
    @Transactional
    public void debeGuardarUnUsuario() {
        Usuario usuario = new Usuario();
        usuario.setUser("lautigrz");
        usuario.setToken("dad");
        usuario.setRefreshToken("dsdasd");
        usuario.setUrlFoto("ddas");

        repositorioAuth.guardar(usuario);

        String hql = "from Usuario where id = :id";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", usuario.getId());
        Usuario usuarioGuardado = (Usuario) query.uniqueResult();

        assertThat(usuario, equalTo(usuarioGuardado));

    }
}
