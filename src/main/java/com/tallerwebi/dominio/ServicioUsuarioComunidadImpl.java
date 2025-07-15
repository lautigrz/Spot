package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ServicioUsuarioComunidadImpl implements ServicioUsuarioComunidad {
    @Autowired
    private RepositorioUsuarioComunidad repositorioUsuarioComunidad;
    @Autowired
    private ServicioUsuario servicioUsuario;

    @Autowired
    private ServicioComunidad servicioComunidad;

    private ServicioPosteo servicioPosteo;

    public ServicioUsuarioComunidadImpl(RepositorioUsuarioComunidad repositorioUsuarioComunidad, ServicioPosteo servicioPosteo) {
        this.repositorioUsuarioComunidad = repositorioUsuarioComunidad;
        this.servicioPosteo = servicioPosteo;
    }



    @Override
    public String obtenerRolDelUsuarioEnComunidad(Long idUsuario, Long idComunidad) {
        return repositorioUsuarioComunidad.obtenerRolDelUsuarioEnComunidad(idUsuario, idComunidad);
    }

    @Override
    public Boolean agregarUsuarioAComunidad(Usuario usuario, Comunidad comunidad, String rol) {
        return repositorioUsuarioComunidad.agregarUsuarioAComunidad(usuario, comunidad, rol);
    }

    @Override
    public UsuarioComunidad obtenerUsuarioEnComunidad(Long idUsuario, Long id) {
        return repositorioUsuarioComunidad.obtenerUsuarioEnComunidad(idUsuario, id);
    }

    @Override
    public Boolean eliminarUsuarioDeComunidad(Long idUsuario, Long idComunidad) {
        return repositorioUsuarioComunidad.eliminarUsuarioDeComunidad(idUsuario, idComunidad);
    }

    @Override
    public List<Comunidad> obtenerComunidadesDondeElUsuarioEsteUnido(Long idUsuario) {
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        return repositorioUsuarioComunidad.obtenerComunidadesDondeElUsuarioEsteUnido(usuario);
    }

    @Override
    public void compartirPosteoEnComunidad(Long idPost, List<Long> comunidades, Long idUsuario) {
        Post post = servicioPosteo.obtenerPosteoPorId(idPost);
        List<Comunidad> comunidads = new ArrayList<>();
        Usuario usuario = null;
        for(Long comunidad : comunidades){

            UsuarioComunidad usuarioComunidad = repositorioUsuarioComunidad.obtenerUsuarioEnComunidad(idUsuario,comunidad);

            comunidads.add(usuarioComunidad.getComunidad());
            usuario = usuarioComunidad.getUsuario();

        }

        repositorioUsuarioComunidad.compartirPosteoEnComunidad(post,comunidads,usuario);
    }

    @Override
    public Boolean agregarUsuarioAComunidadDePreescucha(Long id, Long idPreescucha, String rol) {

        Comunidad comunidad = servicioComunidad.obtenerComuniadDePreescucha(idPreescucha);
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(id);
        if (comunidad == null || usuario == null) {
            return false; // Comunidad o usuario no encontrado
        }
        return repositorioUsuarioComunidad.agregarUsuarioAComunidad(usuario, comunidad, rol);

    }
}
