package com.tallerwebi.integracion;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallerwebi.dominio.ServicioNotificacion;
import com.tallerwebi.presentacion.ControladorNotificacion;
import com.tallerwebi.presentacion.dto.NotificacionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ControladorNotificacionTest {
    private MockMvc mockMvc;
    private ServicioNotificacion servicioNotificacion;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        servicioNotificacion = Mockito.mock(ServicioNotificacion.class);
        ControladorNotificacion controlador = new ControladorNotificacion(servicioNotificacion);
        mockMvc = MockMvcBuilders.standaloneSetup(controlador).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void obtenerNotificacionesPorUsuario_deberiaRetornarLista() throws Exception {
        NotificacionDto dto1 = new NotificacionDto();
        dto1.setId(1L);
        dto1.setMensaje("Mensaje 1");

        NotificacionDto dto2 = new NotificacionDto();
        dto2.setId(2L);
        dto2.setMensaje("Mensaje 2");

        List<NotificacionDto> lista = List.of(dto1, dto2);

        when(servicioNotificacion.obtenerNotificacionesPorUsuario(anyLong())).thenReturn(lista);

        MvcResult mvcResult = mockMvc.perform(get("/notificaciones/123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonRespuesta = mvcResult.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        List<NotificacionDto> listaRespuesta = mapper.readValue(
                jsonRespuesta,
                new TypeReference<List<NotificacionDto>>() {}
        );

        assertThat(listaRespuesta.size(), equalTo(2));
        assertThat(listaRespuesta.get(0).getId(), equalTo(1L));
        assertThat(listaRespuesta.get(0).getMensaje(), equalTo("Mensaje 1"));
        assertThat(listaRespuesta.get(1).getId(), equalTo(2L));
        assertThat(listaRespuesta.get(1).getMensaje(), equalTo("Mensaje 2"));

        verify(servicioNotificacion).obtenerNotificacionesPorUsuario(123L);
    }


    @Test
    public void cambiarEstadoNotificacion_deberiaResponderConOk() throws Exception {
        List<Long> ids = List.of(1L, 2L, 3L);

        String json = objectMapper.writeValueAsString(ids);

        mockMvc.perform(post("/notificacion/leer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(servicioNotificacion).cambiarEstadoNotificacion(ids);
    }
}
