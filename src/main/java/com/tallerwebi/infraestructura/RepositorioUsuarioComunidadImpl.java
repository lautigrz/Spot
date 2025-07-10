package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.presentacion.dto.UsuarioDto;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RepositorioUsuarioComunidadImpl implements RepositorioUsuarioComunidad {
    @Autowired
    private SessionFactory sessionFactory;

    public RepositorioUsuarioComunidadImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Boolean agregarUsuarioAComunidad(Usuario usuario, Comunidad comunidad, String rol) {

        Usuario usuarioPersistente =  sessionFactory.getCurrentSession().get(Usuario.class, usuario.getId()); // Cargar usuario persistente
        Comunidad comunidadPersistente =  sessionFactory.getCurrentSession().get(Comunidad.class, comunidad.getId());
        UsuarioComunidad usuarioComunidad = new UsuarioComunidad();
        usuarioComunidad.setUsuario(usuario);
        usuarioComunidad.setComunidad(comunidad);
        usuarioComunidad.setRol(rol);

        usuarioPersistente.getComunidades().add(usuarioComunidad);
        comunidadPersistente.getUsuarios().add(usuarioComunidad);

        sessionFactory.getCurrentSession().save(usuarioComunidad);
        return true;
    }



    @Override
    public String obtenerRolDelUsuarioEnComunidad(Long idUsuario, Long idComunidad) {
        String hql = "FROM UsuarioComunidad uc WHERE uc.usuario.id = :idUsuario AND uc.comunidad.id = :idComunidad";
        Query <UsuarioComunidad> query = sessionFactory.getCurrentSession().createQuery(hql, UsuarioComunidad.class);
        query.setParameter("idUsuario", idUsuario);
        query.setParameter("idComunidad", idComunidad);
        return query.uniqueResult().getRol();
    }

    @Override
    public UsuarioComunidad obtenerUsuarioEnComunidad(Long idUsuario, Long idComunidad) {
        String hql = "FROM UsuarioComunidad uc WHERE uc.usuario.id = :idUsuario AND uc.comunidad.id = :idComunidad";
        Query<UsuarioComunidad> query = sessionFactory.getCurrentSession().createQuery(hql, UsuarioComunidad.class);
        query.setParameter("idUsuario", idUsuario);
        query.setParameter("idComunidad", idComunidad);
        return query.uniqueResult();
    }

    @Override
    public List<UsuarioDto> obtenerUsuariosDeLaComunidad(Long idComunidad) {

        String hql = "FROM UsuarioComunidad uc JOIN uc.usuario u WHERE uc.comunidad.id = :idComunidad";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idComunidad", idComunidad);
        List<UsuarioComunidad> usuariosComunidad = query.getResultList();
        List<UsuarioDto> usuariosDto = new ArrayList<>();

        for(UsuarioComunidad usuarioComunidad : usuariosComunidad) {
            UsuarioDto usuarioDto = new UsuarioDto();
            usuarioDto.setId(usuarioComunidad.getUsuario().getId());
            usuarioDto.setUser(usuarioComunidad.getUsuario().getUser());

            usuariosDto.add(usuarioDto);

        }

        return usuariosDto;
    }

    @Override
    public void actualizar(UsuarioComunidad usuarioComunidad) {
        sessionFactory.getCurrentSession().update(usuarioComunidad);
    }

    @Override
    public Boolean eliminarUsuarioDeComunidad(Long idUsuario, Long idComunidad) {
        String hql = "DELETE FROM UsuarioComunidad uc WHERE uc.usuario.id = :idUsuario AND uc.comunidad.id = :idComunidad";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idUsuario", idUsuario);
        query.setParameter("idComunidad", idComunidad);
        int result = query.executeUpdate();

        return result > 0;
    }

    @Override
    public UsuarioComunidad obtenerUsuarioPorNombreEnComunidad(String usuario, Long idComunidad) {
        String hql = "FROM UsuarioComunidad uc WHERE uc.usuario.user = :usuario AND uc.comunidad.id = :idComunidad";
        Query<UsuarioComunidad> query = sessionFactory.getCurrentSession().createQuery(hql, UsuarioComunidad.class);
        query.setParameter("usuario", usuario);
        query.setParameter("idComunidad", idComunidad);
        UsuarioComunidad resultado = query.uniqueResult();

        return resultado;
    }

    @Override
    public List<Comunidad> obtenerComunidadesDondeElUsuarioEsteUnido(Usuario usuario) {

        String hql = "SELECT uc.comunidad FROM UsuarioComunidad uc WHERE uc.usuario.id = :idUsuario";
        Query<Comunidad> query = sessionFactory.getCurrentSession().createQuery(hql, Comunidad.class);
        query.setParameter("idUsuario", usuario.getId());
        List<Comunidad> comunidades = query.getResultList();

        return comunidades;
    }

    @Override
    public void compartirPosteoEnComunidad(Post post, List<Comunidad> comunidades, Usuario usuario) {

        if (post == null || usuario == null) {
            throw new IllegalArgumentException("Post y usuario no pueden ser nulos.");
        }

        if (comunidades == null || comunidades.isEmpty()) {
            throw new IllegalArgumentException("Debe especificar al menos una comunidad para compartir el post.");
        }

        for (Comunidad comunidad : comunidades) {
            Mensaje mensaje = new Mensaje();
            mensaje.setComunidad(comunidad);
            mensaje.setPostCompartido(post);
            mensaje.setUsuario(usuario);
            mensaje.setEstadoMensaje(true);

            sessionFactory.getCurrentSession().save(mensaje);
        }
    }
}
