package com.tallerwebi.dominio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cancion {
    @Id
    @GeneratedValue
    private Long id;

    private String titulo;

    @ManyToMany(mappedBy = "canciones")
    private List<Playlist> playlists;

}
