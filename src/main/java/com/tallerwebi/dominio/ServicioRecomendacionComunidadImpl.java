package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.CancionDto;
import com.tallerwebi.presentacion.dto.RecomendacionDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServicioRecomendacionComunidadImpl implements ServicioRecomedacionComunidad{

    private RepositorioRecomendacion repositorioRecomendacion;
    private RepositorioUsuarioComunidad repositorioUsuarioComunidad;
    private RepositorioCancion repositorioCancion;
    public ServicioRecomendacionComunidadImpl(RepositorioRecomendacion repositorioRecomendacion, RepositorioUsuarioComunidad repositorioUsuarioComunidad
    , RepositorioCancion repositorioCancion) {
        this.repositorioRecomendacion = repositorioRecomendacion;
        this.repositorioUsuarioComunidad = repositorioUsuarioComunidad;
        this.repositorioCancion = repositorioCancion;
    }

    @Override
    public void agregarRecomendacion(List<CancionDto> cancionDtos, Long idUsuario, Long idComunidad) {
        
        UsuarioComunidad usuarioComunidad = this.repositorioUsuarioComunidad.obtenerUsuarioEnComunidad(idUsuario, idComunidad);
        if (usuarioComunidad == null) {
            throw new IllegalArgumentException("El usuario no pertenece a la comunidad");
        }
        
        
        for(CancionDto cancionDto : cancionDtos) {
            Recomendacion recomendacion = getRecomendacion(cancionDto, usuarioComunidad);
            this.repositorioRecomendacion.agregarRecomendacion(recomendacion);
        }
        
    }

    @Override
    public void eliminarRecomendacion(Long idRecomendacion) {
        this.repositorioRecomendacion.eliminarRecomendacion(idRecomendacion);
    }

    @Override
    public List<Recomendacion> obtenerRecomendacionesPorComunidad(Long idComunidad) {
        return this.repositorioRecomendacion.obtenerRecomendacionesPorComunidad(idComunidad);
    }

    @Override
    public Recomendacion aceptarRecomendacion(Long idRecomendacion) {
        return this.repositorioRecomendacion.aceptarRecomendacion(idRecomendacion);
    }
    private Recomendacion getRecomendacion(CancionDto cancionDto, UsuarioComunidad usuarioComunidad) {

        Cancion cancion = this.repositorioCancion.buscarCancionPorElIdDeSpotify(cancionDto.getSpotifyId());

        if (cancion == null) {
            cancion = new Cancion();
            cancion.setSpotifyId(cancionDto.getSpotifyId());
            cancion.setUri(cancionDto.getUri());
            cancion.setTitulo(cancionDto.getTitulo());
            cancion.setArtista(cancionDto.getArtista());
            cancion.setUrlImagen(cancionDto.getUrlImagen());

            repositorioCancion.guardarCancion(cancion);
        }

        Recomendacion recomendacion = new Recomendacion();
        recomendacion.setCancion(cancion);
        recomendacion.setEstado(false);
        recomendacion.setUsuario(usuarioComunidad.getUsuario());
        recomendacion.setComunidad(usuarioComunidad.getComunidad());
        return recomendacion;
    }
}
