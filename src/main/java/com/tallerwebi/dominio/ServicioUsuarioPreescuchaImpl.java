package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServicioUsuarioPreescuchaImpl implements ServicioUsuarioPreescucha {
    private RepositorioUsuarioPreescucha repositorioUsuarioPreescucha;
    private ServicioUsuario servicioUsuario;
    private ServicioPreescucha servicioPreescucha;

    public ServicioUsuarioPreescuchaImpl(RepositorioUsuarioPreescucha repositorioUsuarioPreescucha, ServicioUsuario servicioUsuario, ServicioPreescucha servicioPreescucha) {
        this.repositorioUsuarioPreescucha = repositorioUsuarioPreescucha;
        this.servicioUsuario = servicioUsuario;
        this.servicioPreescucha = servicioPreescucha;
    }

    @Override
    public UsuarioPreescucha guardar(Long idUsuario, Long idPreescucha) {

        Boolean yaLaCompro = repositorioUsuarioPreescucha.existePorUsuarioYPreescucha(idUsuario, idPreescucha);
        if (yaLaCompro) {
            throw new RuntimeException("El usuario ya compró esta preescucha");
        }
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        Preescucha preescucha = servicioPreescucha.obtenerPreescuchaLocal(idPreescucha);

        return repositorioUsuarioPreescucha.guardar(usuario, preescucha);
    }

    @Override
    public UsuarioPreescucha buscarPorId(Long id) {
        return null;
    }

    @Override
    public void eliminar(Long id) {

    }

    @Override
    public Boolean comprobarSiYaCompro(Long idUsuario, Long idPreescucha) {
        return repositorioUsuarioPreescucha.existePorUsuarioYPreescucha(idUsuario, idPreescucha);
    }

    @Override
    public List<UsuarioPreescucha> buscarPorUsuario(Long id) {
        return repositorioUsuarioPreescucha.buscarPorUsuario(id);
    }

    @Override
    public List<UsuarioPreescucha> buscarPorPreescucha(Long id) {
        return List.of();
    }
}
