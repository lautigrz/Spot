package com.tallerwebi.dominio;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Preescucha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String spotifyAlbumId;
    private LocalDateTime fechaEscucha;
    private Double precio;
    private String titulo;
    private String preescuchaFotoUrl;
    private String rutaAudio;

    @OneToMany(mappedBy = "preescucha", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Audio> audios = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "artista_id")
    private Artista artista;

    @OneToMany(mappedBy = "preescucha", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioPreescucha> usuariosQueCompraron = new ArrayList<>();

    @OneToOne(mappedBy = "preescucha", cascade = CascadeType.ALL, orphanRemoval = true)
    private Comunidad comunidad;

    public String getFechaFormateada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return this.fechaEscucha.format(formatter);
    }

}
