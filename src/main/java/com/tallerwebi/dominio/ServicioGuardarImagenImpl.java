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
        String ruta = "src/main/webapp/resources/core/uploads/foto-playlist/" + nombreArchivo;
        imagen.transferTo(new File(ruta));
        String urlImagen = "../uploads/foto-playlist/" + nombreArchivo;

        return urlImagen;
    }

    @Override
    public String guardarImagenPerfilDeComunidad(MultipartFile imagen) throws IOException {
        String nombreArchivo = UUID.randomUUID() + "-" + imagen.getOriginalFilename();
        String ruta = "src/main/webapp/resources/core/uploads/perfil-comunidad/" + nombreArchivo;
        imagen.transferTo(new File(ruta));
        String urlImagen = "../uploads/perfil-comunidad/" + nombreArchivo;

        return urlImagen;
    }

    @Override
    public String guardarImagenPortadaDeComunidad(MultipartFile imagen) throws IOException {
        String nombreArchivo = UUID.randomUUID() + "-" + imagen.getOriginalFilename();
        String ruta = "src/main/webapp/resources/core/uploads/portada-comunidad/" + nombreArchivo;
        imagen.transferTo(new File(ruta));
        String urlImagen = "../uploads/portada-comunidad/" + nombreArchivo;

        return urlImagen;
    }
}
