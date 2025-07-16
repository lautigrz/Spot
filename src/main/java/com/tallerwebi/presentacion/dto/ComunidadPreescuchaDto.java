package com.tallerwebi.presentacion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ComunidadPreescuchaDto {
    private Long id;
    private String nombre;
    private String artistaNombre;
    private String fotoPreescucha;
    private Double precio;

}
