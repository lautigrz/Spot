package com.tallerwebi.presentacion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioPreescuchaDto {

    private Long idPreescucha;
    private String nombrePreescucha;
    private String artistaNombre;
    private String urlFotoPreescucha;
    private String fechaCompra;
    private Double precio;
    private String fechaInicio;

}
