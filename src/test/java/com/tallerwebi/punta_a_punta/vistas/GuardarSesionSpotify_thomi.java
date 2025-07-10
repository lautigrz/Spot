package com.tallerwebi.punta_a_punta.vistas;
import com.microsoft.playwright.*;
import java.nio.file.Paths;
import java.util.Scanner;

public class GuardarSesionSpotify_thomi {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(false)
                            .setSlowMo(50)
            );

            BrowserContext context = browser.newContext();
            Page page = context.newPage();


            page.navigate("https://accounts.spotify.com/en/login");
            System.out.println("🟢 Iniciá sesión manualmente en Spotify...");


            System.out.println("⏳ Presioná ENTER cuando termines y cierres la ventana del navegador.");
            new Scanner(System.in).nextLine();


            context.storageState(new BrowserContext.StorageStateOptions()
                    .setPath(Paths.get("sesionSpotify.json")));

            System.out.println("✅ Sesión guardada correctamente en 'sesionSpotify.json'");
        }
    }
}