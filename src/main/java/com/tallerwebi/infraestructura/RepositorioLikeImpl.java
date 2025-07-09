package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Like;
import com.tallerwebi.dominio.Post;
import com.tallerwebi.dominio.RepositorioLike;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Repository
public class RepositorioLikeImpl implements RepositorioLike {

    private SessionFactory sessionFactory;

    public RepositorioLikeImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void darLikeAPosteo(Post posteo, Usuario usuario) {
        Like like = new Like();
        like.setUsuario(usuario);
        like.setPost(posteo);
        posteo.getLikes().add(like);

        sessionFactory.getCurrentSession().save(posteo);
    }

    @Override
    public void quitarLikeAPosteo(Post posteo, Usuario usuario) {

        String hql = "FROM Like l WHERE l.post.id = :postId AND l.usuario.id = :usuarioId";
        Like like = (Like) sessionFactory.getCurrentSession()
                .createQuery(hql)
                .setParameter("postId", posteo.getId())
                .setParameter("usuarioId", usuario.getId())
                .uniqueResult();

        sessionFactory.getCurrentSession().delete(like);

    }

    @Override
    public List<Post> obtenerPostConLikeDeUsuario(Usuario usuario) {

        String hql = "SELECT l.post FROM Like l WHERE l.usuario.id = :usuarioId";
        List<Post> posts = sessionFactory.getCurrentSession()
                .createQuery(hql, Post.class)
                .setParameter("usuarioId", usuario.getId())
                .getResultList();

        return posts;
    }

    @Override
    public List<Long> devolverIdsDePostConLikeDeUsuarioDeUnaListaDePosts(Long idUsuario, List<Long> ids) {
        String hql = "SELECT l.post.id FROM Like l WHERE l.usuario.id = :usuarioId AND l.post.id IN :ids";
        List<Long> postIds = sessionFactory.getCurrentSession()
                .createQuery(hql, Long.class)
                .setParameter("usuarioId", idUsuario)
                .setParameterList("ids", ids)
                .getResultList();

        return postIds;
    }
}
