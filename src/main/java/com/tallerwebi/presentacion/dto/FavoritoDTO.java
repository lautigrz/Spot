package com.tallerwebi.presentacion.dto;

public class FavoritoDTO {

    private String spotifyArtistId;
    private String nombre;
    private String imagenUrl;

    public FavoritoDTO(String spotifyArtistId, String nombre, String imagenUrl) {
        this.spotifyArtistId = spotifyArtistId;
        this.nombre = nombre;
        this.imagenUrl = imagenUrl;
    }

    public String getSpotifyArtistId() {
        return spotifyArtistId;
    }

    public void setSpotifyArtistId(String spotifyArtistId) {
        this.spotifyArtistId = spotifyArtistId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}