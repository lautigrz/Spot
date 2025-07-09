package com.tallerwebi.punta_a_punta;

import com.microsoft.playwright.*;
import com.tallerwebi.punta_a_punta.vistas.VistaLogin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class VistaPerfilE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaLogin vistaLogin;


    @BeforeAll
    static void abrirNavegador(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
    }

    @AfterAll
    static void cerrarNavegador(){
        playwright.close();
    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }

    @BeforeEach
    void crearContextoPagina(){
        ReiniciarDB.limpiarBaseDeDatos();

        context = browser.newContext();
        Page page = context.newPage();
        vistaLogin = new VistaLogin(page);
    }

    @Test
    void deberiaRedirigirAlHomeLuegoDeAceptarAutenticacionConSpotify(){
        vistaLogin.darClickEnContinuarConSpotify();
        vistaLogin.darClickEnAceptarAlSerRedirigidoAlAutenticadorDeSpotify();
        String url = vistaLogin.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/spring/home"));
    }

    @Test
    void deberiaRedirigirAlPerfilLuegoDeHacerClickEnElBotonDePerfil(){
        vistaLogin.darClickEnBotonPerfil();
        String url = vistaLogin.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/spring/perfil"));
    }

    @Test
    void deberiaVisualizarElEstadoDeAnimoEnElPerfilAlElegirUno(){
        String selectEstadoAnimo = "#estadoDeAnimoId";
        String estadoDeAnimo = "Feliz";
        vistaLogin.seleccionarEstadoDeAnimo(selectEstadoAnimo, estadoDeAnimo);
        vistaLogin.actualizarEstadoDeAnimo();
        vistaLogin.obtenerTextoDeLaBarraDeEstadoDeAnimo();
    }

}
