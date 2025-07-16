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

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
@Transactional
public class RepositorioComentarioTest {

    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioComentario repositorioComentario;

    @BeforeEach
    public void setUp() {
        repositorioComentario = new RepositorioComentarioImpl(sessionFactory);
    }

    @Test
    @Rollback
    public void seDebePoderGuardarUnComentario(){
        Artista artista = new Artista();
        artista.setNombre("Artista");
        artista.setEmail("artista@test.com");
        sessionFactory.getCurrentSession().save(artista);

        Usuario usuario = new Usuario();
        usuario.setUser("facupe");
        sessionFactory.getCurrentSession().save(usuario);


        Post posteo = new Post();
        posteo.setContenido("Post de prueba");
        posteo.setArtista(artista);
        posteo.setFecha(LocalDateTime.now());
        sessionFactory.getCurrentSession().save(posteo);

        Comentario comentario = new Comentario();
        comentario.setUsuario(usuario);
        comentario.setPost(posteo);
        comentario.setTexto("Comentario 1");
        comentario.setFecha(LocalDateTime.now());

        repositorioComentario.guardarComentario(comentario);

        List<Comentario> comentarios = repositorioComentario.obtenerComentariosDePosteo(posteo.getId());
        assertThat(comentarios, hasSize(1));
        assertThat(comentarios.get(0).getTexto(), equalTo("Comentario 1"));
    }

    @Test
    @Rollback
    public void seDebeObtenerUnaListaVaciaCuandoNoHayComentarios(){
        Artista artista = new Artista();
        artista.setNombre("Artista");
        artista.setEmail("artista@test.com");
        sessionFactory.getCurrentSession().save(artista);

        Post posteo = new Post();
        posteo.setArtista(artista);
        posteo.setContenido("Post de prueba");
        posteo.setFecha(LocalDateTime.now());
        sessionFactory.getCurrentSession().save(posteo);

        List<Comentario> comentarios = repositorioComentario.obtenerComentariosDePosteo(posteo.getId());

        assertThat(comentarios, empty());
    }


}
