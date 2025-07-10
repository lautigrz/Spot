package com.tallerwebi.punta_a_punta;
import com.microsoft.playwright.*;
import com.tallerwebi.punta_a_punta.vistas.VistaLogin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.file.Paths;
import static junit.framework.Assert.assertEquals;
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
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(100));
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
    void crearContextoPagina() {
        context = browser.newContext(
                new Browser.NewContextOptions()
                        .setStorageStatePath(Paths.get("sesionSpotify.json"))
        );

        Page page = context.newPage();
        vistaLogin = new VistaLogin(page);
    }

    @Test
    void deberiaRedirigirAlHomeLuegoDeAceptarAutenticacionConSpotify(){
        vistaLogin.darClickEnContinuarConSpotify();
        vistaLogin.darClickEnAceptarAlSerRedirigidoAlAutenticadorDeSpotify();
        vistaLogin.waitTimeOut();
        String url = vistaLogin.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/spring/home"));
    }

    @Test
    void deberiaRedirigirAlPerfilLuegoDeHacerClickEnElBotonDePerfil(){
        vistaLogin.darClickEnContinuarConSpotify();
        vistaLogin.darClickEnAceptarAlSerRedirigidoAlAutenticadorDeSpotify();
        vistaLogin.waitTimeOut();
        vistaLogin.darClickEnBotonPerfil();
        vistaLogin.waitTimeOut();
        String url = vistaLogin.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/spring/perfil"));
    }

    @Test
    void deberiaVisualizarElEstadoDeAnimoEnElPerfilAlElegirUno(){
        String selectEstadoAnimo = "#estadoDeAnimoId";
        String estadoDeAnimo = "Feliz";
        vistaLogin.darClickEnContinuarConSpotify();
        vistaLogin.darClickEnAceptarAlSerRedirigidoAlAutenticadorDeSpotify();
        vistaLogin.waitTimeOut();
        vistaLogin.darClickEnBotonPerfil();
        vistaLogin.waitTimeOut();
        vistaLogin.seleccionarEstadoDeAnimo(selectEstadoAnimo, estadoDeAnimo);
        vistaLogin.actualizarEstadoDeAnimo();
        String texto = vistaLogin.obtenerTextoDeLaBarraDeEstadoDeAnimo();
        assertEquals(estadoDeAnimo, texto);
    }

    @Test
    void deberiaPoderComprarPreEscuchaDeUnArtista(){
        String artista = "Duki";
        String selector = "input[name='nombre']";
        String usuarioMp = "TESTUSER852556331";
        String passMp = "HWRUtNsojZ";
        String selectorMpUsuario = "#user_id";
        String selectorMpPass = "#password";
        String codigoSeguridad = "123";
        String selectorCodigoSeguridad = "input[name='securityCode']";

        vistaLogin.hacerClickEnVisitSite();
        vistaLogin.darClickEnContinuarConSpotify();
        vistaLogin.darClickEnAceptarAlSerRedirigidoAlAutenticadorDeSpotify();
        vistaLogin.waitTimeOut();
        vistaLogin.buscarArtista(selector, artista);
        vistaLogin.waitTimeOut();
        vistaLogin.hacerClickEnComprarPreescucha();
        vistaLogin.hacerClickEnIngresarAMercadoPago();
        vistaLogin.waitTimeOut();
        vistaLogin.ingresarUsuarioMercadoPago(selectorMpUsuario, usuarioMp);
        vistaLogin.hacerClickEnContinuar();
        vistaLogin.waitTimeOut();
        vistaLogin.ingresarPassMercadoPago(selectorMpPass, passMp);
        vistaLogin.hacerClickEnIniciarSesion();
        vistaLogin.waitTimeOut();
        vistaLogin.hacerClickEnSeleccionCuotas();
        vistaLogin.hacerClickEnCantidadCuotas();
        vistaLogin.cambiarFocoAInputYCompletarCodigoSeguridad(selectorCodigoSeguridad, codigoSeguridad);
        vistaLogin.waitTimeOut();
        vistaLogin.hacerClickEnPagar();
        vistaLogin.waitTimeOut();
        vistaLogin.waitTimeOut();
        vistaLogin.hacerClickEnVolverAlPerfil();
        assertThat(vistaLogin.obtenerURLActual(), containsStringIgnoringCase("/spring/perfil"));
    }
}