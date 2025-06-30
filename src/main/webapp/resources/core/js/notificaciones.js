
const idUsuario = document.getElementById('usuarioID').value;

document.querySelector('[data-bs-target="#notificacionesOffcanvas"]').addEventListener('click', () => {

    fetch(`/spring/notificaciones/${idUsuario}`)
        .then(res => res.json())
        .then(data => {
            const lista = document.getElementById('listaNotificaciones');
            lista.innerHTML = '';
            data.forEach(notif => {
                const li = document.createElement('li');
                li.className = 'list-group-item';

                // Si la notificación está leída, le agregamos clase 'leida'
                if (notif.estado === true) {
                    li.classList.add('leida');
                }

                const saludo = document.createElement('div');
                saludo.style.fontWeight = 'bold';
                saludo.textContent = `Hola ${notif.nombreUsuario}`;


                const mensajeSpan = document.createElement('span');
                mensajeSpan.textContent = notif.mensaje;

                // Botón marcar como leída
                const btnMarcar = document.createElement('button');
                const btnMarcarLeida = document.createElement('button');
                btnMarcarLeida.innerHTML = '<i class="bi bi-check-lg"></i>';
                btnMarcarLeida.title = 'Marcar como leída'; // deshabilitado si ya está leída

                btnMarcar.addEventListener('click', () => {
                    // Aquí llamás al endpoint para marcar la notificación como leída, por ejemplo:
                    fetch(`/notificacion/leido/${notif.id}`, { method: 'GET' })
                        .then(response => {
                            if (response.ok) {
                                li.classList.add('leida');
                                btnMarcar.textContent = 'Leída';
                                btnMarcar.disabled = true;
                            } else {
                                alert('Error al marcar como leída');
                            }
                        })
                        .catch(() => alert('Error en la conexión'));
                });

                li.appendChild(saludo);
                li.appendChild(mensajeSpan);
                li.appendChild(btnMarcarLeida);

                lista.appendChild(li);
            });

        });
});
