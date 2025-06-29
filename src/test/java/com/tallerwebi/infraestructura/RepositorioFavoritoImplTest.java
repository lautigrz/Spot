package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Favorito;
import com.tallerwebi.dominio.RepositorioFavorito;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
@Transactional
public class RepositorioFavoritoImplTest {

    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioFavorito repositorioFavorito;


    @BeforeEach
    public void setUp(){
        repositorioFavorito = new RepositorioFavoritoImpl(sessionFactory);
    }


    @Test
    @Rollback
    public void deberiaGuardarUnArtistaFavoritoParaUnUsuario(){
        Usuario usuario = new Usuario();
        usuario.setUser("testuser");
        usuario.setToken("token");
        usuario.setRefreshToken("refresh");
        usuario.setUrlFoto("foto.jpg");
        sessionFactory.getCurrentSession().save(usuario);

        // Crear y guardar un favorito
        Favorito favorito = new Favorito();
        favorito.setSpotifyArtistId("3TVXtAsR1Inumwj472S9r4"); // Drake
        favorito.setUsuario(usuario);
        sessionFactory.getCurrentSession().save(favorito);

        //Metodo que se testea
        List<Favorito> favoritos = repositorioFavorito.obtenerFavoritosDeUsuario(usuario.getId());

        //Solo 1 favorito agregado
        assertThat(favoritos.size(), equalTo(1));
        //El id este correcto
        assertThat(favoritos.get(0).getSpotifyArtistId(), equalTo("3TVXtAsR1Inumwj472S9r4"));
        //Que el favorito pertenece al usuario
        assertThat(favoritos.get(0).getUsuario().getId(), equalTo(usuario.getId()));
    }


    @Test
    @Rollback
    public void deberiaEncontrarSiUnArtistaYaEsFavoritoDeUnUsuario(){
        Usuario usuario = new Usuario();
        usuario.setUser("testuser");
        usuario.setToken("token");
        usuario.setRefreshToken("refresh");
        usuario.setUrlFoto("foto.jpg");
        sessionFactory.getCurrentSession().save(usuario);

        // Crear y guardar un favorito
        Favorito favorito = new Favorito();
        favorito.setSpotifyArtistId("3TVXtAsR1Inumwj472S9r4"); // Drake
        favorito.setUsuario(usuario);
        sessionFactory.getCurrentSession().save(favorito);

        boolean esFavorito = repositorioFavorito.yaEsFavorito(favorito.getSpotifyArtistId(), usuario.getId());

        assertThat(esFavorito, equalTo(true));
    }

    }

