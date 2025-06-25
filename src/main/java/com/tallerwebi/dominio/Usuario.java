package com.tallerwebi.dominio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String user;
    private Boolean activo = false;
    @Lob
    private String token;
    @Lob
    private String refreshToken;
    private String urlFoto;
    private String spotifyID;
    @OneToOne
    private EstadoDeAnimo estadoDeAnimo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "comunidad_usuario", // tabla intermedia
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "comunidad_id")
    )
    private Set<Comunidad> comunidades = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuario_seguidores",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "seguidor_id")
    )
    private Set<Usuario> seguidores = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuario_segudiores",
            joinColumns = @JoinColumn(name = "seguidor_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private Set<Usuario> seguidos = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
