package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Comunidad;
import com.tallerwebi.dominio.RepositorioNuevaComunidad;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.UsuarioComunidad;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
@Transactional
public class RepositorioNuevaComunidadTest {
    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioNuevaComunidad repositorioNuevaComunidad;

    @BeforeEach
    public void setUp() {
        repositorioNuevaComunidad = new RepositorioNuevaComunidadImpl(sessionFactory);
    }

    @Test
    public void seDebeGuardarUnaNuevaComunidadYRetornarSuId() {
        Comunidad comunidad = new Comunidad();

        comunidad.setNombre("Comunidad de prueba");
        comunidad.setDescripcion("Descripción de prueba");
        comunidad.setUrlPortada("http://comunidad-de-prueba.com");
        comunidad.setUrlFoto("http://comunidad-de-prueba.com/imagen.jpg");


        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUser("Usuario de prueba");

        sessionFactory.getCurrentSession().save(usuario);

        UsuarioComunidad usuarioComunidad = new UsuarioComunidad();
        usuarioComunidad.setUsuario(usuario);
        usuarioComunidad.setComunidad(comunidad);

        Long idComunidad = repositorioNuevaComunidad.nuevaComunidad(comunidad, usuario, "ADMIN");

        assertThat(idComunidad, equalTo(comunidad.getId()));
    }

}
