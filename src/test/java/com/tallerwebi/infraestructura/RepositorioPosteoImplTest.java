package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Artista;
import com.tallerwebi.dominio.Post;
import com.tallerwebi.dominio.RepositorioPosteo;
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
public class RepositorioPosteoImplTest {

    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioPosteo repositorioPosteo;

    @BeforeEach
    public void setUp() {
        repositorioPosteo = new RepositorioPosteoImpl(sessionFactory);
    }

    @Test
    @Rollback
    public void deberiaGuardarUnPost() {
        Artista artista = new Artista();
        artista.setNombre("Facu");
        artista.setEmail("facu8cabj@gmail.com");
        artista.setPassword("1234");
        artista.setFotoPerfil("foto.jpg");
        sessionFactory.getCurrentSession().save(artista);

        Post post = new Post();
        post.setContenido("Nuevo lanzamiento");
        post.setFecha(LocalDateTime.now());
        post.setArtista(artista);

        repositorioPosteo.guardar(post);

        List<Post> posts = repositorioPosteo.obtenerPostsPorArtista(artista.getId());

        assertThat(posts.size(), equalTo(1));
        assertThat(posts.get(0).getArtista().getId(), equalTo(artista.getId()));
    }


    @Test
    @Rollback
    public void deberiaObtenerPostsDeArtistasFavoritos(){
        Artista artista1 = new Artista();
        artista1.setNombre("Facu 1");
        artista1.setEmail("facuuno@test.com");
        artista1.setPassword("1234");
        artista1.setFotoPerfil("foto1.jpg");
        sessionFactory.getCurrentSession().save(artista1);

        Artista artista2 = new Artista();
        artista2.setNombre("Facu Dos");
        artista2.setEmail("facudos@test.com");
        artista2.setPassword("1234");
        artista2.setFotoPerfil("foto2.jpg");
        sessionFactory.getCurrentSession().save(artista2);

        Post post1 = new Post();
        post1.setContenido("Post de Artista 1");
        post1.setFecha(LocalDateTime.now());
        post1.setArtista(artista1);
        sessionFactory.getCurrentSession().save(post1);

        Post post2 = new Post();
        post2.setContenido("Post de Artista 2");
        post2.setFecha(LocalDateTime.now());
        post2.setArtista(artista2);
        sessionFactory.getCurrentSession().save(post2);

        //Solamente seguimos al artista 1
        List<Post> posts = repositorioPosteo.obtenerPostsDeArtistasFavoritos(List.of(artista1.getId()));

        assertThat(posts.size(), equalTo(1));
        assertThat(posts.get(0).getArtista().getNombre(), equalTo("Facu 1"));
    }
}
