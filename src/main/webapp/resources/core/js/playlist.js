let arregloDeCanciones = [];
document.addEventListener('DOMContentLoaded', function() {

    const inputBusqueda = document.getElementById('inputBusqueda');

    if (inputBusqueda) {
        inputBusqueda.addEventListener('keyup', (event) => {
            const texto = event.target.value.trim();
            console.log("entroo" + texto);

            if (texto.length > 0) {
                fetch(`/spring/busqueda-cancion/${texto}`)
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Error en la búsqueda');
                        }
                        return response.json();
                    })
                    .then(canciones => {
                        const contenedor = document.getElementById('contenedorCanciones');
                        contenedor.innerHTML = '';

                        canciones.forEach(cancion => {

                            const item = document.createElement('div');
                            item.className = 'list-group-item d-flex align-items-center justify-content-between';

                            const izquierda = document.createElement('div');
                            izquierda.className = 'd-flex align-items-center';

                            const img = document.createElement('img');
                            img.src = cancion.urlImagen || '/images/blanco.jpg';
                            img.className = 'rounded me-3';
                            img.width = 50;
                            img.height = 50;
                            img.alt = 'Portada';

                            const texto = document.createElement('div');

                            const titulo = document.createElement('strong');
                            titulo.textContent = cancion.titulo || " ";

                            const artista = document.createElement('small');
                            artista.className = 'text-muted';
                            artista.textContent = cancion.artista || 'Artista desconocido';

                            texto.appendChild(titulo);
                            texto.appendChild(document.createElement('br'));
                            texto.appendChild(artista);

                            izquierda.appendChild(img);
                            izquierda.appendChild(texto);

                            const boton = document.createElement('button');
                            boton.className = 'btn btn-sm btn-white-border agregar';
                            boton.setAttribute('data-id', cancion.spotifyId);
                            boton.setAttribute('data-uris', cancion.uri);
                            boton.setAttribute('data-img', cancion.urlImagen);
                            boton.setAttribute('data-titulo', cancion.titulo);
                            boton.setAttribute('data-artista', cancion.artista);
                            boton.textContent = '+';

                            item.appendChild(izquierda);
                            item.appendChild(boton);

                            contenedor.appendChild(item);
                        });

                    })
                    .catch(error => {
                        console.error('Error al buscar canciones:', error);
                    });
            }
        });
    } else {
        console.warn("No se encontró el input con id 'inputBusqueda'");
    }


    const contenedor = document.getElementById('contenedorCanciones');

    contenedor.addEventListener('click', (event) => {
        const boton = event.target.closest('.agregar');
        if (!boton) return;

        if (boton.classList.contains('btn-success-checked')) {
            boton.classList.remove('btn-success-checked');
            boton.textContent = '+';

            const spotifyId = boton.dataset.id;
            arregloDeCanciones = arregloDeCanciones.filter(c => c.spotifyId !== spotifyId);
        } else {

            boton.classList.add('btn-success-checked');

            boton.innerHTML = '';

            const icono = document.createElement('i');
            icono.className = 'bi bi-check'; // clases para el ícono
            boton.appendChild(icono);

            const spotifyId = boton.dataset.id;
            const uri = boton.dataset.uris;  // ojo con "uris" o "uri"
            const urlImagen = boton.dataset.img;
            const titulo = boton.dataset.titulo;
            const artista = boton.dataset.artista;

            const cancion = {spotifyId, uri, urlImagen, titulo, artista};
            arregloDeCanciones.push(cancion);
        }

        //  arregloDeCanciones.push(cancion);
        console.log(arregloDeCanciones.length);

    });


    const crearPlaylist = document.getElementById('crearPlaylistBtn');
    const idComunidad = document.getElementById('comunidad').value;
    crearPlaylist.addEventListener('click', (event) => {
        fetch(`/spring/guardar-canciones/${idComunidad}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(arregloDeCanciones)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error en la solicitud');
                }
                return response.text();
            })
            .then(data => {
                console.log("url:" + data);
               window.location.href = data
                arregloDeCanciones = [];
            })
            .catch(error => {
                console.error('Error:', error);
            });

    });

});

