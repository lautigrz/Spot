package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioComentario {
    void guardarComentario(Comentario comentario);
    List<Comentario> obtenerComentariosDePosteo(Long idPosteo);
}
