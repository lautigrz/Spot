package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Artista;
import com.tallerwebi.dominio.ServicioComentario;
import com.tallerwebi.dominio.ServicioLike;
import com.tallerwebi.dominio.ServicioPosteo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ControladorPost {

    private ServicioPosteo servicioPosteo;
    private ServicioLike servicioLike;
    private ServicioComentario servicioComentario;

    public ControladorPost(ServicioPosteo servicioPosteo, ServicioLike servicioLike, ServicioComentario servicioComentario) {
        this.servicioPosteo = servicioPosteo;
        this.servicioLike = servicioLike;
        this.servicioComentario = servicioComentario;
    }

    @PostMapping("/postear")
    public String postearTexto(@RequestParam("texto") String texto, HttpSession session) {
        Object artistaObj = session.getAttribute("artista");
        if (artistaObj == null) {

            return "redirect:/home";
        }

        Artista artista = (Artista) artistaObj;
        servicioPosteo.publicarPosteo(artista, texto);
        return "redirect:/home";
    }

    @PostMapping("/postear-perfil")
    public String postearDesdePerfil(@RequestParam("texto") String texto, HttpSession session) {
        Object artistaObj = session.getAttribute("artista");
        if (artistaObj == null) {

            return "redirect:/home";
        }

        Artista artista = (Artista) artistaObj;
        servicioPosteo.publicarPosteo(artista, texto);
        return "redirect:/perfil/artista";
    }

    @PostMapping("/like/{idPosteo}/{idUsuario}")
    @ResponseBody
    public ResponseEntity<?> darLike(
            @PathVariable("idPosteo") Long idPosteo,
            @PathVariable("idUsuario") Long idUsuario) {

        servicioLike.darLikeAPosteo(idPosteo, idUsuario);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Like dado correctamente");


        return ResponseEntity.ok(response);

    }

    @PostMapping("/dislike/{idPosteo}/{idUsuario}")
    @ResponseBody
    public ResponseEntity<?> quitarLike(
            @PathVariable("idPosteo") Long idPosteo,
            @PathVariable("idUsuario") Long idUsuario) {

        servicioLike.quitarLikeAPosteo(idPosteo, idUsuario);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "dislike dado correctamente");


        return ResponseEntity.ok(response);

    }

    @PostMapping("/comentar")
    public String comentarPost(@RequestParam("idPosteo") Long idPosteo,
                               @RequestParam("texto") String texto, HttpSession session) {

        Object usuarioObj = session.getAttribute("user");
        if (usuarioObj == null) {
            return "redirect:/home";
        }

        Long idUsuario = Long.valueOf(usuarioObj.toString());
        servicioComentario.comentarEnPosteo(idUsuario, idPosteo, texto);
        return "redirect:/home";
    }

}
