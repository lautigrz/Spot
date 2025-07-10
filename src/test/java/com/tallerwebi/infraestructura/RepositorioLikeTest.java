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
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
@Transactional
public class RepositorioLikeTest {

    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioLike repositorioLike;


    @BeforeEach
    public void setUp() {
        repositorioLike = new RepositorioLikeImpl(sessionFactory);
    }

    @Test
    @Rollback
    public void seDebeDarLikeAPosteo() {


        Artista artista = new Artista();
        artista.setNombre("Artista");
        artista.setEmail("asdsadd@gmail.com");
        sessionFactory.getCurrentSession().save(artista);

        Usuario usuario = new Usuario();
        usuario.setUser("lautigrz");
        usuario.setUrlFoto("https://example.com/foto.jpg");

        sessionFactory.getCurrentSession().save(usuario);

        Post posteo = new Post();
        posteo.setContenido("Posteo prueba");
        posteo.setArtista(artista);
        posteo.setFecha(LocalDateTime.now());
        sessionFactory.getCurrentSession().save(posteo);

        repositorioLike.darLikeAPosteo(posteo, usuario);

        String hql = "FROM Like l WHERE l.post.id = :postId AND l.usuario.id = :usuarioId";
        Like like = (Like) sessionFactory.getCurrentSession()
                .createQuery(hql)
                .setParameter("postId", posteo.getId())
                .setParameter("usuarioId", usuario.getId())
                .uniqueResult();
        assertThat(like.getUsuario(), equalTo(usuario));
        assertThat(like.getPost(), equalTo(posteo));

        assertThat(posteo.getLikes().size(), equalTo(1));

    }

    @Test
    @Rollback
    public void seDebeDarLikeAPosteoUsuario() {

        Artista artista = new Artista();
        artista.setNombre("Artista");
        artista.setEmail("asdsadd@gmail.com");
        sessionFactory.getCurrentSession().save(artista);

        Usuario usuario = new Usuario();
        usuario.setUser("lautigrz");
        usuario.setUrlFoto("https://example.com/foto.jpg");

        sessionFactory.getCurrentSession().save(usuario);

        Post posteo = new Post();
        posteo.setContenido("Posteo prueba");
        posteo.setArtista(artista);
        posteo.setFecha(LocalDateTime.now());
        sessionFactory.getCurrentSession().save(posteo);

        repositorioLike.darLikeAPosteo(posteo, usuario);


        repositorioLike.quitarLikeAPosteo(posteo, usuario);

        String hql = "FROM Like l WHERE l.post.id = :postId AND l.usuario.id = :usuarioId";
        Like like = (Like) sessionFactory.getCurrentSession()
                .createQuery(hql)
                .setParameter("postId", posteo.getId())
                .setParameter("usuarioId", usuario.getId())
                .uniqueResult();

        assertThat(like,equalTo(null));

    }

    @Test
    @Rollback
    public void seDebeObtenerPostConLikeDeUsuario() {
        Artista artista = new Artista();
        artista.setNombre("Artista");
        artista.setEmail("asdsadd@gmail.com");
        sessionFactory.getCurrentSession().save(artista);

        Usuario usuario = new Usuario();
        usuario.setUser("lautigrz");
        usuario.setUrlFoto("https://example.com/foto.jpg");

        sessionFactory.getCurrentSession().save(usuario);

        Post posteo = new Post();
        posteo.setContenido("Posteo prueba");
        posteo.setArtista(artista);
        posteo.setFecha(LocalDateTime.now());
        sessionFactory.getCurrentSession().save(posteo);

        repositorioLike.darLikeAPosteo(posteo, usuario);

        List<Post> post = repositorioLike.obtenerPostConLikeDeUsuario(usuario);
        assertThat(post.size(), equalTo(1));
        assertThat(post.get(0).getId(), equalTo(posteo.getId()));


    }

    @Test
    @Rollback
    public void seDebeDevolverIdsDePostConLikeDeUsuarioDeUnaListaDePosts() {

        Artista artista = new Artista();
        artista.setNombre("Artista");
        artista.setEmail("asdsadd@gmail.com");
        sessionFactory.getCurrentSession().save(artista);

        Usuario usuario = new Usuario();
        usuario.setUser("lautigrz");
        usuario.setUrlFoto("https://example.com/foto.jpg");

        sessionFactory.getCurrentSession().save(usuario);

        Post posteo = new Post();
        posteo.setContenido("Posteo prueba");
        posteo.setArtista(artista);
        posteo.setFecha(LocalDateTime.now());
        sessionFactory.getCurrentSession().save(posteo);

        Post posteo2 = new Post();
        posteo2.setContenido("Posteo prueba 2");
        posteo2.setArtista(artista);
        posteo2.setFecha(LocalDateTime.now());

        sessionFactory.getCurrentSession().save(posteo2);

        repositorioLike.darLikeAPosteo(posteo, usuario);

        List<Long > ids = List.of(posteo.getId(), posteo2.getId());

        List<Long> postIds = repositorioLike.devolverIdsDePostConLikeDeUsuarioDeUnaListaDePosts(usuario.getId(), ids);
        assertThat(postIds.size(), equalTo(1));
        assertThat(postIds.get(0), equalTo(posteo.getId()));

    }

}
