package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServicioLikeImpl implements ServicioLike {

    private RepositorioLike repositorioLike;
    private ServicioUsuario servicioUsuario;
    private ServicioPosteo servicioPosteo;

    public ServicioLikeImpl(RepositorioLike repositorioLike, ServicioUsuario servicioUsuario, ServicioPosteo servicioPosteo) {
        this.repositorioLike = repositorioLike;
        this.servicioUsuario = servicioUsuario;
        this.servicioPosteo = servicioPosteo;
    }

    @Override
    public void darLikeAPosteo(Long idPosteo, Long idUsuario) {
        Post posteo = servicioPosteo.obtenerPosteoPorId(idPosteo);
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        if (posteo == null || usuario == null) {
            throw new IllegalArgumentException("Posteo o Usuario no encontrado");
        }
        repositorioLike.darLikeAPosteo(posteo, usuario);
    }

    @Override
    public void quitarLikeAPosteo(Long idPosteo, Long idUsuario) {
        Post posteo = servicioPosteo.obtenerPosteoPorId(idPosteo);
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        if (posteo == null || usuario == null) {
            throw new IllegalArgumentException("Posteo o Usuario no encontrado");
        }
        repositorioLike.quitarLikeAPosteo(posteo, usuario);
    }

    @Override
    public List<Post> obtenerPostConLikeDeUsuario(Long idUsuario) {
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        if (usuario != null) {
            return repositorioLike.obtenerPostConLikeDeUsuario(usuario);
        }
        return List.of();
    }

    @Override
    public List<Long> devolverIdsDePostConLikeDeUsuarioDeUnaListaDePosts(Long idUsuario, List<Long> ids) {
        return repositorioLike.devolverIdsDePostConLikeDeUsuarioDeUnaListaDePosts(idUsuario, ids);
    }


}
