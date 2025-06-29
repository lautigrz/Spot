package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Comunidad;
import com.tallerwebi.dominio.Evento;
import com.tallerwebi.dominio.RepositorioEvento;
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

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
@Transactional
public class RepositorioEventoTest {

    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioEvento repositorioEvento;

    @BeforeEach
    public void setUp() {
        repositorioEvento = new RepositorioEventoImpl(sessionFactory);
    }

    @Test
    @Rollback
    public void seDebeAgregarUnEvento() {

        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Comunidad de Prueba");
        comunidad.setDescripcion("Descripción de la comunidad de prueba");

        sessionFactory.getCurrentSession().save(comunidad);

        Evento evento = new Evento();
        evento.setNombre("Evento de Prueba");
        evento.setComunidad(comunidad);
        evento.setFecha(LocalDate.now());

        repositorioEvento.agregarEvento(evento);

        Evento eventoGuardado = sessionFactory.getCurrentSession().get(Evento.class, evento.getId());

        assertThat(eventoGuardado.getNombre(), equalTo("Evento de Prueba"));
        assertThat(eventoGuardado.getComunidad(), equalTo(comunidad));

    }

    @Test
    @Rollback
    public void seDebeObtenerEventosDeLaComunidad() {

        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Comunidad de Prueba");
        comunidad.setDescripcion("Descripción de la comunidad de prueba");

        sessionFactory.getCurrentSession().save(comunidad);

        Evento evento1 = new Evento();
        evento1.setNombre("Evento 1");
        evento1.setComunidad(comunidad);
        evento1.setFecha(LocalDate.now());

        Evento evento2 = new Evento();
        evento2.setNombre("Evento 2");
        evento2.setComunidad(comunidad);
        evento2.setFecha(LocalDate.now().plusDays(1));

        repositorioEvento.agregarEvento(evento1);
        repositorioEvento.agregarEvento(evento2);

        List<Evento> eventos = repositorioEvento.obetenerEventosDeLaComunidad(comunidad.getId());

        assertThat(eventos.size(), equalTo(2));
        assertThat(eventos.get(0).getNombre(), equalTo("Evento 1"));
        assertThat(eventos.get(1).getNombre(), equalTo("Evento 2"));
    }
}
