package com.tallerwebi.presentacion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CancionDto {
    private Long id;
    private String spotifyId;
    private String uri;
    private String urlImagen;
    private String titulo;
    private String artista;
    private Integer duracion;
    private Integer progreso;
}
