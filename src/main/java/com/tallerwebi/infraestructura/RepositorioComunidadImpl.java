package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Set;

@Repository
public class RepositorioComunidadImpl implements RepositorioComunidad {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioComunidadImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardarMensajeDeLaComunidad(String contenido, Long idUsuario,Long idComunidad) {

        Usuario usuario = obtenerUsuarioEnComunidad(idUsuario,idComunidad);
        Comunidad comunidad = obtenerComunidad(idComunidad);

        Mensaje mensaje = new Mensaje();
        mensaje.setTexto(contenido);
        mensaje.setUsuario(usuario);

        comunidad.agregarMensaje(mensaje);

        sessionFactory.getCurrentSession().save(mensaje);
    }

    @Override
    public List<Mensaje> obtenerMensajesDeComunidad(Long id) {
        Comunidad comunidad = obtenerComunidad(id);
        comunidad.getMensajes().size();  // forzamos la carga
        return comunidad.getMensajes();
    }

    @Override
    public void guardarNuevaComunidad(Comunidad comunidad) {
        sessionFactory.getCurrentSession().save(comunidad);
    }

    @Override
    public List<Comunidad> obtenerComunidades() {
        String hql = "FROM Comunidad";
        TypedQuery<Comunidad> query = sessionFactory.getCurrentSession().createQuery(hql, Comunidad.class);
        return query.getResultList();
    }

    @Override
    public Comunidad obtenerComunidad(Long id) {
        return sessionFactory.getCurrentSession().get(Comunidad.class, id);
    }

    //falta test
    @Override
    public String obtenerTokenDelUsuarioQuePerteneceAUnaComunidad(String user, Long idComunidad) {
        String hql = "SELECT u.token FROM Usuario u " +
                "JOIN u.comunidades c " +
                "WHERE u.user = :username AND c.id = :idComunidad";

        return sessionFactory.getCurrentSession()
                .createQuery(hql, String.class)
                .setParameter("username", user)
                .setParameter("idComunidad", idComunidad)
                .uniqueResult();

    }

    @Override
    public Boolean guardarUsuarioEnComunidad(Usuario usuario, Long idComunidad) {
        try {
            Comunidad comunidad = obtenerComunidad(idComunidad);
            if (comunidad == null) return false;

            usuario.getComunidades().add(comunidad);
            comunidad.getUsuarios().add(usuario);

            sessionFactory.getCurrentSession().saveOrUpdate(comunidad);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Usuario obtenerUsuarioEnComunidad(Long idUsuario,Long idComunidad) {

        Comunidad comunidad = obtenerComunidadConUsuarios(idComunidad);

        if (comunidad == null) {
            return null;
        }

        for (Usuario usuario : comunidad.getUsuarios()) {
            if (usuario.getId().equals(idUsuario)) return usuario;
        }

        return null;
    }

    @Override
    public Comunidad obtenerComunidadConUsuarios(Long idComunidad) {

        String hql = "FROM Comunidad c JOIN FETCH c.usuarios WHERE c.id = :idComunidad";

        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idComunidad", idComunidad);

        Comunidad comunidad = (Comunidad) query.uniqueResult();

        return comunidad;
    }
    @Override
    public List<Playlist> obtenerPlaylistsPorComunidadId(Long comunidadId) {
        String hql = "SELECT p FROM Comunidad c JOIN c.playlists p WHERE c.id = :idComunidad";
        TypedQuery<Playlist> query = sessionFactory.getCurrentSession().createQuery(hql, Playlist.class);
        query.setParameter("idComunidad", comunidadId);
        List<Playlist> playlists = query.getResultList();

        return playlists;
    }

    @Override
    public Set<Cancion> obtenerCancionesDeUnaPlaylistDeUnaComunidad(Long idComunidad) {
        String hql = "SELECT p FROM Comunidad c JOIN c.playlists p WHERE c.id = :idComunidad";
        TypedQuery<Playlist> query = sessionFactory.getCurrentSession().createQuery(hql, Playlist.class);
        query.setParameter("idComunidad", idComunidad);
        List<Playlist> playlists = query.getResultList();

        Playlist playlist = playlists.get(0);

        return playlist.getCanciones();
    }

    @Override
    public Playlist obtenerPlaylistDeUnaComunidad(Long idComunidad) {
        String hql = "SELECT p FROM Comunidad c JOIN c.playlists p WHERE c.id = :idComunidad";
        TypedQuery<Playlist> query = sessionFactory.getCurrentSession().createQuery(hql, Playlist.class);
        query.setParameter("idComunidad", idComunidad);
        List<Playlist> playlists = query.getResultList();

        return playlists.get(0);
    }

    @Override
    public List<Usuario> obtenerUsuariosPorComunidad(Long idComunidad) {
        String hql = "SELECT u FROM Comunidad c JOIN c.usuarios u WHERE c.id = :idComunidad";
        TypedQuery<Usuario> query = sessionFactory.getCurrentSession().createQuery(hql, Usuario.class);
        query.setParameter("idComunidad", idComunidad);
        return query.getResultList();
    }


}
