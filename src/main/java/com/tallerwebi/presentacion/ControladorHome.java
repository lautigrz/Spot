package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioComunidad;
import com.tallerwebi.dominio.ServicioPerfil;
import com.tallerwebi.dominio.ServicioUsuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ControladorHome {

    private ServicioUsuario servicioUsuario;
    private ServicioComunidad servicioComunidad;
    public ControladorHome(ServicioUsuario servicioUsuario, ServicioComunidad servicioComunidad) {
        this.servicioUsuario = servicioUsuario;
        this.servicioComunidad = servicioComunidad;
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {

        Long idUsuario = (Long) session.getAttribute("user");
        model.addAttribute("usuario", servicioUsuario.obtenerUsuarioPorId(idUsuario));
        model.addAttribute("comunidades", servicioComunidad.obtenerTodasLasComunidades());
        return "home";
    }
    @GetMapping("/cerrar")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

}
