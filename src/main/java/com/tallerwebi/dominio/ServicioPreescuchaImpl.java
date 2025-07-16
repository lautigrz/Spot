package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.CancionSimpleDto;
import com.tallerwebi.presentacion.dto.EstadoPreescucha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ServicioPreescuchaImpl implements ServicioPreescucha {
    private final RepositorioPreescucha repositorioPreescucha;
    private SpotifyApi spotifyApi;

    private final Map<Long, EstadoPreescucha> estadosPreescucha = new ConcurrentHashMap<>();


    @Autowired
    public ServicioPreescuchaImpl(RepositorioPreescucha repositorioPreescucha, SpotifyApi spotifyApi) {
        this.repositorioPreescucha = repositorioPreescucha;
        this.spotifyApi = spotifyApi;

    }
    public EstadoPreescucha obtenerEstado(Long idComunidad) {
        return estadosPreescucha.get(idComunidad);
    }

    @Override
    public void actualizarEstado(Long idComunidad, int nuevoIndice, int nuevoSegundos) {
        EstadoPreescucha estado = obtenerEstado(idComunidad);
        if (estado != null) {
            estado.setIndiceActual(nuevoIndice);
            estado.setTimestampInicio(System.currentTimeMillis() - (nuevoSegundos * 1000L));
            System.out.println("Estado actualizado para comunidad no es null " + idComunidad + ": Indice actual: " + nuevoIndice + ", Timestamp inicio: " + estado.getTimestampInicio());
        }
        System.out.println("Estado actualizado para comunidad " + idComunidad + ": Indice actual: " + nuevoIndice + ", Timestamp inicio: " + estado.getTimestampInicio());
    }

    public void iniciarPreescucha(Long idComunidad, List<CancionSimpleDto> canciones) {
        EstadoPreescucha estado = new EstadoPreescucha();
        estado.setCanciones(canciones);
        estado.setIndiceActual(0);
        estado.setTimestampInicio(System.currentTimeMillis());
        estado.setReproduciendo(true);
        estadosPreescucha.put(idComunidad, estado);
    }


    @Override
    public boolean yaComproPreescucha(String albumId, Usuario usuario) {
        return repositorioPreescucha.existeCompra(albumId, usuario.getId());
    }

    @Override
    public void comprarPreescucha(String albumId, Usuario usuario) {
        if(!repositorioPreescucha.existeCompra(albumId, usuario.getId())){

        }

    }

    @Override
    public List<String> obtenerAlbumesComprados(Usuario usuario) {
        return repositorioPreescucha.obtenerComprasPorUsuario(usuario.getId())
                .stream()
                .map(Preescucha::getSpotifyAlbumId)
                .collect(Collectors.toList());
    }

    @Override
    public Long crearPreescuchaLocal(Preescucha preescucha) {

        repositorioPreescucha.guardar(preescucha);
        return preescucha.getId();
    }

    @Override
    public void comprarPreescuchaLocal(int preescuchaId, Usuario usuario) {


    }

    @Override
    public boolean yaComproPreescuchaLocal(int preescuchaId, Usuario usuario) {
        return repositorioPreescucha.existeCompraLocal(preescuchaId, usuario.getId());
    }

    public List<Preescucha> obtenerPreescuchasCompradasLocalmente(Usuario usuario){
        return repositorioPreescucha.obtenerPreescuchasLocalesCompradasPorUsuario(usuario.getId());
    }

    @Override
    public Preescucha obtenerPreescuchaLocal(Long id) {
        return repositorioPreescucha.buscarPreescuchaPorId(id);
    }

    @Override
    public List<Preescucha> obtenerPreescuchasPorArtista(Long idArtista) {
        return repositorioPreescucha.obtenerPreescuchasPorArtista(idArtista);
    }
}