package com.tallerwebi.punta_a_punta;

import com.microsoft.playwright.*;

import java.nio.file.Paths;
import java.util.Scanner;

public class GuardarSesionSpotify {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            // Ir al login de Spotify
            page.navigate("https://accounts.spotify.com/en/login");
            System.out.println("🟢 Iniciá sesión en Spotify manualmente.");

            // Detener ejecución hasta que el usuario confirme que terminó
            System.out.println("🔵 Presioná ENTER cuando termines y cierres la ventana del navegador...");
            new Scanner(System.in).nextLine();

            // Guardar el contexto
            context.storageState(new BrowserContext.StorageStateOptions()
                    .setPath(Paths.get("sesionSpotify.json")));

            System.out.println("✅ Sesión guardada correctamente en 'sesionSpotify.json'");
        }
    }
}