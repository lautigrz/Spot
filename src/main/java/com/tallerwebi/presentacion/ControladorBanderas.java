package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Pais;
import com.tallerwebi.dominio.ServicioBanderas;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ControladorBanderas {

    private final ServicioBanderas servicioBanderas;

    public ControladorBanderas(ServicioBanderas servicioBanderas) {
        this.servicioBanderas = servicioBanderas;
    }

    @GetMapping("/pais-bandera")
    public String vistaBandera() {
        return "pais-bandera";
    }

    @PostMapping("/pais-bandera")
    public String mostrarBandera(@RequestParam("codigoPais") String codigoPais, Model model) {
        String paisUrl = servicioBanderas.getBandera(codigoPais);
        model.addAttribute("banderaUrl", paisUrl);
        return "pais-bandera";
    }

    @GetMapping("/pais-info")
    public String vistaInfoPaises() {
        return "pais-info";
    }

    @PostMapping("/pais-info")
    public String mostrarInfoPaises(@RequestParam("code") String code, Model model) {
        Pais pais = servicioBanderas.getInfoPais(code);

        if (pais != null) {
            model.addAttribute("pais", pais);
        } else {
            model.addAttribute("error", "País no encontrado");
        }

        return "pais-info";
    }
}
