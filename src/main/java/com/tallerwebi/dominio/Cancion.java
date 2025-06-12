package com.tallerwebi.dominio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cancion {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique=true)
    private String spotifyId;

    private String uri;

    private String urlImagen;

    private String titulo;

    private String artista;

    @ManyToMany(mappedBy = "canciones")
    private Set<Playlist> playlists = new HashSet<>();

}
