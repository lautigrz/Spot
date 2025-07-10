package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

public class VistaComunidad extends VistaWeb{
    public VistaComunidad(Page page, String url) {
        super(page);
        page.navigate(url);
    }


    public void darClickEnCrearNuevaPlalList() {
        this.darClickEnElElemento("#playlist");
    }

    public void escribirNombrePlaylist(String nombre) {
        this.escribirEnElElemento("#nombrePlaylist", nombre);
    }

    public void darClickEnSiguiente() {
        this.darClickEnElElemento("#btnSiguiente");
    }

    public void buscarArtistaParaCanciones(String artista) {
        this.escribirEnElElemento("#inputBusqueda", artista);
    }
    public void esperarResultadosDeBusqueda() {
        this.page.waitForSelector("#contenedorCanciones", new Page.WaitForSelectorOptions().setTimeout(25000));
    }

    public void darClickEnLasPrimerasCanciones() {
        this.page.locator("#contenedorCanciones button.agregar").first().click();
        this.page.locator("#contenedorCanciones button.agregar").nth(1).click();
        this.page.locator("#contenedorCanciones button.agregar").nth(2).click();
    }

    public void darClickParaGuardarPlaylist() {
        this.darClickEnElElemento("#btnCrearPlaylist");
    }

    public void darClickEnEnEmpezarReproduccion() {
        this.darClickEnElElemento("#reproduccion");
    }

}
