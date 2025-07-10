package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

public class VistaHome extends VistaWeb{

    public VistaHome(Page page) {
        super(page);
        page.navigate("http://localhost:8080/spring/home");

    }

    public void buscarArtista(String nombre){
        this.escribirEnElElemento("input[name='nombre']", nombre);
        this.page.locator("form").first().evaluate("form => form.submit()"); //Envia el formulario
    }

    public void esperarDetalleArtista(){
        this.page.waitForURL("**/artistas/**", new Page.WaitForURLOptions().setTimeout(10000));
        this.page.waitForSelector("h1");
    }

    public String obtenerNombreArtista() {
        return this.obtenerTextoDelElemento("h1");
    }

    public void agregarArtistaAFavoritos(){
        this.darClickEnElElemento("button[name='favorito']");
    }

    public void esperarRedireccionAPerfil(){
        this.page.waitForURL("**/spring/perfil");
    }

    public String obtenerPrimerFavorito(){
        return this.obtenerTextoDelElemento("div:has(h4:has-text('Mis Favoritos')) ul li a");
    }

    public void darClickEnCrearNuevaComunidad(){

        this.darClickEnElElemento("a[href='/spring/nueva-comunidad']");

    }


}
