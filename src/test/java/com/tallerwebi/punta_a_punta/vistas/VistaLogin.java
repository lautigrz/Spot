package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Page;

public class VistaLogin extends VistaWeb {

    public VistaLogin(Page page) {
        super(page);
        page.navigate("https://e63cc7a933be.ngrok-free.app/spring/login");
    }

    public Page getPage() {
        return this.page;
    }

    public void darClickEnBotonSpotify(){
        this.darClickEnElElemento("a.btn-spotify");
    }

    public void aceptarPermisosEnSpotify() {
        Locator botonAceptar = page.locator("[data-testid='auth-accept']");
        botonAceptar.waitFor(new Locator.WaitForOptions().setTimeout(10000));
        botonAceptar.scrollIntoViewIfNeeded();
        page.waitForTimeout(500);
        botonAceptar.click();
    }

    public void esperarRedireccionAlHome() {
        this.page.waitForURL("**/spring/home**", new Page.WaitForURLOptions().setTimeout(10000));


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

    public String obtenerTextoDeLaBarraDeEstadoDeAnimo() {
        page.waitForSelector("h5.mb-0 span");
        return this.obtenerTextoDelElemento("h5.mb-0 span");
    }

    public void waitTimeOut() {
        page.waitForTimeout(3000);
    }

    public void buscarArtista(String selector, String artista) {
        this.escribirEnElElemento(selector, artista);
        this.page.press(selector, "Enter");
    }


    public void hacerClickEnComprarPreescucha() {
        this.darClickEnElElemento("button:has-text('Comprar pre-escucha')");
    }


    public void hacerClickEnIngresarAMercadoPago() {
        this.darClickEnElElemento("button.andes-list__item-action:has(.icon_mercado_pago_yellow)");
    }

    public void ingresarUsuarioMercadoPago(String selector, String usuario) {
        this.escribirEnElElemento(selector, usuario);
    }

    public void ingresarPassMercadoPago(String selectorMpPass, String passMp) {
        this.escribirEnElElemento(selectorMpPass, passMp);
    }

    public void hacerClickEnSeleccionCuotas() {
        this.darClickEnElElemento("#installments-Cuotas-trigger");
        page.waitForSelector("#installments-Cuotas-menu-list");
    }

    public void hacerClickEnCantidadCuotas(){
        this.darClickEnElElemento("#installments-Cuotas-menu-list-option-1");
    }

    public void cambiarFocoAInputYCompletarCodigoSeguridad(String selector, String codigoSeguridad) {
        Frame codigoSeguridadFrame = page.frames()
                .stream()
                .filter(f -> "securityCode".equals(f.name()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No se encontró el frame con name='securityCode'"));

        codigoSeguridadFrame.fill(selector, codigoSeguridad);
    }

    public void hacerClickEnPagar(){
        this.darClickEnElElemento("button:has-text('Pagar')");
    }


    public void hacerClickEnContinuar() {
        this.darClickEnElElemento("button:has-text('Continuar')");
    }

    public void hacerClickEnIniciarSesion() {
        this.darClickEnElElemento("button:has-text('Iniciar sesión')");
    }

    public void hacerClickEnVisitSite() {
        this.darClickEnElElemento("button:has-text('Visit Site')");
    }

    public void hacerClickEnVolverAlPerfil() {
        this.darClickEnElElemento("a[href='/spring/perfil']");
    }
}
