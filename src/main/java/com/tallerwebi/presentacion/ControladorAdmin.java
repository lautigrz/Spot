package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioAdmin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ControladorAdmin {

    private ServicioAdmin servicioAdmin;
    public ControladorAdmin(ServicioAdmin servicioAdmin) {
        this.servicioAdmin = servicioAdmin;
    }

    @PostMapping("/eliminarMiembro/{idComunidad}/{idMiembro}")
    public String eliminarMiembroDeComunidad(@PathVariable Long idComunidad,@PathVariable Long idMiembro) {
        servicioAdmin.eliminarMiembroDeComunidad(idComunidad, idMiembro);
        return "redirect:/comunidad/" + idComunidad;
    }

    @PostMapping("/hacerAdmin/{idComunidad}/{idMiembro}")
    public String hacerAdminAUnMiembro(@PathVariable Long idComunidad, @PathVariable Long idMiembro) {
        servicioAdmin.hacerAdminAUnMiembro(idComunidad, idMiembro);
        return "redirect:/comunidad/" + idComunidad;
    }
}
