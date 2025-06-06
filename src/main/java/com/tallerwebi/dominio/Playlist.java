package com.tallerwebi.dominio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
}
