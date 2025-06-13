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


    // Mostrar preview de la imagen cuando se selecciona
    document.getElementById('imagenPlaylist').addEventListener('change', (event) => {
        const file = event.target.files[0];
        console.log('Archivo seleccionado:', file);
        const preview = document.getElementById('previewImagen');
        if (file) {
            const url = URL.createObjectURL(file);
            preview.src = url;
        } else {
            preview.src = "https://via.placeholder.com/200x200?text=+";
        }
    });

// Enviar formulario al backend con FormData al clickear crear playlist
    document.getElementById('btnCrearPlaylist').addEventListener('click', (event) => {
        const idComunidad = document.getElementById('comunidad').value;
        const nombrePlaylist = document.getElementById('nombrePlaylist').value;
        const imagenInput = document.getElementById("imagenPlaylist");
        const imagen = imagenInput.files[0];

        console.log('Input imagenPlaylist:', imagenInput);
        console.log('Archivos seleccionados:', imagenInput.files);
        console.log('Primer archivo:', imagen);

        console.log('nombre:', nombrePlaylist);
        if (!nombrePlaylist || !imagen) {
            alert("Faltan datos para crear la playlist.");
            return;
        }

        const formData = new FormData();
        formData.append('nombre', nombrePlaylist);
        formData.append('imagen', imagen);
        formData.append('canciones', JSON.stringify(arregloDeCanciones));

        fetch(`/spring/guardar-canciones/${idComunidad}`, {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error en la solicitud');
                }
                return response.text();
            })
            .then(data => {
                console.log("URL:", data);
                window.location.href = data;
                arregloDeCanciones = [];
            })
            .catch(error => {
                console.error('Error:', error);
            });
    });


    document.getElementById('listaPlaylists').addEventListener('click', (event) => {

        const playlistDiv = event.target.closest('.playlist-item');
        if (!playlistDiv) return;

        const idCompleto = playlistDiv.id;
        const nombreElemento = playlistDiv.querySelector('h5');
        const idPlaylist = idCompleto ? idCompleto.replace('playlist-', '') : null;

        console.log('Playlist clickeada con id:', idPlaylist);
        document.getElementById('listaPlaylists').classList.add('d-none');
        document.getElementById('detallePlaylist').classList.remove('d-none');


        obtenerCancionesDeUnaPlaylist(idPlaylist)
            .then(canciones => {
                const contenido = document.getElementById('contenidoCanciones');

                if (!canciones || canciones.length === 0) {
                    contenido.innerHTML = '<p>No hay canciones en esta playlist.</p>';
                    return;
                }

                let html = `<h5>${nombreElemento.textContent}</h5>`; // si tienes nombre playlist en el DTO, si no, solo 'Playlist'
                html += `<div class="list-group">`;

                canciones.forEach(c => {
                    html += `
  <div class="d-flex align-items-center bg-dark text-white mb-1 p-3 rounded shadow-sm" style="box-shadow: 0 2px 8px rgba(58,55,55,0.17);">
    <img src="${c.urlImagen || '/images/default.jpg'}" alt="${c.artista}" class="me-3 rounded" style="width: 60px; height: 60px; object-fit: cover;">
    <div>
      <div class="fw-bold">${c.titulo}</div>
      <small class="text-white-80">${c.artista}</small>
    </div>
  </div>
`;
                });

                html += `</div>`;
                contenido.innerHTML = html;
            })
            .catch(error => console.error(error));

    });
    document.getElementById('volver').addEventListener('click', function () {
        document.getElementById('detallePlaylist').classList.add('d-none');
        document.getElementById('listaPlaylists').classList.remove('d-none');
    });

});

function obtenerCancionesDeUnaPlaylist(idPlaylist) {
    return fetch(`/spring/canciones-de-playlist/${idPlaylist}`)
    .then(response => {
        if (!response.ok) {
            throw new Error('Error en la búsqueda');
        }
        return response.json();
    });
}

