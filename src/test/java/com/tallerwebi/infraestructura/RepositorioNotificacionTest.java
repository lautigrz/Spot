package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
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

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
@Transactional
public class RepositorioNotificacionTest {

    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioNotificacion repositorioNotificacion;

    @BeforeEach
    public void setUp() {
        repositorioNotificacion = new RepositorioNotificacionImpl(sessionFactory);
    }

    @Test
    @Rollback
    public void testObtenerNotificacionesPorUsuario(){

        Usuario usuario = new Usuario();
        usuario.setUser("prueba");
        usuario.setUrlFoto("aasdas");

        sessionFactory.getCurrentSession().save(usuario);

        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("comunidad prueba");

        Cancion cancion = new Cancion();
        cancion.setUri("das:prueba");

        sessionFactory.getCurrentSession().save(cancion);
        sessionFactory.getCurrentSession().save(comunidad);

        Recomendacion recomendacion = new Recomendacion();
        recomendacion.setCancion(cancion);
        recomendacion.setComunidad(comunidad);
        recomendacion.setUsuario(usuario);

        sessionFactory.getCurrentSession().save(recomendacion);

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);

        notificacion.setMensaje("Mensaje de notificacion");

        sessionFactory.getCurrentSession().save(notificacion);
        sessionFactory.getCurrentSession().flush();
        List<Notificacion> notificacions = repositorioNotificacion.obtenerNotificacionesPorUsuario(usuario.getId());

        assertThat(notificacions.get(0), equalTo(notificacion));


    }

    @Test
    @Rollback
    public void testGuardarNotifiacion(){
        Usuario usuario = new Usuario();
        usuario.setUser("prueba");
        usuario.setUrlFoto("aasdas");

        sessionFactory.getCurrentSession().save(usuario);

        repositorioNotificacion.guardarNotificacion("Mensaje de notificacion",usuario);

        String hql = "FROM Notificacion";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);

        Notificacion notificacion = (Notificacion) query.uniqueResult();

        assertThat(query.list().size(), equalTo(1));
        assertThat(notificacion.getMensaje(), equalTo("Mensaje de notificacion"));
        assertThat(notificacion.getUsuario().getUser(), equalTo("prueba"));
    }

    @Test
    @Rollback
    public void seDebeCambiarEstadoDeLeidoDeLaNotificacion(){
        Usuario usuario = new Usuario();
        usuario.setUser("prueba");
        usuario.setUrlFoto("aasdas");

        sessionFactory.getCurrentSession().save(usuario);

        Notificacion notificacion = new Notificacion();
        notificacion.setLeido(false);
        notificacion.setUsuario(usuario);
        notificacion.setMensaje("Mensaje de notificacion");

        sessionFactory.getCurrentSession().save(notificacion);

        List<Long> ids = List.of(notificacion.getId());

        repositorioNotificacion.cambiarEstadoNotificacion(ids);

        sessionFactory.getCurrentSession().clear();

        String hql = "FROM Notificacion";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);

        Notificacion notificacion2 = (Notificacion) query.uniqueResult();

        assertThat(notificacion2.getLeido(), equalTo(true));

    }

    @Test
    @Rollback
    public void testElUsuarioTieneNotificacionesSinLeerDevuelveTrue(){
        Usuario usuario = new Usuario();
        usuario.setUser("prueba");
        usuario.setUrlFoto("aasdas");

        sessionFactory.getCurrentSession().save(usuario);

        Notificacion notificacion = new Notificacion();
        notificacion.setLeido(false);
        notificacion.setUsuario(usuario);

        notificacion.setMensaje("Mensaje de notificacion");

        sessionFactory.getCurrentSession().save(notificacion);

        Boolean tieneNotificaciones = repositorioNotificacion.elUsuarioTieneNotificaciones(usuario.getId());

        assertThat(tieneNotificaciones, equalTo(true));

    }

    @Test
    @Rollback
    public void testElUsuarioTieneNotificacionesQueLeyoYDevuelveFalse(){
        Usuario usuario = new Usuario();
        usuario.setUser("prueba");
        usuario.setUrlFoto("aasdas");

        sessionFactory.getCurrentSession().save(usuario);

        Notificacion notificacion = new Notificacion();
        notificacion.setLeido(true);
        notificacion.setUsuario(usuario);

        notificacion.setMensaje("Mensaje de notificacion");

        sessionFactory.getCurrentSession().save(notificacion);

        Boolean tieneNotificaciones = repositorioNotificacion.elUsuarioTieneNotificaciones(usuario.getId());

        assertThat(tieneNotificaciones, equalTo(false));

    }


}
