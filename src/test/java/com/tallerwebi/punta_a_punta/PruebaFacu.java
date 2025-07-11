package com.tallerwebi.punta_a_punta;

import com.microsoft.playwright.*;
import com.tallerwebi.punta_a_punta.vistas.VistaHome;
import com.tallerwebi.punta_a_punta.vistas.VistaLogin;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;

public class PruebaFacu {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaLogin vistaLogin;

    @BeforeAll
    static void abrirNavegador() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
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
    void deberiaPermitirContinuarConSpotify(){
        vistaLogin.darClickEnBotonSpotify();
        String url = vistaLogin.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("accounts.spotify.com"));
    }

    @Test
    void deberiaAutenticarConSpotifyYVolverAlHome(){
        vistaLogin.darClickEnBotonSpotify();
        //vistaLogin.esperarRedireccionASpotify();
        vistaLogin.aceptarPermisosEnSpotify();
        vistaLogin.esperarRedireccionAlHome();

        String url = vistaLogin.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/spring/home"));
    }

   @Test
    void deberiaBuscarArtistaYMostrarDetalle(){
       Page page = vistaLogin.getPage();
       vistaLogin.darClickEnBotonSpotify();
       vistaLogin.aceptarPermisosEnSpotify();
       vistaLogin.esperarRedireccionAlHome();

       VistaHome vistaHome = new VistaHome(page);
       vistaHome.buscarArtista("Airbag");
       vistaHome.esperarDetalleArtista();

       String nombreArtista = vistaHome.obtenerNombreArtista();
       assertThat(nombreArtista, containsStringIgnoringCase("Airbag"));
    }

    @Test
    void deberiaAgregarAlArtistaAFavoritosYVerloEnElPerfil(){
        Page page = vistaLogin.getPage();
        vistaLogin.darClickEnBotonSpotify();
        vistaLogin.aceptarPermisosEnSpotify();
        vistaLogin.esperarRedireccionAlHome();

        VistaHome vistaHome = new VistaHome(page);
        vistaHome.buscarArtista("Airbag");
        vistaHome.esperarDetalleArtista();
        vistaHome.agregarArtistaAFavoritos();
        vistaHome.esperarRedireccionAPerfil();

        String favorito = vistaHome.obtenerPrimerFavorito();
        assertThat(favorito, containsStringIgnoringCase("Airbag"));
    }

}
