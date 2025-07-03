package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Comunidad;
import com.tallerwebi.dominio.Mensaje;
import com.tallerwebi.dominio.RepositorioMensaje;
import com.tallerwebi.dominio.Usuario;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
@Transactional
public class RepositorioMensajeTest {

    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioMensaje repositorioMensaje;

    @BeforeEach
    public void setUp(){
        repositorioMensaje = new RepositorioMensajeImpl(sessionFactory);
    }

    @Test
    @Rollback
    public void testEliminarMensaje() {
        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Test Comunidad");
        comunidad.setDescripcion("Test Descripcion");

        sessionFactory.getCurrentSession().save(comunidad);

        Usuario usuario = new Usuario();
        usuario.setUser("Test Usuario");
        usuario.setUrlFoto("http://test.com/foto.jpg");

        sessionFactory.getCurrentSession().save(usuario);

        Mensaje mensaje = new Mensaje();
        mensaje.setEstadoMensaje(true);
        mensaje.setComunidad(comunidad);
        mensaje.setUsuario(usuario);
        mensaje.setTexto("Test");



        sessionFactory.getCurrentSession().save(mensaje);

        repositorioMensaje.eliminarMensaje(mensaje.getId());

        String hql = "FROM Mensaje m WHERE m.id = :id";
        Mensaje mensajeEliminado = sessionFactory.getCurrentSession()
                .createQuery(hql, Mensaje.class)
                .setParameter("id", mensaje.getId())
                .uniqueResult();

        assertThat(mensajeEliminado.getEstadoMensaje(), equalTo(false));
        assertThat(mensajeEliminado.getTexto(), equalTo("Test"));
    }

}
