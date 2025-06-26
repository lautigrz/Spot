package com.tallerwebi.presentacion.dto;

import com.tallerwebi.dominio.Comunidad;
import com.tallerwebi.dominio.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecomendacionDto {
    private String spotifyId;
    private String uri;
    private String urlImagen;
    private String titulo;
    private String artista;

}
