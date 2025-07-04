package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Mensaje;
import com.tallerwebi.dominio.RepositorioMensaje;
import com.tallerwebi.presentacion.dto.UsuarioDto;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioMensajeImpl implements RepositorioMensaje {

    @Autowired
    private SessionFactory sessionFactory;

    public RepositorioMensajeImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Mensaje obtenerMensaje(Long id) {
        String hql = "FROM Mensaje m JOIN FETCH m.usuario WHERE m.id = :id";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Mensaje.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public UsuarioDto eliminarMensaje(Long idMensaje) {
        Mensaje mensaje = obtenerMensaje(idMensaje);
        mensaje.setEstadoMensaje(false);
        sessionFactory.getCurrentSession().update(mensaje);
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setId(mensaje.getUsuario().getId());
        usuarioDto.setUrlFoto(mensaje.getUsuario().getUrlFoto());
        return usuarioDto;
    }
}
