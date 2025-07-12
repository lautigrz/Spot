package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioUsuarioPreescucha {
    UsuarioPreescucha guardar(Long idUsuario, Long idPreescucha);

    UsuarioPreescucha buscarPorId(Long id);

    void eliminar(Long id);
    Boolean comprobarSiYaCompro(Long idUsuario, Long idPreescucha);
    List<UsuarioPreescucha> buscarPorUsuario(Long id);

    List<UsuarioPreescucha> buscarPorPreescucha(Long id);

}
