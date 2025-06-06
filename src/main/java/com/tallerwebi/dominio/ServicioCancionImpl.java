package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServicioCancionImpl implements ServicioCancion {
    private RepositorioCancion repositorioCancion;

    @Autowired
    public ServicioCancionImpl(RepositorioCancion repositorioCancion) {
        this.repositorioCancion = repositorioCancion;
    }

    @Override
    public void guardarCancion(Cancion cancion) {
        repositorioCancion.guardarCancion(cancion);
    }

    @Override
    public Cancion obtenerPorId(Long id) {
        return repositorioCancion.obtenerPorId(id);
    }

    @Override
    public List<Cancion> obtenerTodas() {
        return repositorioCancion.obtenerTodas();
    }

    @Override
    public void eliminarCancion(Long id) {
        repositorioCancion.eliminarCancion(id);
    }
}
