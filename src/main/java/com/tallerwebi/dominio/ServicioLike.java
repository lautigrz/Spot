package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioLike {
    void darLikeAPosteo(Long idPosteo, Long idUsuario);
    void quitarLikeAPosteo(Long idPosteo, Long idUsuario);
    List<Post> obtenerPostConLikeDeUsuario(Long idUsuario);
    List<Long> devolverIdsDePostConLikeDeUsuarioDeUnaListaDePosts(Long idUsuario, List<Long> ids);


}
