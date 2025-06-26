package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class ControladorNuevaComunidad {


    private ServicioNuevaComunidad servicioNuevaComunidad;
    private ServicioGuardarImagen servicioGuardarImagen;
    private ServicioUsuario servicioUsuario;
    public ControladorNuevaComunidad(ServicioNuevaComunidad servicioNuevaComunidad,ServicioGuardarImagen servicioGuardarImagen, ServicioUsuario servicioUsuario) {
        this.servicioNuevaComunidad = servicioNuevaComunidad;
        this.servicioGuardarImagen = servicioGuardarImagen;

        this.servicioUsuario = servicioUsuario;
    }


    @GetMapping("/nueva-comunidad")
    public String mostrarFormularioNuevaComunidad(Model model) {
        model.addAttribute("comunidad", new Comunidad());
        return "nueva-comunidad";
    }

    @PostMapping("/crear")
    public String crearComunidad(@ModelAttribute Comunidad comunidad,
                                 @RequestParam("fotoPerfil") MultipartFile fotoPerfil,
                                 @RequestParam("fotoPortada") MultipartFile fotoPortada, HttpSession session) throws IOException {


        String nombreArchivoPerfil = servicioGuardarImagen.guardarImagenPerfilDeComunidad(fotoPerfil);
        String nombreArchivoPortada = servicioGuardarImagen.guardarImagenPortadaDeComunidad(fotoPortada);
        Long idUsuario = (Long) session.getAttribute("user");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);

        comunidad.setUrlFoto(nombreArchivoPerfil);
        comunidad.setUrlPortada(nombreArchivoPortada);

        Long id = servicioNuevaComunidad.nuevaComunidad(comunidad, usuario, "Admin");
        return "redirect:/comunidad/" + id;
    }


}
