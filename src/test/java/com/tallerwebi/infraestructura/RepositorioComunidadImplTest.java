package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
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

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
@Transactional
public class RepositorioComunidadImplTest {

    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioComunidad repositorioComunidad;

    private RepositorioPlaylist repositorioPlaylist;


    @BeforeEach
    public void setUp() {
        repositorioComunidad = new RepositorioComunidadImpl(sessionFactory);
        repositorioPlaylist = new RepositorioPlaylistImpl(sessionFactory);
    }


    @Test
    @Rollback
    public void seDebeGuardarElMensajeEnLaBDCorrespondienteAUnaComunidad() {

        Usuario usuario = new Usuario();
        usuario.setToken("223");
        usuario.setUrlFoto("https://");
        usuario.setUser("lauti");
        usuario.setRefreshToken("das2");
        sessionFactory.getCurrentSession().save(usuario);

        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Rock");
        comunidad.setDescripcion("excelente");
        sessionFactory.getCurrentSession().save(comunidad);

        String textoMensaje = "Hola como estas";

        repositorioComunidad.guardarUsuarioEnComunidad(usuario,comunidad.getId());

        repositorioComunidad.guardarMensajeDeLaComunidad(textoMensaje, usuario.getId(), comunidad.getId());


        Comunidad comunidadObtenida = sessionFactory.getCurrentSession()
                .get(Comunidad.class, comunidad.getId());


        List<Mensaje> mensajes = comunidadObtenida.getMensajes();
        assertThat(mensajes.size(), equalTo(1));
        assertThat(mensajes.get(0).getTexto(), equalTo(textoMensaje));
        assertThat(mensajes.get(0).getUsuario().getId(), equalTo(usuario.getId()));
    }

    @Test
    @Rollback
    public void seDebeObtenerUnaListaDeMensajesDeUnaComunidad() {
        Usuario usuario1 = new Usuario();
        usuario1.setToken("223");
        usuario1.setUrlFoto("https://");
        usuario1.setUser("lauti");
        usuario1.setRefreshToken("das2");

        sessionFactory.getCurrentSession().save(usuario1);

        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Rock");
        comunidad.setDescripcion("excelente");
        sessionFactory.getCurrentSession().save(comunidad);

        repositorioComunidad.guardarUsuarioEnComunidad(usuario1,comunidad.getId());

        String textoMensaje = "Hola como estas";
        String textoMensaje2 = "Muy bien";

        List<String> mensajes = new ArrayList<>();
        mensajes.add(textoMensaje);
        mensajes.add(textoMensaje2);

        repositorioComunidad.guardarMensajeDeLaComunidad(textoMensaje, usuario1.getId(), comunidad.getId());
        repositorioComunidad.guardarMensajeDeLaComunidad(textoMensaje2, usuario1.getId(), comunidad.getId());

        List<Mensaje> mensajesDeComunidad = repositorioComunidad.obtenerMensajesDeComunidad(comunidad.getId());

        assertThat(mensajesDeComunidad.size(), equalTo(mensajes.size()));

        for (int i = 0; i < mensajes.size(); i++) {
            assertThat(mensajesDeComunidad.get(i).getTexto(), equalTo(mensajes.get(i)));
        }

    }

    @Test
    @Rollback
    public void seDebeGuardarUnaComunidad() {
        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Rock");
        comunidad.setDescripcion("excelente");
       repositorioComunidad.guardarNuevaComunidad(comunidad);

        String hql = "FROM Comunidad";

        Query query = sessionFactory.getCurrentSession().createQuery(hql);

        Comunidad comunidad2 = (Comunidad) query.uniqueResult();

        assertThat(comunidad2.getNombre(), equalTo(comunidad.getNombre()));
        assertThat(comunidad2.getDescripcion(), equalTo(comunidad.getDescripcion()));
        assertThat(comunidad2.getId(), equalTo(comunidad.getId()));
    }

    @Test
    @Rollback
    public void seDebeObtenerLasComunidades(){
        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Rock");
        comunidad.setDescripcion("excelente");
        sessionFactory.getCurrentSession().save(comunidad);

        Comunidad comunidad2 = new Comunidad();
        comunidad2.setNombre("Cumbia");
        comunidad2.setDescripcion("excelente");

        sessionFactory.getCurrentSession().save(comunidad2);


        List<Comunidad> comunidades = repositorioComunidad.obtenerComunidades();

        assertThat(comunidades.size(), equalTo(2));
        assertThat(comunidades.get(0).getId(), equalTo(comunidad.getId()));
        assertThat(comunidades.get(1).getId(), equalTo(comunidad2.getId()));
    }

    @Test
    @Rollback
    public void seDebeObtenerUnaComunidadPorId() {
        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Rock");
        comunidad.setDescripcion("excelente");
        sessionFactory.getCurrentSession().save(comunidad);

        Comunidad comunidad2 = new Comunidad();
        comunidad2.setNombre("Cumbia");
        comunidad2.setDescripcion("excelente");

        sessionFactory.getCurrentSession().save(comunidad2);

        Comunidad comunidadObtenida = repositorioComunidad.obtenerComunidad(comunidad.getId());

        assertThat(comunidadObtenida.getId(), equalTo(comunidad.getId()));
        assertThat(comunidadObtenida, equalTo(comunidad));
    }


    @Test
    @Rollback
    public void seDebeGuardarUnUsuarioEnLaComunidad() {
        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Rock");
        comunidad.setDescripcion("excelente");
        sessionFactory.getCurrentSession().save(comunidad);

        Usuario usuario = new Usuario();
        usuario.setToken("223");
        usuario.setUrlFoto("https://");
        usuario.setUser("lauti");
        usuario.setRefreshToken("das2");
        sessionFactory.getCurrentSession().save(usuario);

        Boolean seGuardo = repositorioComunidad.guardarUsuarioEnComunidad(usuario, comunidad.getId());

        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();

        Usuario usuarioEnComunidad = repositorioComunidad.obtenerUsuarioEnComunidad(usuario.getId(),comunidad.getId());

        assertThat(usuarioEnComunidad.getId(), equalTo(usuario.getId()));
        assertThat(seGuardo, equalTo(true));

    }

    @Test
    @Rollback
    public void seDebeObtenerUnUsuarioDeUnaComunidad() {
        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Rock");
        comunidad.setDescripcion("excelente");
        sessionFactory.getCurrentSession().save(comunidad);

        Usuario usuario = new Usuario();
        usuario.setToken("223");
        usuario.setUrlFoto("https://");
        usuario.setUser("lauti");
        usuario.setRefreshToken("das2");
        sessionFactory.getCurrentSession().save(usuario);

        repositorioComunidad.guardarUsuarioEnComunidad(usuario, comunidad.getId());

        Usuario usuarioBuscado = repositorioComunidad.obtenerUsuarioEnComunidad(usuario.getId(), comunidad.getId());

        assertThat(usuarioBuscado.getId(),equalTo(usuario.getId()));
    }

    @Test
    @Rollback
    public void seDebeObtenerUnaComunidadConUsuarios(){
        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Rock");
        comunidad.setDescripcion("excelente");
        sessionFactory.getCurrentSession().save(comunidad);

        Usuario usuario = new Usuario();
        usuario.setToken("223");
        usuario.setUrlFoto("https://");
        usuario.setUser("lauti");
        usuario.setRefreshToken("das2");
        sessionFactory.getCurrentSession().save(usuario);

        Usuario usuario1 = new Usuario();
        usuario1.setToken("223");
        usuario1.setUrlFoto("https://");
        usuario1.setUser("lauti");
        usuario1.setRefreshToken("das2");
        sessionFactory.getCurrentSession().save(usuario1);

        List<Usuario> usuarios = List.of(usuario, usuario1);

        repositorioComunidad.guardarUsuarioEnComunidad(usuario, comunidad.getId());
        repositorioComunidad.guardarUsuarioEnComunidad(usuario1, comunidad.getId());

        Comunidad comunidadObtenida = repositorioComunidad.obtenerComunidadConUsuarios(comunidad.getId());
        assertThat(comunidadObtenida, equalTo(comunidad));

        assertThat(comunidadObtenida.getUsuarios(), containsInAnyOrder(usuarios.toArray()));

    }

    @Test
    @Rollback
    public void seDebeObtenerElTokenDeUnUsuarioQuePerteneceAUnaComunidad() {
        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Rock");
        comunidad.setDescripcion("excelente");
        repositorioComunidad.guardarNuevaComunidad(comunidad);

        Usuario usuario = new Usuario();
        usuario.setToken("223");
        usuario.setUrlFoto("https://");
        usuario.setUser("lauti");
        usuario.setRefreshToken("das2");
        sessionFactory.getCurrentSession().save(usuario);

        repositorioComunidad.guardarUsuarioEnComunidad(usuario, comunidad.getId());

        String token = repositorioComunidad.obtenerTokenDelUsuarioQuePerteneceAUnaComunidad(usuario.getUser(), comunidad.getId());

        assertThat(token, equalTo(usuario.getToken()));



    }

    @Test
    @Rollback
    public void seDebeObtenerUnaPlaylistDeUnaComunidad() {

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

        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Rock");
        comunidad.setDescripcion("excelente");

        Playlist playlist = new Playlist();
        playlist.setNombre("Prueba");
        repositorioPlaylist.agregarPlaylist(playlist,cancionSet);
        comunidad.agregarPlaylist(playlist);

        sessionFactory.getCurrentSession().save(playlist);
        repositorioComunidad.guardarNuevaComunidad(comunidad);

        Playlist playlistDeLaComunidad = repositorioComunidad.obtenerPlaylistDeUnaComunidad(comunidad.getId());
        assertThat(playlistDeLaComunidad.getNombre(), equalTo(playlist.getNombre()));
        assertThat(playlistDeLaComunidad.getId(), equalTo(playlist.getId()));
    }
    @Test
    @Rollback
    public void seDebeObtenerLasCancionesDeUnaPlaylistDeUnaComunidad() {

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

        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Rock");
        comunidad.setDescripcion("excelente");

        Playlist playlist = new Playlist();
        playlist.setNombre("Prueba");

        repositorioPlaylist.agregarPlaylist(playlist,cancionSet);

        comunidad.agregarPlaylist(playlist);

        sessionFactory.getCurrentSession().save(playlist);
        repositorioComunidad.guardarNuevaComunidad(comunidad);

        Set<Cancion> cancionesDeLaPLaylist = repositorioComunidad.obtenerCancionesDeUnaPlaylistDeUnaComunidad(comunidad.getId());
        assertThat(cancionesDeLaPLaylist.size(), equalTo(cancionSet.size()));
        assertThat(cancionesDeLaPLaylist,containsInAnyOrder(cancionSet.toArray()));

    }

    @Test
    @Rollback
    public void seDebenObtenerLasPlaylistDeUnaComunidad() {

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

        Comunidad comunidad = new Comunidad();
        comunidad.setNombre("Rock");
        comunidad.setDescripcion("excelente");

        Playlist playlist = new Playlist();
        playlist.setNombre("Prueba");
        repositorioPlaylist.agregarPlaylist(playlist,cancionSet);
        comunidad.agregarPlaylist(playlist);

        Playlist playlist2 = new Playlist();
        playlist2.setNombre("Lauti");
        repositorioPlaylist.agregarPlaylist(playlist2,new ArrayList<>(List.of(cancion)));
        comunidad.agregarPlaylist(playlist2);

        sessionFactory.getCurrentSession().save(playlist);
        sessionFactory.getCurrentSession().save(playlist2);
        repositorioComunidad.guardarNuevaComunidad(comunidad);

        List<Playlist> playlistDeLaComunidad = repositorioComunidad.obtenerPlaylistsPorComunidadId(comunidad.getId());

        assertThat(playlistDeLaComunidad, containsInAnyOrder(playlist, playlist2));
    }

}
