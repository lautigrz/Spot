package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

public class VistaNuevaComunidad extends VistaWeb{
    public VistaNuevaComunidad(Page page) {
        super(page);
        page.navigate("http://localhost:8080/spring/nueva-comunidad");
    }

    public void escribirNombreComunidad(String nombre) {
        this.escribirEnElElemento("input[name='nombre']", nombre);
    }

    public void escribirDescripcionComunidad(String descripcion) {
        this.escribirEnElElemento("textarea[name='descripcion']", descripcion);
    }

    public void escribirSobreQuienEsLaComunidad(String sobre) {
        this.escribirEnElElemento("input[name='artista']", sobre);
    }

    public void darClickEnCrearComunidad() {
        this.darClickEnElElemento("#btn-crear-comunidad");

    }

    public void esperarRedireccionAComunidad() {
        this.page.waitForURL("**/spring/comunidad/**", new Page.WaitForURLOptions().setTimeout(10000));
    }


}
