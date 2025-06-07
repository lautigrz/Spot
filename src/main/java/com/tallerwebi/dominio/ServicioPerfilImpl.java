package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.data.follow.GetUsersFollowedArtistsRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import se.michaelthelin.spotify.requests.data.player.GetInformationAboutUsersCurrentPlaybackRequest;
import se.michaelthelin.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Transactional
@Service
public class ServicioPerfilImpl implements ServicioPerfil {

    private final RepositorioUsuarioImpl repositorioUsuarioImpl;
    private ServicioSpotify spotify;

    @Autowired
    public ServicioPerfilImpl(ServicioSpotify spotify, RepositorioUsuarioImpl repositorioUsuarioImpl) {
        this.spotify = spotify;
        this.repositorioUsuarioImpl = repositorioUsuarioImpl;
    }

    private static final ModelObjectType type = ModelObjectType.ARTIST;

    @Override
    public User obtenerPerfilUsuario(String token, String refreshToken) throws Exception {

        SpotifyApi spotifyApi = spotify.obtenerInstanciaDeSpotifyConToken(token, refreshToken);
        User user = spotifyApi.getCurrentUsersProfile().build().execute();
        return user;
    }

    @Override
    public Integer obtenerCantidadDeArtistaQueSigueElUsuario(String token, String refreshToken) throws Exception {
        SpotifyApi spotifyApi = spotify.obtenerInstanciaDeSpotifyConToken(token, refreshToken);
        GetUsersFollowedArtistsRequest followed = spotifyApi.getUsersFollowedArtists(type).build();

        PagingCursorbased<Artist> artists = followed.execute();

        return artists.getTotal();
    }

    @Override
    public List<Artist> obtenerMejoresArtistasDelUsuario(String token, String refreshToken) throws Exception {
        SpotifyApi spotifyApi = spotify.obtenerInstanciaDeSpotifyConToken(token, refreshToken);
        GetUsersTopArtistsRequest getUsersTopArtistsRequest = spotifyApi.getUsersTopArtists().limit(10).build();
        Paging<Artist> artists = getUsersTopArtistsRequest.execute();

        List<Artist> mejores = new ArrayList<>();
        
        mejores.addAll(Arrays.asList(artists.getItems()));

        return mejores;
    }

    @Override
    public List<PlaylistSimplified> obtenerNombreDePlaylistDelUsuario(String token, String refreshToken) throws Exception {

        SpotifyApi spotifyApi = spotify.obtenerInstanciaDeSpotifyConToken(token, refreshToken);

        GetListOfCurrentUsersPlaylistsRequest playlist = spotifyApi.getListOfCurrentUsersPlaylists().build();

        Paging<PlaylistSimplified> playlists = playlist.execute();

        List<PlaylistSimplified> playlistsUsuario = new ArrayList<>();

        playlistsUsuario.addAll(Arrays.asList(playlists.getItems()));

        return playlistsUsuario;

    }

    @Override
    public Integer obtenerCantidadDePlaylistDelUsuario(String token, String refreshToken) throws Exception {
        SpotifyApi spotifyApi = spotify.obtenerInstanciaDeSpotifyConToken(token, refreshToken);

        GetListOfCurrentUsersPlaylistsRequest playlist = spotifyApi.getListOfCurrentUsersPlaylists().build();

        Paging<PlaylistSimplified> playlists = playlist.execute();

        return playlists.getTotal();
    }

    @Override
    public Track obtenerReproduccionActualDelUsuario(String token, String refreshToken) throws Exception {
        Track track = null;
        SpotifyApi spotifyApi = spotify.obtenerInstanciaDeSpotifyConToken(token, refreshToken);

        GetUsersCurrentlyPlayingTrackRequest currentlyPlayingTrackRequest = spotifyApi.getUsersCurrentlyPlayingTrack().build();

        CurrentlyPlaying currentlyPlaying = currentlyPlayingTrackRequest.execute();

        if(currentlyPlaying != null && currentlyPlaying.getItem() instanceof Track) {
            track = (Track) currentlyPlaying.getItem();

        }

        return track;
    }

    @Override
    public EstadoDeAnimo obtenerEstadoDeAnimoDelUsuario(String token, String refreshToken) throws Exception{
    SpotifyApi spotifyApi = spotify.obtenerInstanciaDeSpotifyConToken(token, refreshToken);
    User user = spotifyApi.getCurrentUsersProfile().build().execute();
    Usuario usuario = repositorioUsuarioImpl.buscarUsuarioPorSpotifyID(user.getId());
    return usuario.getEstadoDeAnimo();
    }

    @Override
    public void actualizarEstadoDeAnimoUsuario(String token, String refreshToken, EstadoDeAnimo estadoDeAnimo) throws Exception{
        SpotifyApi spotifyApi = spotify.obtenerInstanciaDeSpotifyConToken(token, refreshToken);
        User user = spotifyApi.getCurrentUsersProfile().build().execute();
        Usuario usuario = repositorioUsuarioImpl.buscarUsuarioPorSpotifyID(user.getId());
        usuario.setEstadoDeAnimo(estadoDeAnimo);
        repositorioUsuarioImpl.actualizarUsuario(usuario);
    }
}
