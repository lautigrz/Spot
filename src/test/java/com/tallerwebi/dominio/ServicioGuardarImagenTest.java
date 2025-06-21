package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;

public class ServicioGuardarImagenTest {

    private ServicioGuardarImagen servicioGuardarImagen;
    private MultipartFile imagen;
    @BeforeEach
    public void setUp() {
        servicioGuardarImagen = new ServicioGuardarImagenImpl();
        imagen = mock(MultipartFile.class);

    }

    @Test
    public void seDebeGuardarUnaImagenDePlaylist() throws Exception {
        String nombreOriginal = "mi-imagen.png";
        byte[] contenidoFalso = "contenido-falso".getBytes();

        when(imagen.getOriginalFilename()).thenReturn(nombreOriginal);
        when(imagen.getBytes()).thenReturn(contenidoFalso);
        doAnswer(invoc -> {
            File archivo = invoc.getArgument(0); // obtiene el argumento que se pasó al metodo transferTo(File)
            archivo.getParentFile().mkdirs(); // crea las carpetas necesarias, si no existen
            Files.write(archivo.toPath(), contenidoFalso); // escribe datos simulados en el archivo
            return null; // porque transferTo devuelve void
        }).when(imagen).transferTo(any(File.class));

        // Act
        String resultado = servicioGuardarImagen.guardarImagenDePlaylist(imagen);

        assertThat(resultado, containsString("../uploads/foto-playlist/"));

    }
    @Test
    public void seDebeGuardarUnaImagenDePerfilDeLaComunidad() throws Exception {
        String nombreOriginal = "mi-imagen.png";
        byte[] contenidoFalso = "contenido-falso".getBytes();

        when(imagen.getOriginalFilename()).thenReturn(nombreOriginal);
        when(imagen.getBytes()).thenReturn(contenidoFalso);
        doAnswer(invoc -> {
            File archivo = invoc.getArgument(0);
            archivo.getParentFile().mkdirs(); // asegura que la carpeta existe
            Files.write(archivo.toPath(), contenidoFalso); // simula el guardado
            return null;
        }).when(imagen).transferTo(any(File.class));

        // Act
        String resultado = servicioGuardarImagen.guardarImagenPerfilDeComunidad(imagen);

        assertThat(resultado, containsString("../uploads/perfil-comunidad/"));

    }
    @Test
    public void seDebeGuardarUnaImagenDePortadaDeLaComunidad() throws Exception {
        String nombreOriginal = "mi-imagen.png";
        byte[] contenidoFalso = "contenido-falso".getBytes();

        when(imagen.getOriginalFilename()).thenReturn(nombreOriginal);
        when(imagen.getBytes()).thenReturn(contenidoFalso);
        doAnswer(invoc -> {
            File archivo = invoc.getArgument(0);
            archivo.getParentFile().mkdirs(); // asegura que la carpeta existe
            Files.write(archivo.toPath(), contenidoFalso); // simula el guardado
            return null;
        }).when(imagen).transferTo(any(File.class));

        // Act
        String resultado = servicioGuardarImagen.guardarImagenPortadaDeComunidad(imagen);

        assertThat(resultado, containsString("../uploads/portada-comunidad/"));

    }


}
