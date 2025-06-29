package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.EventoDto;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ServicioEventoCombinadoImpl implements ServicioEventoCombinado {

    private ServicioEventoResponse servicioEventoResponse;
    private RepositorioEvento repositorioEvento;

    public ServicioEventoCombinadoImpl(ServicioEventoResponse servicioEventoResponse, RepositorioEvento repositorioEvento) {
        this.servicioEventoResponse = servicioEventoResponse;
        this.repositorioEvento = repositorioEvento;
    }

    @Override
    public List<EventoDto> obtenerEventos(String artista, Long idComunidad) {
        List<EventoDto> resultado = new ArrayList<>();

        // 1. Eventos locales (de BD)
        List<Evento> eventosLocales = repositorioEvento.obetenerEventosDeLaComunidad(idComunidad); // o lo que uses
        for (Evento e : eventosLocales) {
            resultado.add(mapearEventoEntidad(e));
        }

        // 2. Eventos de API
        List<EventoResponse.Evento> eventosApi = servicioEventoResponse.eventosobtenerEventosDeLaApi(artista);
        for (EventoResponse.Evento e : eventosApi) {
            System.out.println("API:" + e.getName());
            resultado.add(mapearEventoApi(e));
        }

        return resultado;
    }


    private EventoDto mapearEventoEntidad(Evento evento) {
        EventoDto dto = new EventoDto();
        dto.setNombre(evento.getNombre());
        dto.setFecha(evento.getFecha().toString());
        dto.setLugar(evento.getLugar());
        dto.setCiudad(evento.getCiudad());
        dto.setPais(evento.getPais());
        dto.setImagen(evento.getImagen());
        dto.setUrl(evento.getUrl());
        return dto;
    }

    private EventoDto mapearEventoApi(EventoResponse.Evento apiEvento) {
        EventoDto dto = new EventoDto();
        dto.setNombre(apiEvento.getName());
        dto.setFecha(apiEvento.getDates().getStart().getLocalDate());
        dto.setUrl(apiEvento.getUrl());


        if (apiEvento.get_embedded() != null &&
                apiEvento.get_embedded().getVenues() != null &&
                !apiEvento.get_embedded().getVenues().isEmpty()) {

            var venue = apiEvento.get_embedded().getVenues().get(0);

            dto.setLugar(
                    venue.getName() != null ? venue.getName() : "Lugar desconocido"
            );

            dto.setCiudad(
                    venue.getCity() != null && venue.getCity().getName() != null
                            ? venue.getCity().getName()
                            : "Ciudad desconocida"
            );

            dto.setPais(
                    venue.getCountry() != null && venue.getCountry().getName() != null
                            ? venue.getCountry().getName()
                            : "País desconocido"
            );

        } else {
            dto.setLugar("Lugar desconocido");
            dto.setCiudad("Ciudad desconocida");
            dto.setPais("País desconocido");
        }

        String imagen = apiEvento.getImages().isEmpty() ? null : apiEvento.getImages().get(0).getUrl();
        dto.setImagen(imagen != null ? imagen : "https://via.placeholder.com/400x200?text=Sin+imagen");

        return dto;
    }
}
