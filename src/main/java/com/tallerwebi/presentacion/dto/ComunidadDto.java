package com.tallerwebi.presentacion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComunidadDto {
    private Long id;
    private String nombre;
    private String urlFoto;

}
