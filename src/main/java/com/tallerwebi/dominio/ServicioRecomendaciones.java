package com.tallerwebi.dominio;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.List;

public interface ServicioRecomendaciones {

    List<Track> generarRecomendaciones(String token) throws Exception;
}
