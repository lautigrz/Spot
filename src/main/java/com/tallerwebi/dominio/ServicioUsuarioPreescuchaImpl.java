package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.ComunidadPreescuchaDto;
import com.tallerwebi.presentacion.dto.UsuarioPreescuchaDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<UsuarioPreescuchaDto> buscarPorUsuario(Long id) {
        List<UsuarioPreescucha> usuarioPreescuchas = repositorioUsuarioPreescucha.buscarPorUsuario(id);
        if (usuarioPreescuchas.isEmpty()) {
            return List.of();
        }
        return usuarioPreescuchas.stream()
                .map(up -> new UsuarioPreescuchaDto(
                        up.getPreescucha().getId(),
                        up.getPreescucha().getTitulo(),
                        up.getPreescucha().getArtista().getNombre(),
                        up.getPreescucha().getPreescuchaFotoUrl(),
                        up.getFechaFormateada(),
                        up.getPreescucha().getPrecio(),
                        up.getPreescucha().getFechaFormateada()
                ))
                .collect(Collectors.toList());

    }

    @Override
    public List<UsuarioPreescucha> buscarPorPreescucha(Long id) {
        return List.of();
    }
}
