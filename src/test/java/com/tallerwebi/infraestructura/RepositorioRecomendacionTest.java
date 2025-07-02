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

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
@Transactional
public class RepositorioRecomendacionTest {
    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioRecomendacion repositorioRecomendacion;

    @BeforeEach
    public void setUp() {
        repositorioRecomendacion = new RepositorioRecomendacionImpl(sessionFactory);
    }

    @Test
    @Rollback
    public void testAgregarRecomendacion() {
     Cancion c = new Cancion();
        c.setSpotifyId("spotify:track:1234567890");
        c.setTitulo("Test Song");
        c.setArtista("Test Artist");
        c.setUrlImagen("http://example.com/image.jpg");

        sessionFactory.getCurrentSession().save(c);

        Usuario u = new Usuario();
        u.setUser("prueba");

        sessionFactory.getCurrentSession().save(u);

        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Comunidad de Prueba");
        comunidad.setDescripcion("Descripción de la comunidad de prueba");

        sessionFactory.getCurrentSession().save(comunidad);

        UsuarioComunidad usuarioComunidad = new UsuarioComunidad();
        usuarioComunidad.setUsuario(u);
        usuarioComunidad.setComunidad(comunidad);
        usuarioComunidad.setRol("Miembro");

        sessionFactory.getCurrentSession().save(usuarioComunidad);

        Recomendacion recom = new Recomendacion();
        recom.setUsuario(usuarioComunidad.getUsuario());
        recom.setComunidad(usuarioComunidad.getComunidad());
        recom.setCancion(c);
        recom.setEstado(false);

        repositorioRecomendacion.agregarRecomendacion(recom);

        String hql = "FROM Recomendacion r WHERE r.usuario.id = :idUsuario AND r.comunidad.id = :idComunidad";
        Recomendacion recomendacion = sessionFactory.getCurrentSession()
                .createQuery(hql, Recomendacion.class)
                .setParameter("idUsuario", u.getId())
                .setParameter("idComunidad", comunidad.getId())
                .uniqueResult();

        assertThat(recomendacion,equalTo(recom));



    }

    @Test
    @Rollback
    public void testEliminarRecomendacion() {
        Cancion c = new Cancion();
        c.setSpotifyId("spotify:track:1234567890");
        c.setTitulo("Test Song");
        c.setArtista("Test Artist");
        c.setUrlImagen("http://example.com/image.jpg");

        sessionFactory.getCurrentSession().save(c);

        Usuario u = new Usuario();
        u.setUser("prueba");

        sessionFactory.getCurrentSession().save(u);

        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Comunidad de Prueba");
        comunidad.setDescripcion("Descripción de la comunidad de prueba");

        sessionFactory.getCurrentSession().save(comunidad);

        UsuarioComunidad usuarioComunidad = new UsuarioComunidad();
        usuarioComunidad.setUsuario(u);
        usuarioComunidad.setComunidad(comunidad);
        usuarioComunidad.setRol("Miembro");

        sessionFactory.getCurrentSession().save(usuarioComunidad);

        Recomendacion recom = new Recomendacion();
        recom.setUsuario(usuarioComunidad.getUsuario());
        recom.setComunidad(usuarioComunidad.getComunidad());
        recom.setCancion(c);
        recom.setEstado(false);

        repositorioRecomendacion.agregarRecomendacion(recom);

        Long idRecomendacion = recom.getId();

        repositorioRecomendacion.eliminarRecomendacion(idRecomendacion);

        Recomendacion recomendacionEliminada = sessionFactory.getCurrentSession().get(Recomendacion.class, idRecomendacion);

        assertThat(recomendacionEliminada.getEstado(), equalTo(false));
    }

    @Test
    @Rollback
    public void testObtenerRecomendacionesPorComunidad() {
        Cancion c = new Cancion();
        c.setSpotifyId("spotify:track:1234567890");
        c.setTitulo("Test Song");
        c.setArtista("Test Artist");
        c.setUrlImagen("http://example.com/image.jpg");

        sessionFactory.getCurrentSession().save(c);

        Usuario u = new Usuario();
        u.setUser("prueba");

        sessionFactory.getCurrentSession().save(u);

        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Comunidad de Prueba");
        comunidad.setDescripcion("Descripción de la comunidad de prueba");

        sessionFactory.getCurrentSession().save(comunidad);

        UsuarioComunidad usuarioComunidad = new UsuarioComunidad();
        usuarioComunidad.setUsuario(u);
        usuarioComunidad.setComunidad(comunidad);
        usuarioComunidad.setRol("Miembro");

        sessionFactory.getCurrentSession().save(usuarioComunidad);

        Recomendacion recom = new Recomendacion();
        recom.setUsuario(usuarioComunidad.getUsuario());
        recom.setComunidad(usuarioComunidad.getComunidad());
        recom.setCancion(c);
        recom.setEstado(false);

        repositorioRecomendacion.agregarRecomendacion(recom);

        Long idComunidad = comunidad.getId();

        List<Recomendacion> recomendaciones = repositorioRecomendacion.obtenerRecomendacionesPorComunidad(idComunidad);

        assertThat(recomendaciones.size(), equalTo(1));
        assertThat(recomendaciones.get(0), equalTo(recom));
    }

    @Test
    @Rollback
    public void testAceptarRecomendacion() {
        Cancion c = new Cancion();
        c.setSpotifyId("spotify:track:1234567890");
        c.setTitulo("Test Song");
        c.setArtista("Test Artist");
        c.setUrlImagen("http://example.com/image.jpg");

        sessionFactory.getCurrentSession().save(c);

        Usuario u = new Usuario();
        u.setUser("prueba");

        sessionFactory.getCurrentSession().save(u);

        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Comunidad de Prueba");
        comunidad.setDescripcion("Descripción de la comunidad de prueba");

        sessionFactory.getCurrentSession().save(comunidad);

        UsuarioComunidad usuarioComunidad = new UsuarioComunidad();
        usuarioComunidad.setUsuario(u);
        usuarioComunidad.setComunidad(comunidad);
        usuarioComunidad.setRol("Miembro");

        sessionFactory.getCurrentSession().save(usuarioComunidad);

        Recomendacion recom = new Recomendacion();
        recom.setUsuario(usuarioComunidad.getUsuario());
        recom.setComunidad(usuarioComunidad.getComunidad());
        recom.setCancion(c);
        recom.setEstado(false);

        repositorioRecomendacion.agregarRecomendacion(recom);

        Long idRecomendacion = recom.getId();

        Recomendacion recomendacionAceptada = repositorioRecomendacion.aceptarRecomendacion(idRecomendacion);

        assertThat(recomendacionAceptada.getEstado(), equalTo(true));
    }

    @Test
    @Rollback
    public void testObtenerRecomendacionesPorComunidadQueNoFueronLeidas() {
        Cancion c = new Cancion();
        c.setSpotifyId("spotify:track:1234567890");
        c.setTitulo("Test Song");
        c.setArtista("Test Artist");
        c.setUrlImagen("http://example.com/image.jpg");

        sessionFactory.getCurrentSession().save(c);

        Usuario u = new Usuario();
        u.setUser("prueba");

        sessionFactory.getCurrentSession().save(u);

        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Comunidad de Prueba");
        comunidad.setDescripcion("Descripción de la comunidad de prueba");

        sessionFactory.getCurrentSession().save(comunidad);

        UsuarioComunidad usuarioComunidad = new UsuarioComunidad();
        usuarioComunidad.setUsuario(u);
        usuarioComunidad.setComunidad(comunidad);
        usuarioComunidad.setRol("Miembro");

        sessionFactory.getCurrentSession().save(usuarioComunidad);

        Recomendacion recom = new Recomendacion();
        recom.setUsuario(usuarioComunidad.getUsuario());
        recom.setComunidad(usuarioComunidad.getComunidad());
        recom.setCancion(c);
        recom.setEstado(false);
        recom.setLeida(false);

        Recomendacion recom1 = new Recomendacion();
        recom1.setUsuario(usuarioComunidad.getUsuario());
        recom1.setComunidad(usuarioComunidad.getComunidad());
        recom1.setCancion(c);
        recom1.setEstado(false);
        recom1.setLeida(true);

        repositorioRecomendacion.agregarRecomendacion(recom);
        repositorioRecomendacion.agregarRecomendacion(recom1);

        Long idComunidad = comunidad.getId();

        List<Recomendacion> recomendacionesNoLeidas = repositorioRecomendacion.obtenerRecomendacionesPorComunidadQueNoFueronLeidas(idComunidad);

        assertThat(recomendacionesNoLeidas.size(), equalTo(1));
        assertThat(recomendacionesNoLeidas.get(0), equalTo(recom));
    }

    @Test
    @Rollback
    public void testObtenerRecomendacionPorId() {
        Cancion c = new Cancion();
        c.setSpotifyId("spotify:track:1234567890");
        c.setTitulo("Test Song");
        c.setArtista("Test Artist");
        c.setUrlImagen("http://example.com/image.jpg");

        sessionFactory.getCurrentSession().save(c);

        Usuario u = new Usuario();
        u.setUser("prueba");

        sessionFactory.getCurrentSession().save(u);

        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Comunidad de Prueba");
        comunidad.setDescripcion("Descripción de la comunidad de prueba");

        sessionFactory.getCurrentSession().save(comunidad);

        UsuarioComunidad usuarioComunidad = new UsuarioComunidad();
        usuarioComunidad.setUsuario(u);
        usuarioComunidad.setComunidad(comunidad);
        usuarioComunidad.setRol("Miembro");

        sessionFactory.getCurrentSession().save(usuarioComunidad);

        Recomendacion recom = new Recomendacion();
        recom.setUsuario(usuarioComunidad.getUsuario());
        recom.setComunidad(usuarioComunidad.getComunidad());
        recom.setCancion(c);
        recom.setEstado(false);

        repositorioRecomendacion.agregarRecomendacion(recom);

        Long idRecomendacion = recom.getId();

        Recomendacion recomendacionObtenida = repositorioRecomendacion.obtenerRecomendacionPorId(idRecomendacion);

        assertThat(recomendacionObtenida, equalTo(recom));
    }

}
