package com.tallerwebi.dominio;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Preescucha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String spotifyAlbumId;
    private LocalDateTime fechaCompra;
    private Double precio;
    private String titulo;
    private String preescuchaFotoUrl;
    private String rutaAudio;


    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "artista_id")
    private Artista artista;

}
