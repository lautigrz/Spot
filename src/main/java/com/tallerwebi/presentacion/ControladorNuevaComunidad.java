package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Comunidad;
import com.tallerwebi.dominio.ServicioGuardarImagen;
import com.tallerwebi.dominio.ServicioGuardarImagenImpl;
import com.tallerwebi.dominio.ServicioNuevaComunidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class ControladorNuevaComunidad {

    @Autowired
    private ServicioNuevaComunidad servicioNuevaComunidad;
    private ServicioGuardarImagen servicioGuardarImagen;

    public ControladorNuevaComunidad(ServicioNuevaComunidad servicioNuevaComunidad,ServicioGuardarImagen servicioGuardarImagen) {
        this.servicioNuevaComunidad = servicioNuevaComunidad;
        this.servicioGuardarImagen = servicioGuardarImagen;
    }

    @GetMapping("/nueva-comunidad")
    public String mostrarFormularioNuevaComunidad(Model model) {
        model.addAttribute("comunidad", new Comunidad());
        return "nueva-comunidad";
    }

    @PostMapping("/crear")
    public String crearComunidad(@ModelAttribute Comunidad comunidad,
                                 @RequestParam("fotoPerfil") MultipartFile fotoPerfil,
                                 @RequestParam("fotoPortada") MultipartFile fotoPortada) throws IOException {


        String nombreArchivoPerfil = servicioGuardarImagen.guardarImagenPerfilDeComunidad(fotoPerfil);
        String nombreArchivoPortada = servicioGuardarImagen.guardarImagenPortadaDeComunidad(fotoPortada);

        comunidad.setUrlFoto(nombreArchivoPerfil);
        comunidad.setUrlPortada(nombreArchivoPortada);

        Long id = servicioNuevaComunidad.nuevaComunidad(comunidad);

        return "redirect:/comunidad/" + id;
    }


}
