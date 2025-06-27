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
public class Comunidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;
    private String urlFoto;
    private String urlPortada;

    @OneToMany(mappedBy = "comunidad", fetch = FetchType.LAZY)
    private List<Mensaje> mensajes = new ArrayList<>();

    @OneToMany(mappedBy = "comunidad", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<UsuarioComunidad> usuarios = new HashSet<>();


    @OneToMany(mappedBy = "comunidad", fetch = FetchType.LAZY)
    private List<Playlist> playlists = new ArrayList<>();

    public void agregarMensaje(Mensaje mensaje) {
        mensajes.add(mensaje);
        mensaje.setComunidad(this); // ← este es el punto clave
    }


    public void agregarPlaylist(Playlist playlist) {
        playlists.add(playlist);
        playlist.setComunidad(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Comunidad comunidad = (Comunidad) o;
        return Objects.equals(id, comunidad.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
