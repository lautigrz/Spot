package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioCancion {
    void guardarCancion(Cancion cancion);        // Para crear o actualizar una canción
    Cancion obtenerPorId(Long id);               // Buscar canción por id
    List<Cancion> obtenerTodas();                // Listar todas las canciones
    void eliminarCancion(Long id);
}
