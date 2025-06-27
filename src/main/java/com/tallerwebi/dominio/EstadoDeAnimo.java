package com.tallerwebi.dominio;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "estado_de_animo")
public class EstadoDeAnimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Float energy;
    private Float danceability;
    private Float valence;
    private Float tempo;

    public EstadoDeAnimo(){
    }

    public EstadoDeAnimo(String nombre, Float energy, Float danceability, Float valence, Float tempo) {
        this.nombre = nombre;
        this.energy = energy;
        this.danceability = danceability;
        this.valence = valence;
        this.tempo = tempo;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Float getEnergy() {
        return energy;
    }
    public void setEnergy(Float energy) {
        this.energy = energy;
    }
    public Float getDanceability() {
        return danceability;
    }
    public void setDanceability(Float danceability) {
        this.danceability = danceability;
    }
    public Float getValence() {
        return valence;
    }
    public void setValence(Float valence) {
        this.valence = valence;
    }
    public Float getTempo() {
        return tempo;
    }
    public void setTempo(Float tempo) {
        this.tempo = tempo;
    }

}