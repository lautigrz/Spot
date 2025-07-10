package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ServicioGuardarImagenImpl implements ServicioGuardarImagen {

    @Override
    public String guardarImagenDePlaylist(MultipartFile imagen) throws IOException {
        String nombreArchivo = UUID.randomUUID() + "-" + imagen.getOriginalFilename();
        String ruta = "uploads/foto-playlist/" + nombreArchivo;
        imagen.transferTo(new File(ruta));
        String urlImagen = "/spring/uploads/foto-playlist/" + nombreArchivo;

        return urlImagen;
    }

    @Override
    public String guardarImagenPerfilDeComunidad(MultipartFile imagen) throws IOException {
        String nombreArchivo = UUID.randomUUID() + "-" + imagen.getOriginalFilename();
        String ruta = "uploads/perfil-comunidad/" + nombreArchivo;
        imagen.transferTo(new File(ruta));
        String urlImagen = "/spring/uploads/perfil-comunidad/" + nombreArchivo;

        return urlImagen;
    }

    @Override
    public String guardarImagenPortadaDeComunidad(MultipartFile imagen) throws IOException {
        String nombreArchivo = UUID.randomUUID() + "-" + imagen.getOriginalFilename();
        String ruta = "uploads/portada-comunidad/" + nombreArchivo;
        imagen.transferTo(new File(ruta));
        String urlImagen = "/spring/uploads/portada-comunidad/" + nombreArchivo;

        return urlImagen;
    }

    @Override
    public String guardarImagenPerfilDeArtista(MultipartFile imagen) throws IOException {
       String nombreArchivo = UUID.randomUUID() + "-" + imagen.getOriginalFilename();
       String ruta = "uploads/perfil-artista/" + nombreArchivo;
       imagen.transferTo(new File(ruta));
       String urlImagen = "/spring/uploads/perfil-artista/" + nombreArchivo;
       return urlImagen;
    }

    @Override
    public String guardarImagenPreescucha(MultipartFile imagen) throws IOException{
        String nombreArchivo = UUID.randomUUID() + "-" + imagen.getOriginalFilename();
        String ruta = "uploads/portadas-preescucha/" + nombreArchivo;
        imagen.transferTo(new File(ruta));
        String urlImagen = "/spring/uploads/portadas-preescucha/" + nombreArchivo;
        return urlImagen;
    }

    @Override
    public String guardarAudioPreescucha(MultipartFile archivoAudio) throws IOException {
        String nombreArchivo = UUID.randomUUID() + "-" + archivoAudio.getOriginalFilename();
        String ruta = "uploads/preescuchas-audio/" + nombreArchivo;
        archivoAudio.transferTo(new File(ruta));
        String urlAudio = "/spring/uploads/preescuchas-audio/" + nombreArchivo;
        return urlAudio;
    }


}
