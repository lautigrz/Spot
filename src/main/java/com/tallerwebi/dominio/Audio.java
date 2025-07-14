package com.tallerwebi.dominio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Audio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rutaAudio;

    private String portadaUrl;

    private String titulo;

    private Long duracion;

    @ManyToOne
    @JoinColumn(name = "preescucha_id")
    private Preescucha preescucha;
}
