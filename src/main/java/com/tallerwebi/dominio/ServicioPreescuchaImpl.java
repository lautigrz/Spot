package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;

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
}
