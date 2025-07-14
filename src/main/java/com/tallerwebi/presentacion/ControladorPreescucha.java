package com.tallerwebi.presentacion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.tallerwebi.dominio.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ControladorPreescucha {

    private final ServicioPreescucha servicioPreescucha;
    private final ServicioGuardarImagen servicioGuardarImagen;
    private ServicioComunidad servicioComunidad;
    private ServicioUsuario servicioUsuario;
    private ServicioMercadoPago servicioMercadoPago;
    private ServicioUsuarioPreescucha servicioUsuarioPreescucha;
    private ServicioUsuarioComunidad servicioUsuarioComunidad;

    @Autowired
    private ServicioAudio servicioAudio;


    public ControladorPreescucha(ServicioPreescucha servicioPreescucha,
                                 ServicioGuardarImagen servicioGuardarImagen, ServicioComunidad servicioComunidad, ServicioUsuario servicioUsuario, ServicioMercadoPago servicioMercadoPago, ServicioUsuarioPreescucha servicioUsuarioPreescucha, ServicioUsuarioComunidad servicioUsuarioComunidad) {
        this.servicioPreescucha = servicioPreescucha;
        this.servicioGuardarImagen = servicioGuardarImagen;
        this.servicioUsuarioComunidad = servicioUsuarioComunidad;
        this.servicioMercadoPago = servicioMercadoPago;
        this.servicioComunidad = servicioComunidad;
        this.servicioUsuario = servicioUsuario;
        this.servicioUsuarioPreescucha = servicioUsuarioPreescucha;
    }

    @GetMapping("/crear-preescucha")
    public String crearPreescucha(Model model) {
        model.addAttribute("preescucha", new Preescucha());
        return "crear-preescucha";
    }

    @PostMapping("/crear-preescucha")
    public String procesarPreescucha(
            @ModelAttribute Preescucha preescucha,
            @RequestParam("imagenPortada") MultipartFile imagen,
            @RequestParam("archivoAudio") List<MultipartFile> archivos,
            @RequestParam("portadaAudio") List<MultipartFile> portadaAudio,
            @RequestParam("titulosCanciones") List<String> titulos,
            HttpSession session) throws Exception {

        Artista artista = (Artista) session.getAttribute("artista");


        String urlImagen = servicioGuardarImagen.guardarImagenPreescucha(imagen);
        preescucha.setPreescuchaFotoUrl(urlImagen);


        preescucha.setArtista(artista);


        List<Audio> audios = new ArrayList<>();
        for (int i = 0; i < archivos.size(); i++) {
            MultipartFile archivo = archivos.get(i);
            MultipartFile portada = portadaAudio.get(i);
            if (!archivo.isEmpty()) {
                String url = servicioGuardarImagen.guardarAudioPreescucha(archivo);
                String urlPortada = servicioGuardarImagen.guardarImagenPreescucha(portada);
                Long duracion = servicioAudio.duracionDeAudio(url);
                Audio audio = new Audio();
                audio.setTitulo(titulos.get(i));
                audio.setRutaAudio(url);
                audio.setPortadaUrl(urlPortada);
                audio.setPreescucha(preescucha);
                audio.setDuracion(duracion);

                audios.add(audio);
            }
        }

        preescucha.setAudios(audios);

        servicioPreescucha.crearPreescuchaLocal(preescucha);


        servicioComunidad.crearComunidadParaUnaPreescucha(preescucha.getId());

        return "redirect:/home";
    }



    @PostMapping("/artistas/{id}/comprar-preescucha")
    public String comprarPreescucha(@PathVariable String id, HttpSession session, String albumId) {
        Object usuarioIdObj = session.getAttribute("user");

        if (usuarioIdObj != null) {
            Long usuarioId = Long.valueOf(usuarioIdObj.toString());
            Usuario usuario = servicioUsuario.obtenerUsuarioPorId(usuarioId);

            if(!servicioPreescucha.yaComproPreescucha(albumId, usuario)){
                try{
                    /*
                    Preference pref = servicioMercadoPago.crearPreferenciaPago(
                            "Pre-escucha exclusiva del album " + albumId,
                            new BigDecimal("100.00"),
                            "https://01d477d4e984.ngrok-free.app/spring/pago-exitoso",
                            "https://01d477d4e984.ngrok-free.app/spring/pago-error",
                            albumId
                    );

                     */
                    return "redirect:"; //+ pref.getInitPoint();
                } catch (Exception e){
                    e.printStackTrace();
                    return "redirect:/perfil?errorPago";
                }
            }
        }
        return "redirect:/perfil";
    }

    @PostMapping("/artistas-locales/{artistaId}/comprar-preescucha/{preescuchaId}")
    public String comprarPreescuchaLocal(@PathVariable Long artistaId, @PathVariable Long preescuchaId, HttpSession session) {
        Long idUsuario = (Long) session.getAttribute("user");

        servicioUsuarioPreescucha.guardar(idUsuario, preescuchaId);

        return "redirect:/perfil";
    }

    @PostMapping("/comprar-preescucha/{idPreescucha}")
    public String comprarPreescuchaLocal(@PathVariable Long idPreescucha, HttpSession session) {

        Long idUsuario = (Long) session.getAttribute("user");

        if (!servicioUsuarioPreescucha.comprobarSiYaCompro(idUsuario, idPreescucha)) {

            Preescucha preescucha = servicioPreescucha.obtenerPreescuchaLocal(idPreescucha);

            try {

                System.out.println("ids:" + idUsuario + " - " + idPreescucha);

                String urlExito = "https://ff26a3a466f9.ngrok-free.app/spring/pago-exitoso/" + preescucha.getId() + "/" + idUsuario;


                String notificationUrl = "https://ff26a3a466f9.ngrok-free.app/spring/mercadopago/notification";

                Preference pref = servicioMercadoPago.crearPreferenciaPago(
                        "Pre-escucha exclusiva de " + preescucha.getTitulo(),
                        BigDecimal.valueOf(preescucha.getPrecio()),
                        urlExito,
                        "https://ff26a3a466f9.ngrok-free.app/spring/pago-error",
                        preescucha.getTitulo(),
                        idUsuario,
                        idPreescucha,
                        notificationUrl
                );

                return "redirect:" + pref.getInitPoint();

            } catch (Exception e) {
                e.printStackTrace();
                return "redirect:/perfil?errorPago";
            }
        }

        return "redirect:/perfil";
    }



    @GetMapping("/pago-exitoso/{idPreescucha}/{idUsuario}")
    public String pagoExitoso(@RequestParam Map<String, String> params, HttpSession session, Model model){
        Object usuarioIdObj = session.getAttribute("user");
        if (usuarioIdObj != null) {
            Long usuarioId = Long.valueOf(usuarioIdObj.toString());
            Usuario usuario = servicioUsuario.obtenerUsuarioPorId(usuarioId);

            String albumId = params.get("external_reference");

            if(albumId != null && !servicioPreescucha.yaComproPreescucha(albumId, usuario)){
                servicioPreescucha.comprarPreescucha(albumId, usuario);
            }
            model.addAttribute("albumId", albumId);
        }
        return "pago-exitoso";
    }

    @PostMapping("/mercadopago/notification")
    public ResponseEntity<Void> recibirNotificacion(@RequestBody String payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(payload);

            String topic = rootNode.path("type").asText();
            Long paymentId = null;

            if ("payment".equals(topic)) {
                paymentId = rootNode.path("data").path("id").asLong();
            } else {
                return ResponseEntity.ok().build();
            }

            System.out.println("Recibí notificación de pago ID: " + paymentId);

            if (paymentId == null || paymentId <= 0L || paymentId == 123456L) {
                System.out.println("Notificación de prueba. No se consulta API.");
                return ResponseEntity.ok().build();
            }

            PaymentClient paymentClient = new PaymentClient();
            Payment payment = paymentClient.get(paymentId);

            if (!"approved".equalsIgnoreCase(payment.getStatus())) {

                return ResponseEntity.ok().build();
            }

            Map<String, Object> metadata = payment.getMetadata();

            Double usuarioIdDouble = (Double) metadata.get("usuario_id");
            Double preescuchaIdDouble = (Double) metadata.get("preescucha_id");

            Long usuarioId = usuarioIdDouble.longValue();
            Long preescuchaId = preescuchaIdDouble.longValue();;


            servicioUsuarioPreescucha.guardar(usuarioId, preescuchaId);
            servicioUsuarioComunidad.agregarUsuarioAComunidadDePreescucha(usuarioId,preescuchaId, "Oyente");

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/pago-error")
    public String pagoError(){
        return "pago-error";
    }
}
