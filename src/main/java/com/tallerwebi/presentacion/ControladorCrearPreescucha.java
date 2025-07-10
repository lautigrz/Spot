package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class ControladorCrearPreescucha {

    private final ServicioPreescucha servicioPreescucha;
    private final ServicioGuardarImagen servicioGuardarImagen;
    private final ServicioUsuarioComunidad servicioUsuarioComunidad;

    @Autowired
    public ControladorCrearPreescucha(ServicioPreescucha servicioPreescucha,
                                      ServicioGuardarImagen servicioGuardarImagen, ServicioUsuarioComunidad servicioUsuarioComunidad) {
        this.servicioPreescucha = servicioPreescucha;
        this.servicioGuardarImagen = servicioGuardarImagen;
        this.servicioUsuarioComunidad = servicioUsuarioComunidad;
    }

    @GetMapping("/crear-preescucha")
    public String crearPreescucha(Model model) {
        model.addAttribute("preescucha", new Preescucha());
        return "crear-preescucha";
    }

    @PostMapping("/crear-preescucha")
    public String procesarPreescucha(@ModelAttribute Preescucha preescucha,
                                     @RequestParam("imagenPortada") MultipartFile imagen,@RequestParam("archivoAudio") MultipartFile archivoAudio, HttpSession session) throws IOException {

        Artista artista = (Artista) session.getAttribute("artista");
        String urlImagen = servicioGuardarImagen.guardarImagenPreescucha(imagen);
        preescucha.setPreescuchaFotoUrl(urlImagen);

        String urlAudio = servicioGuardarImagen.guardarAudioPreescucha(archivoAudio);
        preescucha.setRutaAudio(urlAudio);
        preescucha.setArtista(artista);



        servicioPreescucha.crearPreescuchaLocal(
                preescucha.getPrecio(),
                preescucha.getTitulo(),
                preescucha.getPreescuchaFotoUrl(),
                preescucha.getRutaAudio(),
                artista);

        System.out.println("Preescucha creada");
        System.out.println("Titulo: " + preescucha.getTitulo());
        System.out.println("URL Imagen: " + preescucha.getPreescuchaFotoUrl());
        System.out.println("Audio: " + preescucha.getRutaAudio());
        System.out.println("Precio: " + preescucha.getPrecio());
        System.out.println("Artista: " + preescucha.getArtista().getNombre());

        return "redirect:/home";
    }
}
