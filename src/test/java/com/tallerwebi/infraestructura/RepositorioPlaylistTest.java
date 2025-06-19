package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Cancion;
import com.tallerwebi.dominio.Comunidad;
import com.tallerwebi.dominio.Playlist;
import com.tallerwebi.dominio.RepositorioPlaylist;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
@Transactional
public class RepositorioPlaylistTest {
    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioPlaylist repositorioPlaylist;

    @BeforeEach
    public void setUp() {
        repositorioPlaylist = new RepositorioPlaylistImpl(sessionFactory);

    }

    @Test
    @Rollback
    public void seDebeAgregarUnaPlaylistConCancionesEnLaBD() {

        Cancion cancion = new Cancion();
        cancion.setArtista("Billie");
        cancion.setTitulo("Ocean eyes");
        cancion.setUri("spotify:track:434fjsd");
        cancion.setSpotifyId("dfsdfds$fDAc");

        Cancion cancion1 = new Cancion();
        cancion1.setArtista("Harry");
        cancion1.setTitulo("Golden");
        cancion1.setUri("spotify:track:43da114fjsd");
        cancion1.setSpotifyId("d2fsdfds$6RTa");

        sessionFactory.getCurrentSession().save(cancion);
        sessionFactory.getCurrentSession().save(cancion1);

        List<Cancion> canciones = new ArrayList<>();
        canciones.add(cancion);
        canciones.add(cancion1);

        Playlist playlist = new Playlist();
        playlist.setNombre("Prueba");

        repositorioPlaylist.agregarPlaylist(playlist,canciones);

        String hql = "FROM Playlist WHERE id=:idPlaylist";

        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idPlaylist", playlist.getId());
        Playlist playlistBuscada = (Playlist) query.uniqueResult();

        assertThat(playlist,equalTo(playlistBuscada));
        assertThat(playlist.getCanciones(),containsInAnyOrder(cancion,cancion1));
    }

    @Test
    @Rollback
    public void seDebeAgrearUnaCancionAUnaPlaylist(){
        Cancion cancion = new Cancion();
        cancion.setArtista("Billie");
        cancion.setTitulo("Ocean eyes");
        cancion.setUri("spotify:track:434fjsd");
        cancion.setSpotifyId("dfsdfds$fDAc");

        sessionFactory.getCurrentSession().save(cancion);

        Playlist playlist = new Playlist();
        playlist.setNombre("Prueba");
        repositorioPlaylist.agregarPlaylist(playlist,new ArrayList<>());

        repositorioPlaylist.agregarCancionALaPlaylist(playlist.getId(), cancion);

        String hql = "FROM Playlist WHERE id=:idPlaylist";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idPlaylist", playlist.getId());
        Playlist playlistBuscada = (Playlist) query.uniqueResult();

        assertThat(playlistBuscada.getCanciones(),containsInAnyOrder(cancion));
        assertThat(playlistBuscada.getCanciones().iterator().next().getId(),equalTo(cancion.getId()));
    }

    @Test
    @Rollback
    public void seDebeObtenerUnaPlaylistPorId(){
        Playlist playlist = new Playlist();
        playlist.setNombre("zeta");
        repositorioPlaylist.agregarPlaylist(playlist,new ArrayList<>());

        Playlist playlistBuscada = repositorioPlaylist.obtenerPlaylist(playlist.getId());

        assertThat(playlist,equalTo(playlistBuscada));

    }

    @Test
    @Rollback
    public void seDebeObtenerLasCancionesDeUnaPlaylist(){
        Cancion cancion = new Cancion();
        cancion.setArtista("Billie");
        cancion.setTitulo("Ocean eyes");
        cancion.setUri("spotify:track:434fjsd");
        cancion.setSpotifyId("dfsdfds$fDAc");

        Cancion cancion1 = new Cancion();
        cancion1.setArtista("Harry");
        cancion1.setTitulo("Golden");
        cancion1.setUri("spotify:track:43da114fjsd");
        cancion1.setSpotifyId("d2fsdfds$6RTa");

        sessionFactory.getCurrentSession().save(cancion);
        sessionFactory.getCurrentSession().save(cancion1);

        List<Cancion> cancionSet = new ArrayList<>();
        cancionSet.add(cancion);
        cancionSet.add(cancion1);

        Playlist playlist = new Playlist();
        playlist.setNombre("billie");
        repositorioPlaylist.agregarPlaylist(playlist,cancionSet);

        List<Cancion> canciones = repositorioPlaylist.obtenerCancionesDeLaPlaylist(playlist.getId());
        assertThat(canciones,containsInAnyOrder(cancion,cancion1));
    }

    @Test
    @Rollback
    public void seDebeCrearUnaNuevaPlaylistConCancionesParaUnaComunidad(){
        Cancion cancion = new Cancion();
        cancion.setArtista("Billie");
        cancion.setTitulo("Ocean eyes");
        cancion.setUri("spotify:track:434fjsd");
        cancion.setSpotifyId("dfsdfds$fDAc");

        Cancion cancion1 = new Cancion();
        cancion1.setArtista("Harry");
        cancion1.setTitulo("Golden");
        cancion1.setUri("spotify:track:43da114fjsd");
        cancion1.setSpotifyId("d2fsdfds$6RTa");

        sessionFactory.getCurrentSession().save(cancion);
        sessionFactory.getCurrentSession().save(cancion1);

        Set<Cancion> cancionSet = new HashSet<>();
        cancionSet.add(cancion);
        cancionSet.add(cancion1);

        Comunidad comunidad = new Comunidad();
        comunidad.setDescripcion("Rock");
        comunidad.setNombre("Nioe");

        sessionFactory.getCurrentSession().save(comunidad);

        repositorioPlaylist.crearNuevaPlaylistConCanciones(comunidad,cancionSet, "d","D");

        String hql = "FROM Playlist p WHERE p.comunidad.id = :idComunidad";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idComunidad", comunidad.getId());
        Playlist playlists = (Playlist) query.uniqueResult();

        assertThat(playlists.getComunidad(),equalTo(comunidad));
        assertThat(playlists.getCanciones(),containsInAnyOrder(cancion, cancion1));


    }

    @Test
    @Rollback
    public void seDebeObtenerPlaylistsRelacionadasAUnaComunidad(){
        Comunidad comunidad = new Comunidad();
        comunidad.setDescripcion("Rock");
        comunidad.setNombre("Nioe");

        sessionFactory.getCurrentSession().save(comunidad);

        Cancion cancion = new Cancion();
        cancion.setArtista("Billie");
        cancion.setTitulo("Ocean eyes");
        cancion.setUri("spotify:track:434fjsd");
        cancion.setSpotifyId("dfsdfds$fDAc");

        Cancion cancion1 = new Cancion();
        cancion1.setArtista("Harry");
        cancion1.setTitulo("Golden");
        cancion1.setUri("spotify:track:43da114fjsd");
        cancion1.setSpotifyId("d2fsdfds$6RTa");

        sessionFactory.getCurrentSession().save(cancion);
        sessionFactory.getCurrentSession().save(cancion1);

        Set<Cancion> cancionSet = new HashSet<>();
        cancionSet.add(cancion);
        cancionSet.add(cancion1);

        repositorioPlaylist.crearNuevaPlaylistConCanciones(comunidad,cancionSet, "d","D");

        List<Playlist> playlists = repositorioPlaylist.obtenerPlaylistsRelacionadasAUnaComunidad(comunidad.getId());

        assertThat(playlists.isEmpty(),equalTo(false));
        assertThat(playlists.get(0).getCanciones(),containsInAnyOrder(cancion, cancion1));
        assertThat(comunidad.getPlaylists().get(0), equalTo(playlists.get(0)));

    }
}
