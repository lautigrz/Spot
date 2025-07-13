package com.tallerwebi.dominio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Artista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @Column(unique=true)
    private String email;
    private String password;
    private String fotoPerfil;

    @OneToMany(mappedBy = "artista", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Preescucha> preescuchas = new ArrayList<>();

    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comunidad> comunidades = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Artista artista = (Artista) o;
        return Objects.equals(email, artista.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }
}
