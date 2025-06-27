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
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import se.michaelthelin.spotify.requests.data.player.GetInformationAboutUsersCurrentPlaybackRequest;
import se.michaelthelin.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
@Transactional
public class ServicioPerfilImpl implements ServicioPerfil {

    private ServicioInstancia spotify;
    private final RepositorioUsuario repositorioUsuarioImpl;

    @Autowired
    public ServicioPerfilImpl(ServicioInstancia spotify, RepositorioUsuario repositorioUsuarioImpl) {
        this.spotify = spotify;
        this.repositorioUsuarioImpl = repositorioUsuarioImpl;
    }

    private static final ModelObjectType type = ModelObjectType.ARTIST;

    @Override
    public User obtenerPerfilUsuario(String token) throws Exception {

        SpotifyApi spotifyApi = spotify.obtenerInstanciaDeSpotifyConToken(token);
        User user = spotifyApi.getCurrentUsersProfile().build().execute();
        return user;
    }

    @Override
    public Integer obtenerCantidadDeArtistaQueSigueElUsuario(String token) throws Exception {
        SpotifyApi spotifyApi = spotify.obtenerInstanciaDeSpotifyConToken(token);
        GetUsersFollowedArtistsRequest followed = spotifyApi.getUsersFollowedArtists(type).build();

        PagingCursorbased<Artist> artists = followed.execute();

        return artists.getTotal();
    }

    @Override
    public List<Artist> obtenerMejoresArtistasDelUsuario(String token) throws Exception {
        SpotifyApi spotifyApi = spotify.obtenerInstanciaDeSpotifyConToken(token);
        GetUsersTopArtistsRequest getUsersTopArtistsRequest = spotifyApi.getUsersTopArtists().limit(10).build();
        Paging<Artist> artists = getUsersTopArtistsRequest.execute();

        List<Artist> mejores = new ArrayList<>();
        
        mejores.addAll(Arrays.asList(artists.getItems()));

        return mejores;
    }

    @Override
    public List<Track> obtenerTopTracksDeLUsuario(String token) throws Exception{
        SpotifyApi spotifyApi = spotify.obtenerInstanciaDeSpotifyConToken(token);
        GetUsersTopTracksRequest getUsersTopTracksRequest = spotifyApi.getUsersTopTracks().limit(10).build();
        Paging <Track> topTracks = getUsersTopTracksRequest.execute();
        List<Track> mejores = new ArrayList<>();
        mejores.addAll(Arrays.asList(topTracks.getItems()));
        return mejores;
    }



    @Override
    public List<PlaylistSimplified> obtenerNombreDePlaylistDelUsuario(String token) throws Exception {

        SpotifyApi spotifyApi = spotify.obtenerInstanciaDeSpotifyConToken(token);

        GetListOfCurrentUsersPlaylistsRequest playlist = spotifyApi.getListOfCurrentUsersPlaylists().build();

        Paging<PlaylistSimplified> playlists = playlist.execute();

        List<PlaylistSimplified> playlistsUsuario = new ArrayList<>();

        playlistsUsuario.addAll(Arrays.asList(playlists.getItems()));

        return playlistsUsuario;

    }

    @Override
    public Integer obtenerCantidadDePlaylistDelUsuario(String token) throws Exception {
        SpotifyApi spotifyApi = spotify.obtenerInstanciaDeSpotifyConToken(token);

        GetListOfCurrentUsersPlaylistsRequest playlist = spotifyApi.getListOfCurrentUsersPlaylists().build();

        Paging<PlaylistSimplified> playlists = playlist.execute();

        return playlists.getTotal();
    }

    @Override
    public Track obtenerReproduccionActualDelUsuario(String token) throws Exception {
        Track track = null;
        SpotifyApi spotifyApi = spotify.obtenerInstanciaDeSpotifyConToken(token);

        GetUsersCurrentlyPlayingTrackRequest currentlyPlayingTrackRequest = spotifyApi.getUsersCurrentlyPlayingTrack().build();

        CurrentlyPlaying currentlyPlaying = currentlyPlayingTrackRequest.execute();

        if(currentlyPlaying != null && currentlyPlaying.getItem() instanceof Track) {
            track = (Track) currentlyPlaying.getItem();

        }

        return track;
    }

    @Override
    public EstadoDeAnimo obtenerEstadoDeAnimoDelUsuario(String token) throws Exception {
    SpotifyApi spotifyApi = spotify.obtenerInstanciaDeSpotifyConToken(token);
    User user = spotifyApi.getCurrentUsersProfile().build().execute();
    Usuario usuario = repositorioUsuarioImpl.buscarUsuarioPorSpotifyID(user.getId());

    EstadoDeAnimo estado = usuario.getEstadoDeAnimo();
    System.out.println("Estado de ánimo actual del usuario: " + (estado != null ? estado.getNombre() : "null"));

    if(estado == null || estado.getId() == null) {
        return null;
    }
        return estado;
    }

    @Override
    public void actualizarEstadoDeAnimoUsuario(String token, EstadoDeAnimo estadoDeAnimo) throws Exception{
        SpotifyApi spotifyApi = spotify.obtenerInstanciaDeSpotifyConToken(token);
        User user = spotifyApi.getCurrentUsersProfile().build().execute();
        System.out.println("ID del usuario Spotify: " + user.getId());
        Usuario usuario = repositorioUsuarioImpl.buscarUsuarioPorSpotifyID(user.getId());
        System.out.println("Usuario encontrado en base: " + (usuario != null ? usuario.getId() : "null"));
        usuario.setEstadoDeAnimo(estadoDeAnimo);
        System.out.println("Estado de ánimo seteado a: " + estadoDeAnimo.getNombre());
        repositorioUsuarioImpl.actualizarUsuario(usuario);
        System.out.println("Usuario actualizado");
    }

    @Override
    public List<Album> obtenerAlbumesDePreescuchaCompradosPorElUsuario(List<String> albumIds, String token) throws Exception {
        SpotifyApi spotifyApi = spotify.obtenerInstanciaDeSpotifyConToken(token);
        List<Album> albums = new ArrayList<>();

        for (String id : albumIds) {
            try{
                Album album = spotifyApi.getAlbum(id).build().execute();
                albums.add(album);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return albums;

    }
}
