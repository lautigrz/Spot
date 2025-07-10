package com.tallerwebi.punta_a_punta;

import com.microsoft.playwright.*;
import com.tallerwebi.punta_a_punta.vistas.VistaComunidad;
import com.tallerwebi.punta_a_punta.vistas.VistaHome;
import com.tallerwebi.punta_a_punta.vistas.VistaLogin;
import com.tallerwebi.punta_a_punta.vistas.VistaNuevaComunidad;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;

public class ComunidadE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaLogin vistaLogin;
    VistaHome vistaHome;
    VistaNuevaComunidad vistaNuevaComunidad;
    VistaComunidad vistaComunidad;
    @BeforeAll
    static void abrirNavegador() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(1500));
    }

    @AfterAll
    static void cerrarNavegador() {
        playwright.close();
    }
    @BeforeEach
    void crearContextoYPagina() {
        context = browser.newContext(new Browser.NewContextOptions()
                .setStorageStatePath(Paths.get("sesionSpotify.json"))); // 👈 usamos sesión guardada
        Page page = context.newPage();
        vistaLogin = new VistaLogin(page);

    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }


    @Test
    void comunidadE2E() {
        Page page = vistaLogin.getPage();
        vistaLogin.darClickEnBotonSpotify();
        vistaLogin.aceptarPermisosEnSpotify();
        vistaLogin.esperarRedireccionAlHome();

        vistaHome = new VistaHome(page);

        vistaHome.darClickEnCrearNuevaComunidad();

        vistaNuevaComunidad = new VistaNuevaComunidad(page);

        vistaNuevaComunidad.escribirNombreComunidad("Comunidad de prueba");
        vistaNuevaComunidad.escribirDescripcionComunidad("Descripción de prueba para la comunidad");
        vistaNuevaComunidad.escribirSobreQuienEsLaComunidad("Duki");
        vistaNuevaComunidad.darClickEnCrearComunidad();
        vistaNuevaComunidad.esperarRedireccionAComunidad();
        String url = vistaNuevaComunidad.obtenerURLActual();

        vistaComunidad = new VistaComunidad(page,url);
        vistaComunidad.darClickEnCrearNuevaPlalList();
        vistaComunidad.escribirNombrePlaylist("Playlist de prueba");
        vistaComunidad.darClickEnSiguiente();
        vistaComunidad.buscarArtistaParaCanciones("Travis scott");
        vistaComunidad.esperarResultadosDeBusqueda();
        vistaComunidad.darClickEnLasPrimerasCanciones();
        vistaComunidad.darClickParaGuardarPlaylist();
        vistaComunidad.darClickEnEnEmpezarReproduccion();

    }




}
