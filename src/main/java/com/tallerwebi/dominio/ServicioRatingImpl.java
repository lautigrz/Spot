package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.User;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class ServicioRatingImpl implements ServicioRating {

    private RepositorioRating repositoriorRating;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioCancion repositorioCancion;
    private ServicioInstancia spotify;

    @Autowired
    public ServicioRatingImpl(RepositorioRating repositoriorRating, RepositorioUsuario repositorioUsuario, RepositorioCancion repositorioCancion, ServicioInstancia spotify) {
        this.repositoriorRating = repositoriorRating;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioCancion = repositorioCancion;
        this.spotify = spotify;
    }

    @Override
    public void guardarRating(String token, String spotifyId, String titulo, String artista, String urlImagen, String uri, Integer puntaje) throws Exception {
        System.out.println("SERVICIO RATING: Guardando rating con token: " + token + ", spotifyId: " + spotifyId + ", puntaje: " + puntaje);

        if (puntaje < 1 || puntaje > 5) {
            throw new Exception("El puntaje debe ser entre 1 y 5");
        }

        // Obtener perfil de Spotify con token
        SpotifyApi spotifyApi = spotify.obtenerInstanciaDeSpotifyConToken(token);
        User user = spotifyApi.getCurrentUsersProfile().build().execute();
        System.out.println("ID del usuario Spotify desde API: " + user.getId());

        // Buscar usuario interno por spotifyID
        Usuario usuario = repositorioUsuario.buscarUsuarioPorSpotifyID(user.getId());
        System.out.println("Usuario encontrado en base: " + (usuario != null ? usuario.getId() : "null"));
        if (usuario == null) {
            throw new Exception("Usuario no encontrado");
        }

        // Buscar o crear la canción
        Cancion cancion = repositorioCancion.buscarCancionPorElIdDeSpotify(spotifyId);
        if (cancion == null) {
            cancion = new Cancion();
            cancion.setSpotifyId(spotifyId);
            cancion.setTitulo(titulo);
            cancion.setArtista(artista);
            cancion.setUrlImagen(urlImagen);
            cancion.setUri(uri);
            repositorioCancion.guardarCancion(cancion);
        }

        // Buscar rating existente o crear uno nuevo
        Rating ratingExistente = repositoriorRating.buscarPorUsuarioYCancion(usuario, cancion);

        if (ratingExistente != null) {
            ratingExistente.setPuntaje(puntaje);
            System.out.println("SERVICIO RATING: Rating actualizado correctamente.");
        } else {
            ratingExistente = new Rating(usuario, cancion, puntaje);
            System.out.println("SERVICIO RATING: Rating creado correctamente.");
        }

        // Guardar rating
        repositoriorRating.guardarRating(ratingExistente);
    }

    @Override
    public List<Rating> obtenerRating(String spotifyId){
        Usuario usuario = repositorioUsuario.buscarUsuarioPorSpotifyID(spotifyId);
        if (usuario == null) {
            return Collections.emptyList();
        } else
            return repositoriorRating.obtenerRating(usuario);
    }



}
