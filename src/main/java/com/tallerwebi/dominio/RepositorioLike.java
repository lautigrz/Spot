package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioLike {

    void darLikeAPosteo(Post posteo, Usuario usuario);
    void quitarLikeAPosteo(Post posteo, Usuario usuario);
    List<Post> obtenerPostConLikeDeUsuario(Usuario usuario);
    List<Long> devolverIdsDePostConLikeDeUsuarioDeUnaListaDePosts(Long idUsuario, List<Long> ids);
}
