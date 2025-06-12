package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioEstadoDeAnimoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class ServicioEstadoDeAnimoImpl implements ServicioEstadoDeAnimo {

    private RepositorioEstadoDeAnimoImpl repositorioEstadoDeAnimo;

    @Autowired
    public ServicioEstadoDeAnimoImpl(RepositorioEstadoDeAnimoImpl repositorioEstadoDeAnimoImpl) {
        this.repositorioEstadoDeAnimo = repositorioEstadoDeAnimoImpl;
    }


    @Override
    public List<EstadoDeAnimo> obtenerTodosLosEstadosDeAnimo() {
        return repositorioEstadoDeAnimo.obtenerTodosLosEstadosDeAnimo();
    }

    @Override
    public EstadoDeAnimo obtenerEstadoDeAnimoPorId(Long id) {
        EstadoDeAnimo estadoDeAnimo = repositorioEstadoDeAnimo.obtenerEstadoDeAnimoPorId(id);
        if (estadoDeAnimo == null) {
            return new EstadoDeAnimo();
        }
        return estadoDeAnimo;
    }
}