package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ServicioAdminTest {
    private ServicioAdmin servicioAdmin;
    private RepositorioAdmin repositorioAdminMock;

    @BeforeEach
    public void setUp() {

        repositorioAdminMock = mock(RepositorioAdmin.class);
        servicioAdmin = new ServicioAdminImpl(repositorioAdminMock);
    }

    @Test
    public void seDebeHacerAdminAUnMiembro() {

            Long idComunidad = 1L;
            Long idMiembro = 2L;

            servicioAdmin.hacerAdminAUnMiembro(idComunidad, idMiembro);

            verify(repositorioAdminMock).hacerAdminAUnMiembro(idComunidad, idMiembro);

    }

    @Test
    public void seDebeEliminarMiembroDeComunidad() {

            Long idComunidad = 1L;
            Long idMiembro = 2L;

            servicioAdmin.eliminarMiembroDeComunidad(idComunidad, idMiembro);

            verify(repositorioAdminMock).eliminarMiembroDeComunidad(idComunidad, idMiembro);

    }
}
