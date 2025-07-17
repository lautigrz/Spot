    package com.tallerwebi.dominio;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.Optional;


    @Service
    public class ServicioArtistaImpl implements ServicioArtista {

        private final RepositorioArtista repositorioArtista;

        @Autowired
        public ServicioArtistaImpl(RepositorioArtista repositorioArtista) {
            this.repositorioArtista = repositorioArtista;
        }

        @Override
        public Optional<Artista> buscarPorEmail(String email) {
            return repositorioArtista.buscarPorEmail(email);

        }

        @Override
        public void guardar(Artista artista) {
            repositorioArtista.guardar(artista);

        }

        @Override
        public Artista buscarPorNombre(String nombre) {
            return repositorioArtista.buscarPorNombre(nombre);
        }

        @Override
        public Artista buscarPorId(Long id) {
            return repositorioArtista.buscarPorId(id);
        }

        @Override
        public List<Preescucha> obtenerPreescuchasDeArtista(Long artistaId){
            return repositorioArtista.obtenerPreescuchasDeArtista(artistaId);
        }

        @Override
        public List<Artista> buscarPorTexto(String texto) {
            return repositorioArtista.buscarPorTexto(texto);
        }


    }