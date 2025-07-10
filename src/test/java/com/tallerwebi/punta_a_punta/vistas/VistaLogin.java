package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class VistaLogin extends VistaWeb {

    public VistaLogin(Page page) {
        super(page);
        page.navigate("http://localhost:8080/spring/login");
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
}
