package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioComunidad;
import com.tallerwebi.dominio.ServicioPerfil;
import com.tallerwebi.dominio.ServicioUsuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView vistaHome(HttpSession session) {
        ModelMap modelMap = new ModelMap();

        Long idUsuario = (Long) session.getAttribute("user");

        modelMap.put("usuario", servicioUsuario.obtenerUsuarioPorId(idUsuario));
        modelMap.put("comunidades", servicioComunidad.obtenerTodasLasComunidades());
        return new ModelAndView("home", modelMap);
    }
    @GetMapping("/cerrar")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }


}
