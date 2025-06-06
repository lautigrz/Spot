package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.UsuarioDto;

import java.util.List;

public interface RepositorioComunidad {
    void guardarMensajeDeLaComunidad(String mensaje, Long idUsuario, Long idComunidad);
    List<Mensaje> obtenerMensajesDeComunidad(Long id);
    void guardarNuevaComunidad(Comunidad comunidad);
    List<Comunidad> obtenerComunidades();
    Comunidad obtenerComunidad(Long id);
    String obtenerTokenDelUsuarioQuePerteneceAUnaComunidad(String user, Long idComunidad);
    Boolean guardarUsuarioEnComunidad(Usuario usuario,Long idComunidad);
    Usuario obtenerUsuarioEnComunidad(Long idUsuario, Long idComunidad);
    Comunidad  obtenerComunidadConUsuarios(Long idComunidad);
}
