package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioEstadoDeAnimoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class ServicioEstadoDeAnimoImplTest {
    
    private ServicioEstadoDeAnimoImpl servicioEstadoDeAnimoImpl;
    private RepositorioEstadoDeAnimoImpl repositorioEstadoDeAnimoImpl;
    private EstadoDeAnimo estadoDeAnimo;
    
    
    @BeforeEach
    public void setUp() {
        repositorioEstadoDeAnimoImpl = mock(RepositorioEstadoDeAnimoImpl.class);
        servicioEstadoDeAnimoImpl = new ServicioEstadoDeAnimoImpl(repositorioEstadoDeAnimoImpl);
        estadoDeAnimo = new EstadoDeAnimo("Feliz", 0.5f, 0.5f, 0.5f, 0.05f);
    }
    
    @Test
    public void queAlBuscarUnEstadoDeAnimoPorIDLoRetorne(){
        when(servicioEstadoDeAnimoImpl.obtenerEstadoDeAnimoPorId(1L)).thenReturn(estadoDeAnimo);

        EstadoDeAnimo resultado = servicioEstadoDeAnimoImpl.obtenerEstadoDeAnimoPorId(1L);

        assertNotNull(resultado);
        assertEquals("Feliz", resultado.getNombre());
    }


    @Test
    public void queAlBuscarTodosLosEstadosDeAnimoLosRetorne(){
        EstadoDeAnimo e1 = new EstadoDeAnimo("Feliz", 0.5f, 0.5f, 0.5f, 0.5f);
        EstadoDeAnimo e2 = new EstadoDeAnimo("Triste", 0.6f, 0.6f, 0.6f, 0.6f);
        List<EstadoDeAnimo> listaMock = Arrays.asList(e1, e2);

        when(repositorioEstadoDeAnimoImpl.obtenerTodosLosEstadosDeAnimo()).thenReturn(listaMock);

        List<EstadoDeAnimo> resultado = servicioEstadoDeAnimoImpl.obtenerTodosLosEstadosDeAnimo();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Feliz", resultado.get(0).getNombre());
        assertEquals("Triste", resultado.get(1).getNombre());
    }

}
