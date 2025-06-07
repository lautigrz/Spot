package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioEstadoDeAnimoImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioEstadoDeAnimoImpl implements ServicioEstadoDeAnimo {

    private RepositorioEstadoDeAnimoImpl repositorioEstadoDeAnimo;

    public ServicioEstadoDeAnimoImpl(RepositorioEstadoDeAnimoImpl repositorioEstadoDeAnimoImpl) {
        this.repositorioEstadoDeAnimo = repositorioEstadoDeAnimoImpl;
    }


    @Override
    public List<EstadoDeAnimo> obtenerTodosLosEstadosDeAnimo(){
    return repositorioEstadoDeAnimo.obtenerTodosLosEstadosDeAnimo();
    }

    @Override
    public EstadoDeAnimo obtenerEstadoDeAnimoPorId(Long id){
    return repositorioEstadoDeAnimo.obtenerEstadoDeAnimoPorId(id);
    }


}
