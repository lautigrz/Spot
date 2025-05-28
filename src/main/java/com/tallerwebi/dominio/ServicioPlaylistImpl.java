package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioPlaylistImpl implements ServicioPlaylist {

    private RepositorioPlaylist repositorioPlaylist;

    @Autowired
    public ServicioPlaylistImpl(RepositorioPlaylist repositorioPlaylist) {
        this.repositorioPlaylist = repositorioPlaylist;
    }

    @Override
    public void agregarCancionALaPlaylist(Long idPlaylist,Cancion cancion) {


        repositorioPlaylist.agregarCancionALaPlaylist(idPlaylist, cancion);
    }

    @Override
    public void eliminarCancionALaPlaylist(Long id, Cancion cancion) {

    }

    @Override
    public List<Cancion> obtenerCancionesDeLaPlaylist(Long id) {
        return repositorioPlaylist.obtenerCancionesDeLaPlaylist(id);
    }
}
