package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.UsuarioDto;

import java.util.List;
import java.util.Set;

public interface RepositorioComunidad {
    void guardarMensajeDeLaComunidad(String mensaje, Long idUsuario, Long idComunidad);
    List<Mensaje> obtenerMensajesDeComunidad(Long id);
    List<Comunidad> obtenerComunidades();
    Comunidad obtenerComunidad(Long id);
    String obtenerTokenDelUsuarioQuePerteneceAUnaComunidad(String user, Long idComunidad);
    Boolean guardarUsuarioEnComunidad(Usuario usuario,Long idComunidad);
    Usuario obtenerUsuarioEnComunidad(Long idUsuario, Long idComunidad);
    Comunidad  obtenerComunidadConUsuarios(Long idComunidad);
    List<Playlist> obtenerPlaylistsPorComunidadId(Long comunidadId);
    Set<Cancion> obtenerCancionesDeUnaPlaylistDeUnaComunidad(Long idComunidad);
    Playlist obtenerPlaylistDeUnaComunidad(Long idComunidad);
    List<Usuario> obtenerUsuariosPorComunidad(Long idComunidad);

}
