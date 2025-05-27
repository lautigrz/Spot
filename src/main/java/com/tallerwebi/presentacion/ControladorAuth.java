package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioAuth;
import com.tallerwebi.dominio.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URI;
@Controller
public class ControladorAuth {

    private ServicioAuth servicioAuth;

    public ControladorAuth(ServicioAuth servicioAuth) {
        this.servicioAuth = servicioAuth;
    }

    @GetMapping("/inicio")
    public String login(HttpServletResponse response) throws Exception {

        try {
            AuthorizationCodeUriRequest authorizationCodeRequest = servicioAuth.login();
            URI uri = authorizationCodeRequest.getUri();

            response.sendRedirect(uri.toString());

        }catch (Exception e) {  System.out.println("Error al iniciar login: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/error";

        }

        return "login";

        }

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code, HttpSession session) throws Exception {

        try{
            AuthorizationCodeCredentials credentials = servicioAuth.credentials(code);

            session.setAttribute("token", credentials.getAccessToken());
            session.setAttribute("refreshToken", credentials.getRefreshToken());
            Usuario usuario = generarUsuario(credentials.getAccessToken(), credentials.getRefreshToken());
            session.setAttribute("user", usuario.getUser());
            servicioAuth.guardarUsuario(usuario);

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/login";

        }

        return "redirect:/comunidad";
    }

    private Usuario generarUsuario(String token, String refreshToken) throws Exception {
        User user = servicioAuth.obtenerPerfilUsuario(token, refreshToken);

        if(user == null) {
            throw new Exception("Error al obtener el usuario");
        }
        Usuario usuario = new Usuario();
        usuario.setUser(user.getDisplayName());
        usuario.setToken(token);
        usuario.setRefreshToken(refreshToken);
        usuario.setUrlFoto(user.getImages()[0].getUrl());

        return usuario;
    }


}
