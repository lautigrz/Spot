package com.tallerwebi.dominio;

import java.util.List;
import java.util.Set;

public interface RepositorioComunidad {
    Long guardarMensajeDeLaComunidad(String mensaje, Comunidad comunidad, Usuario usuario);
    List<Mensaje> obtenerMensajesDeComunidad(Long id);
    List<Comunidad> obtenerComunidades();
    Comunidad obtenerComunidad(Long id);
    String obtenerTokenDelUsuarioQuePerteneceAUnaComunidad(String user, Long idComunidad);
    Comunidad  obtenerComunidadConUsuarios(Long idComunidad);
    List<Playlist> obtenerPlaylistsPorComunidadId(Long comunidadId);
    Set<Cancion> obtenerCancionesDeUnaPlaylistDeUnaComunidad(Long idComunidad);
    Playlist obtenerPlaylistDeUnaComunidad(Long idComunidad);
    List<Usuario> obtenerUsuariosPorComunidad(Long idComunidad);
    List<Comunidad> buscarComunidadesPorNombre(String nombreComunidad);
    Long crearComunidadParaUnaPreescucha(Preescucha preescucha);
    Boolean obtenerComunidadDeArtista(Long idComunidad,Long idArtista);
    Comunidad obtenerComuniadDePreescucha(Long idPreescucha);
}
