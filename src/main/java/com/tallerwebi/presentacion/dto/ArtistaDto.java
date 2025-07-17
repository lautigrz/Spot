package com.tallerwebi.presentacion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArtistaDto {
    private Long id; // Solo para artistas locales. Para Spotify, puede ir null.
    private String nombre;
    private String fotoPerfil;
    private String urlPerfil;
    private boolean local;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ArtistaDto that = (ArtistaDto) o;
        return Objects.equals(id, that.id) && local == that.local;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, local);
    }
}
