package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.dto.EventoDto;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServicioEventoImpl implements ServicioEvento {

    private ServicioComunidad servicioComunidad;
    private RepositorioEvento repositorioEvento;

    public ServicioEventoImpl (ServicioComunidad servicioComunidad, RepositorioEvento repositorioEvento) {
        this.servicioComunidad = servicioComunidad;
        this.repositorioEvento = repositorioEvento;
    }


    @Override
    public void publicarEvento(Evento evento, Long idComunidad) {
        Comunidad comunidad = servicioComunidad.obtenerComunidad(idComunidad);
        if (comunidad == null) {
            throw new IllegalArgumentException("Comunidad no encontrada");
        }
        evento.setComunidad(comunidad);
        repositorioEvento.agregarEvento(evento);
    }

    @Override
    public List<Evento> obtenerEventosDeLaBaseDeDatos(Long idComunidad) {
        return repositorioEvento.obetenerEventosDeLaComunidad(idComunidad);
    }


}
