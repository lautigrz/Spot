package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Cancion;
import com.tallerwebi.dominio.Comunidad;
import com.tallerwebi.dominio.Playlist;
import com.tallerwebi.dominio.RepositorioPlaylist;
import com.tallerwebi.presentacion.dto.CancionDto;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class RepositorioPlaylistImpl implements RepositorioPlaylist {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioPlaylistImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void agregarPlaylist(Playlist playlist, List<Cancion> canciones) {
        for (Cancion cancion : canciones) {
            if (cancion != null) {
                playlist.getCanciones().add(cancion);
                cancion.getPlaylists().add(playlist);
            }
        }
        sessionFactory.getCurrentSession().saveOrUpdate(playlist);
    }

    @Override
    public void agregarCancionALaPlaylist(Long id, Cancion cancion) {
        Playlist playlist = obtenerPlaylist(id);

        playlist.getCanciones().add(cancion);
        cancion.getPlaylists().add(playlist);

        sessionFactory.getCurrentSession().saveOrUpdate(playlist);
    }

    @Override
    public void eliminarCancionALaPlaylist(Long id, Cancion cancion) {

        Playlist playlist = obtenerPlaylist(id);
        playlist.getCanciones().remove(cancion);
    }

    @Override
    public List<Playlist> obtenerPlaylistsRelacionadasAUnaComunidad(Long idComuniadad) {

        String hql = "from Playlist where comunidad.id = :idComuniadad";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idComuniadad", idComuniadad);

        List<Playlist> playlists = query.list();
        return playlists;
    }

    @Override
    public List<Cancion> obtenerCancionesDeLaPlaylist(Long id) {

        String hql = "SELECT c FROM Playlist p JOIN p.canciones c WHERE p.id = :id";
        Query<Cancion> query = sessionFactory.getCurrentSession().createQuery(hql, Cancion.class);
        query.setParameter("id", id);
        List<Cancion> canciones = query.list();

        return canciones;
    }

    @Override
    public Playlist obtenerPlaylist(Long idPlaylist) {
        return sessionFactory.getCurrentSession().get(Playlist.class, idPlaylist);
    }

    @Override
    public void crearNuevaPlaylistConCanciones(Comunidad comunidad, Set<Cancion> canciones,String nombre, String urlImagen) {

        Comunidad comunidadManaged = sessionFactory.getCurrentSession().get(Comunidad.class, comunidad.getId());

        Playlist playlist = new Playlist();
        playlist.setNombre(nombre);
        playlist.setComunidad(comunidad);
        playlist.setUrlImagen(urlImagen);
        playlist.agregarCanciones(canciones);
        comunidadManaged.agregarPlaylist(playlist);

        sessionFactory.getCurrentSession().saveOrUpdate(playlist);
    }
}
