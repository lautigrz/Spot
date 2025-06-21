package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Comunidad;
import com.tallerwebi.dominio.RepositorioNuevaComunidad;
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
    public void seDebeGuardarUnaNuevaComunidadYRetornarla() {
        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Comunidad de prueba");
        comunidad.setDescripcion("Descripción de prueba");
        comunidad.setUrlFoto("imagen_de_prueba.jpg");
        comunidad.setUrlPortada("https://www.ejemplo.com/comunidad-de-prueba");

        Comunidad comunidadGuardada = repositorioNuevaComunidad.nuevaComunidad(comunidad);

        assertThat(comunidadGuardada.getNombre(), equalTo("Comunidad de prueba"));
        assertThat(comunidadGuardada, equalTo(comunidad));
    }

}
