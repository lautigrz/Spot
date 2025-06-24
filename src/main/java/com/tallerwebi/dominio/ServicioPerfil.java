package com.tallerwebi.dominio;

import se.michaelthelin.spotify.model_objects.specification.*;

import java.util.List;

public interface ServicioPerfil {
    User obtenerPerfilUsuario(String token) throws Exception;
    Integer obtenerCantidadDeArtistaQueSigueElUsuario(String token) throws Exception;
    List<Artist> obtenerMejoresArtistasDelUsuario(String token) throws Exception;
    List<PlaylistSimplified> obtenerNombreDePlaylistDelUsuario(String token) throws Exception;
    Integer obtenerCantidadDePlaylistDelUsuario(String token) throws Exception;
    Track obtenerReproduccionActualDelUsuario(String token) throws Exception;
    EstadoDeAnimo obtenerEstadoDeAnimoDelUsuario(String token) throws Exception;
    void actualizarEstadoDeAnimoUsuario(String token, EstadoDeAnimo estadoDeAnimo) throws Exception;
    List<Track> obtenerTopTracksDeLUsuario(String token) throws Exception;
    List<Album> obtenerAlbumesDePreescuchaCompradosPorElUsuario(List<String> albumIds, String token) throws Exception;
}
