package com.tallerwebi.presentacion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EstadoPreescucha {
    private List<CancionSimpleDto> canciones;   // Rutas de audio
    private int indiceActual;         // Qué canción de la lista va
    private long timestampInicio;     // Cuándo empezó (en epoch millis)
    private boolean reproduciendo;
    private Long duracionMs;
    private String titulo;
      // ID de la comunidad a la que pertenece la preescucha
}
