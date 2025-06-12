package com.tallerwebi.dominio;

public interface RepositorioUsuario {


    Usuario buscar(String user);
    Usuario buscarUsuarioPorId(Long id);
    Usuario buscarUsuarioPorSpotifyID(String spotifyID);
    void actualizarUsuario(Usuario usuario);
}


