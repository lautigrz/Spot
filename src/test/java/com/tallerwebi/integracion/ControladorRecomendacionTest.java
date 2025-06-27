package com.tallerwebi.integracion;

import com.tallerwebi.dominio.ServicioRecomedacionComunidad;
import com.tallerwebi.presentacion.ControladorRecomendacion;
import com.tallerwebi.presentacion.dto.CancionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class ControladorRecomendacionTest {

    private ServicioRecomedacionComunidad servicioRecomedacionComunidad;
    private ControladorRecomendacion controladorRecomendacion;

    @BeforeEach
    public void setUp() {
        servicioRecomedacionComunidad = mock(ServicioRecomedacionComunidad.class);
        controladorRecomendacion = new ControladorRecomendacion(servicioRecomedacionComunidad);
    }

    @Test
    public void testCancionesRecomendadas() {

        Long idComunidad = 42L;
        Long idUsuario = 7L;
        List<CancionDto> canciones = List.of(
                new CancionDto()
        );


        String resultado = controladorRecomendacion.cancionesRecomendadas(idComunidad, idUsuario, canciones);


        verify(servicioRecomedacionComunidad, times(1)).agregarRecomendacion(canciones, idUsuario, idComunidad);

        assertThat(resultado, equalTo("/spring/comunidad/" + idComunidad));
    }

}




