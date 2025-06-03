package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Mensaje;
import com.tallerwebi.dominio.RepositorioComunidad;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
public class RepositorioComunidadImplTest {

    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioComunidad repositorioComunidad;

    @BeforeEach
    public void setUp() {
        repositorioComunidad = new RepositorioComunidadImpl(sessionFactory);
    }

  /*
    @Test
    @Transactional
    public void seDebeGuardarElMensajeEnLaBD(){

        Usuario usuario = new Usuario();

        usuario.setToken("223");
        usuario.setUrlFoto("https://");
        usuario.setUser("lauti");
        usuario.setRefreshToken("das2");

        sessionFactory.getCurrentSession().save(usuario);

        String mensaje = "Hola como estas";

        repositorioComunidad.guardarMensajeDeLaComunidad(mensaje, usuario.getId());

        String hql = "FROM Mensaje WHERE texto = :texto";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("texto", mensaje);

        Mensaje mensajeObtenido = (Mensaje) query.getResultList().get(0);

        assertThat(mensajeObtenido.getTexto(), equalTo(mensaje));
        assertThat(mensajeObtenido.getUsuario().getId(), equalTo(usuario.getId()));

    }

   */

    @Test
    @Transactional
    public void seDebeObtenerUnaListaDeMensajes() {
        Usuario usuario1 = new Usuario();
        usuario1.setToken("223");
        usuario1.setUrlFoto("https://");
        usuario1.setUser("lauti");
        usuario1.setRefreshToken("das2");

        sessionFactory.getCurrentSession().save(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setToken("224");
        usuario2.setUrlFoto("https://otro");
        usuario2.setUser("lautdi");
        usuario2.setRefreshToken("das3");

        sessionFactory.getCurrentSession().save(usuario2);

        String texto = "Hola como estas";
        String texto1 = "Hola, bien";

        Mensaje mensaje1 = new Mensaje();
        mensaje1.setTexto(texto);
        mensaje1.setUsuario(usuario1);

        Mensaje mensaje2 = new Mensaje();
        mensaje2.setTexto(texto1);
        mensaje2.setUsuario(usuario2);

        sessionFactory.getCurrentSession().save(mensaje1);
        sessionFactory.getCurrentSession().save(mensaje2);

        String hql = "FROM Mensaje";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);

        List<Mensaje> mensajesObtenido = query.getResultList();

        assertThat(mensajesObtenido.size(), equalTo(2));
        assertThat(mensajesObtenido,equalTo(List.of(mensaje1, mensaje2)));
    }



}
