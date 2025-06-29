package com.tallerwebi.dominio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Playlist {
    @Id
    @GeneratedValue
    private Long id;
    private String nombre;
    private String urlImagen;

    @ManyToMany
    @JoinTable(
            name = "playlist_cancion",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "cancion_id")
    )
    private Set<Cancion> canciones = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "comunidad_id")
    private Comunidad comunidad;

    public void agregarCanciones(Set<Cancion> canciones) {
        for (Cancion cancion : canciones) {
            this.canciones.add(cancion);
            cancion.getPlaylists().add(this); // si es bidireccional
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return Objects.equals(id, playlist.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
