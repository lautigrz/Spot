package com.tallerwebi.presentacion.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Sincronizacion {
    private List<String> uris;
    private int positionMs;
    private String uriInicio;
}
