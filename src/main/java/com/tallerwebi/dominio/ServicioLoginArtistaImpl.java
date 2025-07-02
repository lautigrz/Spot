package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServicioLoginArtistaImpl implements ServicioLoginArtista {

    private final RepositorioArtista repositorioArtista;

    public ServicioLoginArtistaImpl(RepositorioArtista repositorioArtista) {
        this.repositorioArtista = repositorioArtista;
    }


    @Override
    public Artista login(String email, String password) {
        Optional<Artista> artista = repositorioArtista.buscarPorEmail(email);

        if (artista.isPresent() && artista.get().getPassword().equals(password)) {
            return artista.get();
        }
        return null;
    }
}
