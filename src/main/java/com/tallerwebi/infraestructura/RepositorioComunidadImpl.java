package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.presentacion.dto.ComunidadDto;
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
    public Long guardarMensajeDeLaComunidad(String contenido, Comunidad comunidad,Usuario usuario) {

        Mensaje mensaje = new Mensaje();
        mensaje.setTexto(contenido);
        mensaje.setUsuario(usuario);
        mensaje.setEstadoMensaje(true);
        comunidad.agregarMensaje(mensaje);

        sessionFactory.getCurrentSession().save(mensaje);

       return mensaje.getId();
    }

    @Override
    public List<Mensaje> obtenerMensajesDeComunidad(Long id) {
        Comunidad comunidad = obtenerComunidad(id);
        comunidad.getMensajes().size();  // forzamos la carga
        return comunidad.getMensajes();
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

    @Override
    public String obtenerTokenDelUsuarioQuePerteneceAUnaComunidad(String user, Long idComunidad) {

        String hql = "SELECT u.token FROM UsuarioComunidad uc JOIN uc.usuario u WHERE uc.comunidad.id = :idComunidad AND u.user = :username";

        return sessionFactory.getCurrentSession()
                .createQuery(hql, String.class)
                .setParameter("username", user)
                .setParameter("idComunidad", idComunidad)
                .uniqueResult();

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
        String hql = "SELECT uc.usuario FROM Comunidad c JOIN c.usuarios uc WHERE c.id = :idComunidad";
        TypedQuery<Usuario> query = sessionFactory.getCurrentSession().createQuery(hql, Usuario.class);
        query.setParameter("idComunidad", idComunidad);
        return query.getResultList();
    }

    @Override
    public List<Comunidad> buscarComunidadesPorNombre(String nombreComunidad) {
        String hql = "FROM Comunidad c WHERE c.nombre LIKE :nombreComunidad OR c.artista LIKE :nombreComunidad";
        Query<Comunidad> query = sessionFactory.getCurrentSession()
                .createQuery(hql, Comunidad.class);
        query.setParameter("nombreComunidad", "%" + nombreComunidad + "%");

        return query.getResultList();
    }


}
