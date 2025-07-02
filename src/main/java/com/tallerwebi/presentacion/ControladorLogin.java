package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Artista;
import com.tallerwebi.dominio.ServicioLoginArtista;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class ControladorLogin {

    private final ServicioLoginArtista servicioLoginArtista;

    public ControladorLogin(ServicioLoginArtista servicioLoginArtista) {
        this.servicioLoginArtista = servicioLoginArtista;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginComoArtista(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        Artista artista = servicioLoginArtista.login(email, password);

        if (artista == null) {
            model.addAttribute("error", "Email o contraseña incorrecta");
            return "login";
        }

        session.setAttribute("artista", artista);
        return "redirect:/home";
    }

}
