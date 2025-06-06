package com.tallerwebi.presentacion;
import com.tallerwebi.dominio.ServicioComunidad;

import com.tallerwebi.presentacion.dto.ChatMessage;
import com.tallerwebi.presentacion.dto.Sincronizacion;
import com.tallerwebi.presentacion.dto.UsuarioDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpSession;

@Controller
public class ControladorComunidad {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private ServicioComunidad servicioComunidad;

    public ControladorComunidad(ServicioComunidad servicioComunidad) {
        this.servicioComunidad = servicioComunidad;
    }


    @GetMapping("lista")
    public String listar(Model model) {

        model.addAttribute("comunidades", servicioComunidad.obtenerTodasLasComunidades());

        return "lista-comunidades";
    }

    @PostMapping("/sincronizarme/{idComunidad}")
    @ResponseBody
    public ResponseEntity<?> sincronizar(@Payload ChatMessage message, @PathVariable Long idComunidad) {


        try {
            System.out.println("Entró a sincronizar");

            //String username = (String) headerAccessor.getSessionAttributes().get("usuario");

            String usuario = servicioComunidad.obtenerUsuarioDeLaComunidadActivoDeLaLista(String.valueOf(idComunidad), message.getSender());

            return ResponseEntity.status(HttpStatus.OK).body(
                    servicioComunidad.obtenerSincronizacion(usuario,idComunidad)
            );

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al obtener la sincronización");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Sincronizacion());
        }
    }

    @GetMapping("/reproducir/{idComunidad}")
    public String reproducirMusica(HttpSession session, @PathVariable String idComunidad) {
        try {
            String token = (String) session.getAttribute("token");
            servicioComunidad.reproducirCancion(token);

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }

        // Redirigir a la página de la comunidad usando el idComunidad recibido
        return "redirect:/comunidad/" + idComunidad;
    }


    @MessageMapping("/chat.register/{idComunidad}")
    public void register(@Payload ChatMessage message,
                         @DestinationVariable String idComunidad,
                         SimpMessageHeaderAccessor headerAccessor) {

        servicioComunidad.registrarUsuarioEnCanalDeComunidad(message, headerAccessor, idComunidad);

        // Enviar manualmente el mensaje al canal de esa comunidad
        messagingTemplate.convertAndSend("/topic/" + idComunidad, message);
    }

    @MessageMapping("/chat.send/{idComunidad}")
    public void send(@Payload ChatMessage message,
                     @DestinationVariable String idComunidad) {

        try {
            Long id = Long.parseLong(message.getId());
            Long idComuni = Long.parseLong(idComunidad);
            ChatMessage response = servicioComunidad.guardarMensaje(message, id, idComuni);
            messagingTemplate.convertAndSend("/topic/" + idComunidad, response);
        } catch (NumberFormatException e) {
            System.err.println("ID de usuario inválido: " + message.getId());
        }
    }

    @GetMapping("/comunidad/{id}")
    public String comunidad(Model model, HttpSession session, @PathVariable Long id) {

        Long idUsuario = (Long) session.getAttribute("user");
        String idComunidad = String.valueOf(id);
        UsuarioDto usuarioDto = servicioComunidad.obtenerUsuarioDeLaComunidad(idUsuario, id);
        boolean estaEnComunidad = false;

        if(usuarioDto != null){
            model.addAttribute("usuario", usuarioDto.getUser());
            model.addAttribute("urlFoto", usuarioDto.getUrlFoto());
            model.addAttribute("id", usuarioDto.getId());
            model.addAttribute("token", usuarioDto.getToken());
            model.addAttribute("hayUsuarios", servicioComunidad.hayAlguienEnLaComunidad(idComunidad, usuarioDto.getUser()));
            model.addAttribute("mensajes", servicioComunidad.obtenerMensajes(id));
            System.out.println("usuario:" + servicioComunidad.hayAlguienEnLaComunidad(idComunidad, usuarioDto.getUser()));
            estaEnComunidad = true;
        }

        model.addAttribute("comunidad", id);
        model.addAttribute("estaEnComunidad", estaEnComunidad);

        return "comunidad-general";
    }


    //ResponseEntity clase de Spring que representa toda la respuesta HTTP
    @GetMapping("/unirme/{idComunidad}")
    public String unirme(HttpSession session, @PathVariable Long idComunidad) {
        Long idUsuario = (Long) session.getAttribute("user");
        Boolean seGuardo = servicioComunidad.guardarUsuarioEnComunidad(idUsuario,idComunidad);

        if(!seGuardo){

            return "redirect:/error";

        }

        return "redirect:/comunidad/" + idComunidad;
    }

    @GetMapping("/usuario-en-comunidad/{idUsuario}/{idComunidad}")
    @ResponseBody
    public ResponseEntity<?> usuarioEnComunidad(@PathVariable Long idUsuario, @PathVariable Long idComunidad) {

        UsuarioDto usuarioDto = servicioComunidad.obtenerUsuarioDeLaComunidad(idUsuario, idComunidad);

        if(usuarioDto == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}