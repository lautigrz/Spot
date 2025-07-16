package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Cancion;
import com.tallerwebi.dominio.Rating;
import com.tallerwebi.dominio.RepositorioRating;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class RepositorioRatingTest {

    private RepositorioRatingImpl repositorioRating;
    private SessionFactory sessionFactory;
    private Session session;
    private Criteria criteria;
    private Query<Rating> query;

    @BeforeEach
    public void setUp() {
        sessionFactory = mock(SessionFactory.class);
        session = mock(Session.class);
        criteria = mock(Criteria.class);
        query = mock(Query.class);

        repositorioRating = new RepositorioRatingImpl(sessionFactory);

        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void queGuardeUnRating() {
        Rating rating = mock(Rating.class);

        repositorioRating.guardarRating(rating);

        verify(session).saveOrUpdate(rating);
    }

    @Test
    public void queObtengaRatingsPorUsuario() {
        Usuario usuario = mock(Usuario.class);
        List<Rating> listaMock = List.of(mock(Rating.class), mock(Rating.class));

        when(session.createCriteria(Rating.class)).thenReturn(criteria);
        when(criteria.add(any())).thenReturn(criteria);
        when(criteria.list()).thenReturn(listaMock);

        List<Rating> resultado = repositorioRating.obtenerRating(usuario);

        assertEquals(listaMock, resultado);
        verify(session).createCriteria(Rating.class);
        verify(criteria).add(any());
        verify(criteria).list();
    }

    @Test
    public void queBusquePorUsuarioYCancion() {
        Usuario usuario = new Usuario();
        Cancion cancion = new Cancion();
        Rating ratingMock = new Rating();

        when(session.createQuery(anyString(), eq(Rating.class))).thenReturn(query);
        when(query.setParameter("usuario", usuario)).thenReturn(query);
        when(query.setParameter("cancion", cancion)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(ratingMock);

        Rating resultado = repositorioRating.buscarPorUsuarioYCancion(usuario, cancion);

        assertEquals(ratingMock, resultado);
        verify(session).createQuery("FROM Rating r WHERE r.usuario = :usuario AND r.cancion = :cancion", Rating.class);
        verify(query).setParameter("usuario", usuario);
        verify(query).setParameter("cancion", cancion);
        verify(query).uniqueResult();
    }

}
