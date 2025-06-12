package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.EstadoDeAnimo;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
@Transactional
public class RepositorioEstadoDeAnimoImplTest {

    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioEstadoDeAnimoImpl repositorioEstadoDeAnimo;

    @BeforeEach
    public void setUp() {
    repositorioEstadoDeAnimo = new RepositorioEstadoDeAnimoImpl(sessionFactory);
    }

    @Test
    public void obtenerEstadoDeAnimoPorID(){
        EstadoDeAnimo estado = new EstadoDeAnimo();
        estado.setId(1L);
        sessionFactory.getCurrentSession().save(estado);

        EstadoDeAnimo buscado = repositorioEstadoDeAnimo.obtenerEstadoDeAnimoPorId(estado.getId());

        assertThat(estado, equalTo(buscado));
    }

    @Test
    public void obtenerTodosLosEstadosDeAnimo(){
    EstadoDeAnimo estado1 = new EstadoDeAnimo();
    EstadoDeAnimo estado2 = new EstadoDeAnimo();

    sessionFactory.getCurrentSession().save(estado1);
    sessionFactory.getCurrentSession().save(estado2);

    List<EstadoDeAnimo> resultado = repositorioEstadoDeAnimo.obtenerTodosLosEstadosDeAnimo();

    assertThat(resultado.size(), equalTo(2));
    assertThat(resultado.containsAll(Arrays.asList(estado1, estado2)), equalTo(true));
    }

}
