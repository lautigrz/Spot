package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Preescucha;
import com.tallerwebi.dominio.RepositorioPreescucha;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
@Transactional
public class RepositorioPreescuchaImplTest {


    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioPreescucha repositorioPreescucha;

    @BeforeEach
    public void setUp() {
        repositorioPreescucha = new RepositorioPreescuchaImpl(sessionFactory);
    }


    @Test
    @Rollback
    public void deberiaGuardarUnAlbumDePreescuchaCompradoParaUnUsuario(){
        Usuario usuario = new Usuario();
        usuario.setUser("testuser");
        usuario.setToken("token");
        usuario.setRefreshToken("refresh");
        usuario.setUrlFoto("foto.jpg");
        sessionFactory.getCurrentSession().save(usuario);

        Preescucha preescucha = new Preescucha();
        preescucha.setUsuario(usuario);
        preescucha.setSpotifyAlbumId("05IR0uSEjd7nu4ZgQnIMiA");
        preescucha.setFechaCompra(LocalDateTime.now());
        sessionFactory.getCurrentSession().save(preescucha);

        //Metodo a testear
        List<Preescucha> preescuchas = repositorioPreescucha.obtenerComprasPorUsuario(usuario.getId());
        //Que solamente se haya agregado un album comprado
        assertThat(preescuchas.size(), equalTo(1));
        //Que el id del album este ok
        assertThat(preescuchas.get(0).getSpotifyAlbumId(), equalTo("05IR0uSEjd7nu4ZgQnIMiA"));
        //Que el album comprado pertenezca al usuario correcto
        assertThat(preescuchas.get(0).getUsuario().getId(), equalTo(usuario.getId()));
    }

    @Test
    @Rollback
    public void deberiaEncontrarSiUnAlbumDePreescuchaYaFueCompradoPorElUsuario(){
        Usuario usuario = new Usuario();
        usuario.setUser("testuser");
        usuario.setToken("token");
        usuario.setRefreshToken("refresh");
        usuario.setUrlFoto("foto.jpg");
        sessionFactory.getCurrentSession().save(usuario);

        Preescucha preescucha = new Preescucha();
        preescucha.setUsuario(usuario);
        preescucha.setSpotifyAlbumId("05IR0uSEjd7nu4ZgQnIMiA");
        preescucha.setFechaCompra(LocalDateTime.now());
        sessionFactory.getCurrentSession().save(preescucha);

        //Metodo a testear
        boolean fueComprado = repositorioPreescucha.existeCompra(preescucha.getSpotifyAlbumId(), usuario.getId());
        //Si fue comprado ya esta todo ok
        assertThat(fueComprado, equalTo(true));


    }
}
