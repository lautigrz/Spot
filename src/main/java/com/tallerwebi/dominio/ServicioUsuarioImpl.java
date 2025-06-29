package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.UsuarioDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServicioUsuarioImpl implements ServicioUsuario {
    @Autowired
    private final RepositorioUsuario repositorioUsuario;

    public ServicioUsuarioImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public UsuarioDto obtenerUsuarioDtoPorId(Long idUsuario) {
        Usuario usuario = repositorioUsuario.buscarUsuarioPorId(idUsuario);
        if (usuario == null) {
            return null; // O lanzar una excepción si el usuario no se encuentra
        }

        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setId(usuario.getId());
        usuarioDto.setUser(usuario.getUser());
        usuarioDto.setUrlFoto(usuario.getUrlFoto());
        return usuarioDto;
    }

    @Override
    public Usuario obtenerUsuarioPorId(Long idUsuario) {
    Usuario usuario = repositorioUsuario.buscarUsuarioPorId(idUsuario);
        if (usuario == null) {
            return null;
        }

        return usuario;

    }

    @Override
    public void seguirUsuario(Long seguidorId, Long aSeguirId) throws Exception{
        if (seguidorId.equals(aSeguirId)) {
            throw new Exception("No se puede seguir a uno mismo");
        }

        Usuario seguidor = repositorioUsuario.buscarUsuarioPorId(seguidorId);
        Usuario aSeguir = repositorioUsuario.buscarUsuarioPorId(aSeguirId);

        if (seguidor == null || aSeguir == null) {
            throw new Exception("Usuario no encontrado");
        }

        seguidor.getSeguidos().add(aSeguir);
        aSeguir.getSeguidores().add(seguidor);

        repositorioUsuario.actualizarUsuario(aSeguir);
        repositorioUsuario.actualizarUsuario(seguidor);
    }

    @Override
    public void dejarDeSeguirUsuario(Long seguidorId, Long aDejarId) throws Exception{
        if (seguidorId.equals(aDejarId)) {
            throw new Exception("No se puede dejar de seguir a uno mismo");
        }

        Usuario seguidor = repositorioUsuario.buscarUsuarioPorId(seguidorId);
        Usuario aDejar = repositorioUsuario.buscarUsuarioPorId(aDejarId);

        if (seguidor == null || aDejar == null) {
            throw new Exception("Usuario no encontrado");
        }

        seguidor.getSeguidos().remove(aDejar);
        aDejar.getSeguidores().remove(seguidor);
    }

    @Override
    public Set<UsuarioDto> obtenerSeguidos(Long usuarioId){
        Usuario usuario = repositorioUsuario.buscarUsuarioPorId(usuarioId);
        if (usuario == null) return Collections.emptySet();

        return usuario.getSeguidos().stream()
                .map(this::mapToDto)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<UsuarioDto> obtenerSeguidores(Long usuarioId){
        Usuario usuario = repositorioUsuario.buscarUsuarioPorId(usuarioId);
        if (usuario == null) return Collections.emptySet();
        return usuario.getSeguidores().stream()
                .map(this::mapToDto)
                .collect(Collectors.toSet());
    }

    private UsuarioDto mapToDto(Usuario usuario){
        UsuarioDto dto = new UsuarioDto();
        dto.setId(usuario.getId());
        dto.setUser(usuario.getUser());
        dto.setUrlFoto(usuario.getUrlFoto());
        return dto;
    }

    @Override
    public boolean yaSigo(Long seguidorId, Long seguidoId) throws Exception {
        Usuario seguidor = repositorioUsuario.buscarUsuarioPorId(seguidorId);
        Usuario seguido = repositorioUsuario.buscarUsuarioPorId(seguidoId);

        if (seguidor == null || seguido == null) {
            throw new Exception("Usuario no encontrado");
        }

        return seguidor.getSeguidos().contains(seguido);
    }

}
