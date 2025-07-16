package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioComentario {
    void comentarEnPosteo(Long idUsuario, Long idPost, String texto);
    List<Comentario> obtenerComentariosDePosteo(Long idPost);
}
