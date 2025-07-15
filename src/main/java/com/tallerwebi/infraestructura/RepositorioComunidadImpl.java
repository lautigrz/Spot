package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.presentacion.dto.ComunidadDto;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
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
        String hql = "FROM Mensaje m WHERE m.comunidad.id = :id AND m.estadoMensaje = true ORDER BY m.id ASC";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Mensaje.class)
                .setParameter("id", id)
                .getResultList();
    }
    @Override
    public List<Comunidad> obtenerComunidades() {
        String hql = "FROM Comunidad c WHERE c.preescucha IS NULL ORDER BY c.id DESC";
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

    @Override
    public Long crearComunidadParaUnaPreescucha(Preescucha preescucha) {
        Comunidad comunidad = new Comunidad();
        comunidad.setNombre(preescucha.getTitulo());
        comunidad.setDescripcion("Comunidad creada para la preescucha de " + preescucha.getArtista().getNombre());
        comunidad.setUrlFoto(preescucha.getPreescuchaFotoUrl());
        comunidad.setUrlPortada(preescucha.getPreescuchaFotoUrl());
        comunidad.setPreescucha(preescucha);
        comunidad.setHost(preescucha.getArtista());

        preescucha.setComunidad(comunidad);
        preescucha.getArtista().getComunidades().add(comunidad);
        sessionFactory.getCurrentSession().save(comunidad);

        return comunidad.getId();
    }

    @Override
    public Boolean obtenerComunidadDeArtista(Long idComunidad, Long idArtista) {
        String hql = "FROM Comunidad c WHERE c.host.id = :idArtista AND c.id = :idComunidad";
        TypedQuery<Comunidad> query = sessionFactory.getCurrentSession().createQuery(hql, Comunidad.class);
        query.setParameter("idArtista", idArtista);
        query.setParameter("idComunidad", idComunidad);
        Comunidad comunidad = query.getSingleResult();

        return comunidad != null;
    }

    @Override
    public Comunidad obtenerComuniadDePreescucha(Long idPreescucha) {
        String hql = "FROM Comunidad c WHERE c.preescucha.id = :idPreescucha";
        TypedQuery<Comunidad> query = sessionFactory.getCurrentSession().createQuery(hql, Comunidad.class);
        query.setParameter("idPreescucha", idPreescucha);

        return query.getSingleResult();
    }


}



