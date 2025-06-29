package com.tallerwebi.presentacion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventoDto {
    private String nombre;
    private String url;
    private String fecha;
    private String lugar;
    private String ciudad;
    private String pais;
    private String imagen;
}
