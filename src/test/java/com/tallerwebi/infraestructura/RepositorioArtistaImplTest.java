package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Artista;
import com.tallerwebi.dominio.RepositorioArtista;
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

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})
@Transactional
public class RepositorioArtistaImplTest {

    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioArtista repositorioArtista;

    @BeforeEach
    public void setUp() {
        repositorioArtista = new RepositorioArtistaImpl(sessionFactory);
    }

    @Test
    @Rollback
    public void deberiaGuardarYBuscarUnArtistaPorId(){
        Artista artista = new Artista();
        artista.setNombre("Facu");
        artista.setEmail("facu@gmail.com");
        artista.setPassword("1234");
        artista.setFotoPerfil("foto.jpg");

        repositorioArtista.guardar(artista);

        Artista resultado = repositorioArtista.buscarPorId(artista.getId());

        assertThat(resultado.getNombre(), equalTo("Facu"));
        assertThat(resultado.getEmail(), equalTo("facu@gmail.com"));
    }

    @Test
    @Rollback
    public void deberiaBuscarUnArtistaPorEmail(){
        Artista artista = new Artista();
        artista.setNombre("Facu");
        artista.setEmail("facu@gmail.com");
        artista.setPassword("1234");
        artista.setFotoPerfil("foto.jpg");

        repositorioArtista.guardar(artista);

        Optional<Artista> resultado = repositorioArtista.buscarPorEmail(artista.getEmail());

        assertThat(resultado.isPresent(), equalTo(true));
        assertThat(resultado.get().getNombre(), equalTo("Facu"));
        assertThat(resultado.get().getEmail(), equalTo("facu@gmail.com"));
    }

    @Test
    @Rollback
    public void deberiaBuscarUnArtistaPorNombre(){
        Artista artista = new Artista();
        artista.setNombre("Facu");
        artista.setEmail("facu@gmail.com");
        artista.setPassword("1234");
        artista.setFotoPerfil("foto.jpg");

        repositorioArtista.guardar(artista);

        Artista resultado = repositorioArtista.buscarPorNombre(artista.getNombre());
        assertThat(resultado.getNombre(), equalTo("Facu"));
        assertThat(resultado.getEmail(), equalTo("facu@gmail.com"));

    }
}
