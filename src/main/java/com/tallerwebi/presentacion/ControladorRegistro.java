package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Artista;
import com.tallerwebi.dominio.RepositorioArtista;
import com.tallerwebi.dominio.ServicioArtista;
import com.tallerwebi.dominio.ServicioGuardarImagen;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class ControladorRegistro {

    private final ServicioArtista servicioArtista;
    private ServicioGuardarImagen servicioGuardarImagen;


    public ControladorRegistro(ServicioArtista servicioArtista, ServicioGuardarImagen servicioGuardarImagen) {
        this.servicioArtista = servicioArtista;
        this.servicioGuardarImagen = servicioGuardarImagen;
    }

    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarArtista(@RequestParam String nombre, @RequestParam String email, @RequestParam String password,
                                   @RequestParam("fotoPerfil") MultipartFile fotoPerfil, HttpSession session, Model model)throws IOException {

        if (servicioArtista.buscarPorEmail(email).isPresent()) {
            model.addAttribute("error", "Ya existe un artista con ese email");
            return "registro";
        }

        String urlFotoPerfil = servicioGuardarImagen.guardarImagenPerfilDeArtista(fotoPerfil);


        Artista nuevo = new Artista();
        nuevo.setNombre(nombre);
        nuevo.setEmail(email);
        nuevo.setPassword(password);
        nuevo.setFotoPerfil(urlFotoPerfil); // Por ahora nulo

        servicioArtista.guardar(nuevo);
        session.setAttribute("artista", nuevo);
        return "redirect:/login";



    }
}
