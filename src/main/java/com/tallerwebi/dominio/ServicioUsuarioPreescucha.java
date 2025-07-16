package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.ComunidadPreescuchaDto;
import com.tallerwebi.presentacion.dto.UsuarioPreescuchaDto;

import java.util.List;

public interface ServicioUsuarioPreescucha {
    UsuarioPreescucha guardar(Long idUsuario, Long idPreescucha);
    Boolean comprobarSiYaCompro(Long idUsuario, Long idPreescucha);
    List<UsuarioPreescuchaDto> buscarPorUsuario(Long id);
    List<UsuarioPreescuchaDto> buscarPorUsuarioOrdenado(Long idUsuario, String orden);
}
