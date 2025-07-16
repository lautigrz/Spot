    package com.tallerwebi.infraestructura;

    import com.tallerwebi.dominio.Favorito;
    import com.tallerwebi.dominio.RepositorioFavorito;
    import com.tallerwebi.dominio.Usuario;
    import org.hibernate.SessionFactory;
    import org.hibernate.query.Query;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Repository;
    import org.springframework.transaction.annotation.Transactional;

    import java.util.List;

    @Repository
    @Transactional
    public class RepositorioFavoritoImpl implements RepositorioFavorito {

        private final SessionFactory sessionFactory;

        @Autowired
        public RepositorioFavoritoImpl(SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
        }

        @Override
        public void agregarFavorito(Favorito favorito) {
            sessionFactory.getCurrentSession().save(favorito);
        }

        @Override
        public List<Favorito> obtenerFavoritosDeUsuario(Long idUsuario) {
            String hql = "FROM Favorito f WHERE f.usuario.id = :idUsuario";
            Query<Favorito> query = sessionFactory.getCurrentSession().createQuery(hql, Favorito.class);
            query.setParameter("idUsuario", idUsuario);
            return query.list();
        }

        @Override
        public boolean yaEsFavorito(String spotifyArtistId, Long idUsuario) {
            String hql = "SELECT COUNT(f) FROM Favorito f WHERE f.spotifyArtistId = :id AND f.usuario.id = :usuarioId";
            Query<Long> query = sessionFactory.getCurrentSession().createQuery(hql, Long.class);
            query.setParameter("id", spotifyArtistId);
            query.setParameter("usuarioId", idUsuario);
            return query.uniqueResult() > 0;
        }

        @Override
        public void quitarFavorito(String idLocal, Long id) {
            String hql = "DELETE FROM Favorito f WHERE f.spotifyArtistId = :idLocal AND f.usuario.id = :idUsuario";
            Query query = sessionFactory.getCurrentSession().createQuery(hql);
            query.setParameter("idLocal", idLocal);
            query.setParameter("idUsuario", id);
            query.executeUpdate();
        }


    }
