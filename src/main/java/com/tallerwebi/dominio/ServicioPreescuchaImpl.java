package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicioPreescuchaImpl implements ServicioPreescucha {
    private final RepositorioPreescucha repositorioPreescucha;
    private SpotifyApi spotifyApi;


    @Autowired
    public ServicioPreescuchaImpl(RepositorioPreescucha repositorioPreescucha, SpotifyApi spotifyApi) {
        this.repositorioPreescucha = repositorioPreescucha;
        this.spotifyApi = spotifyApi;

    }

    @Override
    public boolean yaComproPreescucha(String albumId, Usuario usuario) {
        return repositorioPreescucha.existeCompra(albumId, usuario.getId());
    }

    @Override
    public void comprarPreescucha(String albumId, Usuario usuario) {
        if(!repositorioPreescucha.existeCompra(albumId, usuario.getId())){
            Preescucha preescucha = new Preescucha();
            preescucha.setSpotifyAlbumId(albumId);
            preescucha.setUsuario(usuario);
            preescucha.setFechaCompra(LocalDateTime.now());
            repositorioPreescucha.guardar(preescucha);
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
    public void crearPreescuchaLocal(Double precio, String titulo, String preescuchaFotoUrl, String rutaAudio, Artista artista){
        Preescucha nuevaPreescucha = new Preescucha();
        nuevaPreescucha.setPrecio(precio);
        nuevaPreescucha.setTitulo(titulo);
        nuevaPreescucha.setPreescuchaFotoUrl(preescuchaFotoUrl);
        nuevaPreescucha.setRutaAudio(rutaAudio);
        nuevaPreescucha.setArtista(artista);
        repositorioPreescucha.guardar(nuevaPreescucha);
    }

    @Override
    public void comprarPreescuchaLocal(int preescuchaId, Usuario usuario) {
        if (!repositorioPreescucha.existeCompraLocal(preescuchaId, usuario.getId())) {
            Preescucha original = repositorioPreescucha.buscarPreescuchaPorId(preescuchaId);
            if (original != null && usuario != null) {
                Preescucha copia = new Preescucha();
                copia.setPrecio(original.getPrecio());
                copia.setTitulo(original.getTitulo());
                copia.setPreescuchaFotoUrl(original.getPreescuchaFotoUrl());
                copia.setRutaAudio(original.getRutaAudio());
                copia.setArtista(original.getArtista());
                copia.setUsuario(usuario);
                copia.setFechaCompra(LocalDateTime.now());
                repositorioPreescucha.guardar(copia);
            }
        }
    }

    @Override
    public boolean yaComproPreescuchaLocal(int preescuchaId, Usuario usuario) {
        return repositorioPreescucha.existeCompraLocal(preescuchaId, usuario.getId());
    }

    public List<Preescucha> obtenerPreescuchasCompradasLocalmente(Usuario usuario){
        return repositorioPreescucha.obtenerPreescuchasLocalesCompradasPorUsuario(usuario.getId());
    }
}