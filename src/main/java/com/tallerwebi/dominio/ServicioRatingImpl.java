package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServicioRatingImpl implements ServicioRating {

    private RepositorioRating repositoriorRating;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioCancion repositorioCancion;

    @Autowired
    public ServicioRatingImpl(RepositorioRating repositoriorRating, RepositorioUsuario repositorioUsuario, RepositorioCancion repositorioCancion){
        this.repositoriorRating = repositoriorRating;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioCancion = repositorioCancion;
    }

    @Override
    public void guardarRating(Long usuarioId, String spotifyId, Integer puntaje) throws Exception {
        if (puntaje < 1 || puntaje > 5) {
            throw new Exception("El puntaje debe ser entre 1 y 5.");
        }

        Usuario usuario = repositorioUsuario.buscarUsuarioPorId(usuarioId);
        if (usuario == null) {
            throw new Exception("El usuario no existe");
        }

        Cancion cancion = repositorioCancion.buscarCancionPorElIdDeSpotify(spotifyId);
        if (cancion == null) {
            throw new Exception("Canción no encontrada.");
        }

        Rating rating = new Rating();
        rating.setUsuario(usuario);
        rating.setCancion(cancion);
        rating.setPuntaje(puntaje);
        repositoriorRating.guardarRating(rating);
    }

    @Override
    public List<Rating> obtenerRating(Long usuarioId){
        Usuario usuario = repositorioUsuario.buscarUsuarioPorId(usuarioId);
        return repositoriorRating.obtenerRating(usuario);
    }




}
