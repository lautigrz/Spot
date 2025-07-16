package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ServicioComentarioImpl implements ServicioComentario {

    private final RepositorioComentario repositorioComentario;
    private final ServicioUsuario servicioUsuario;
    private final ServicioPosteo servicioPosteo;


    public ServicioComentarioImpl(RepositorioComentario repositorioComentario, ServicioUsuario servicioUsuario, ServicioPosteo servicioPosteo) {
        this.repositorioComentario = repositorioComentario;
        this.servicioUsuario = servicioUsuario;
        this.servicioPosteo = servicioPosteo;
    }

    @Override
    public void comentarEnPosteo(Long idUsuario, Long idPost, String texto) {
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        Post post = servicioPosteo.obtenerPosteoPorId(idPost);

        if (usuario == null || post == null) {
            throw new IllegalArgumentException("Usuario o Post no encontrado");
        }

        Comentario comentario = new Comentario();
        comentario.setUsuario(usuario);
        comentario.setPost(post);
        comentario.setTexto(texto);
        comentario.setFecha(LocalDateTime.now());

        repositorioComentario.guardarComentario(comentario);
    }

    @Override
    public List<Comentario> obtenerComentariosDePosteo(Long idPost) {
        return repositorioComentario.obtenerComentariosDePosteo(idPost);
    }
}
