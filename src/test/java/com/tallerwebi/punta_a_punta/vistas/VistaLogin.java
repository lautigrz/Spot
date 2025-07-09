package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

public class VistaLogin extends VistaWeb {

    public VistaLogin(Page page) {
        super(page);
        page.navigate("localhost:8080/spring/login");
    }

    public String obtenerTextoDeLaBarraDeNavegacion(){
        return this.obtenerTextoDelElemento("nav a.navbar-brand");
    }

    public String obtenerMensajeDeError(){
        return this.obtenerTextoDelElemento("p.alert.alert-danger");
    }

    public void escribirEMAIL(String email){
        this.escribirEnElElemento("#email", email);
    }

    public void escribirClave(String clave){
        this.escribirEnElElemento("#password", clave);
    }

    public void darClickEnIniciarSesion(){
        this.darClickEnElElemento("#btn-login");
    }

    public void darClickEnContinuarConSpotify(){
        this.darClickEnElElemento("a.btn-spotify");
    }

    public void darClickEnAceptarAlSerRedirigidoAlAutenticadorDeSpotify(){
        this.darClickEnElElemento("button:has-text('Aceptar')");
    }

    public void darClickEnBotonPerfil() {
        this.darClickEnElElemento("a:has-text('Perfil')");
    }

    public void seleccionarEstadoDeAnimo(String selectorCSS, String opcionElegida) {
        this.seleccionarOpcion(selectorCSS, opcionElegida);
    }

    public void actualizarEstadoDeAnimo() {
        this.darClickEnElElemento("button:has-text('Actualizar')");
    }

    public void obtenerTextoDeLaBarraDeEstadoDeAnimo() {
        this.obtenerTextoDelElemento("");
    }
}
