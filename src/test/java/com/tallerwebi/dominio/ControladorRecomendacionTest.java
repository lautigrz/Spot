package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.ControladorRecomendacion;
import com.tallerwebi.presentacion.dto.CancionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ControladorRecomendacionTest {

    private ServicioRecomedacionComunidad servicioRecomendacion;
    private ControladorRecomendacion controladorRecomendacion;

    @BeforeEach
    public void setUp() {
        servicioRecomendacion = mock(ServicioRecomedacionComunidad.class);
        controladorRecomendacion = new ControladorRecomendacion(servicioRecomendacion);
    }

    @Test
    public void testCancionesRecomendadasDevuelvePathCorrectoYLlamaAlServicio() {
        // Arrange
        Long idComunidad = 10L;
        Long idUsuario = 5L;

        List<CancionDto> canciones = new ArrayList<>();
        canciones.add(new CancionDto());
        canciones.add(new CancionDto());


        String resultado = controladorRecomendacion.cancionesRecomendadas(idComunidad, idUsuario, canciones);

        // Assert
        verify(servicioRecomendacion).agregarRecomendacion(canciones, idUsuario, idComunidad);
        assertThat(resultado, equalTo("/spring/comunidad/" + idComunidad));
    }



}
