package com.tallerwebi.presentacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.tallerwebi.dominio.HomeService;
import org.springframework.web.bind.annotation.ResponseBody;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;

import java.util.List;
@Controller
public class HomeController {

    private final HomeService homeService;

    @Autowired
    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/albums")
    public String albums(Model model) {
        try {
            // Ejemplo con un artista hardcodeado
            String artistId = "1vCWHaC5f2uS3yhpwWbIA6"; // Avicii por ejemplo
            AlbumSimplified[] albums = homeService.getAlbumsByArtist(artistId);
            model.addAttribute("albums", albums);
        } catch (Exception e) {
            model.addAttribute("error", "Error obteniendo álbumes");
            e.printStackTrace();
        }
        return "albums"; // nombre de la vista Thymeleaf

    }
}
