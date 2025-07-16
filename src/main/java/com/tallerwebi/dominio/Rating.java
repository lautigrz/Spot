package com.tallerwebi.dominio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rating {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Cancion cancion;

    private Integer puntaje;

    public Rating(Usuario usuario, Cancion cancion, Integer puntaje) {
        this.usuario = usuario;
        this.cancion = cancion;
        this.puntaje = puntaje;
    }
}
