package com.tallerwebi.dominio;

public interface RepositorioUsuario {

    Usuario buscarUsuario(String email, String password);
    Usuario buscar(String user);
    void modificar(Usuario usuario);
}

