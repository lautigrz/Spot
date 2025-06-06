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
    private String nombreCancion;

   /* @ManyToMany
    @JoinTable(
            name = "comunidad_playlist",
            joinColumns = @JoinColumn(name = "comunidad_id"),
            inverseJoinColumns = @JoinColumn(name = "playlist_id")
    )
    private List<Playlist> playlist = new ArrayList<>();

    */

    @OneToMany(mappedBy = "comunidad", fetch = FetchType.LAZY)
    private List<Mensaje> mensajes = new ArrayList<>();

    @ManyToMany(mappedBy = "comunidades", fetch = FetchType.LAZY)
    private Set<Usuario> usuarios = new HashSet<>();

    @Column(nullable = false, unique = true, updatable = false)



    public void agregarMensaje(Mensaje mensaje) {
        mensajes.add(mensaje);
        mensaje.setComunidad(this); // ← este es el punto clave
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
