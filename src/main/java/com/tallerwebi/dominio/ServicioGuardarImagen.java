package com.tallerwebi.dominio;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ServicioGuardarImagen {
    String guardarImagenDePlaylist(MultipartFile imagen) throws IOException;
    String guardarImagenPerfilDeComunidad(MultipartFile imagen) throws IOException;
    String guardarImagenPortadaDeComunidad(MultipartFile imagen) throws IOException;
    String guardarImagenPerfilDeArtista(MultipartFile fotoPerfil) throws IOException;
    String guardarImagenPreescucha(MultipartFile imagen) throws IOException;
    String guardarAudioPreescucha(MultipartFile archivoAudio) throws IOException;
}
