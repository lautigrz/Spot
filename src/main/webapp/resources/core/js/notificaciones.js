
const idUsuario = document.getElementById('usuarioID').value;

document.querySelector('[data-bs-target="#notificacionesOffcanvas"]').addEventListener('click', () => {
    const idUsuario = document.getElementById('usuarioID').value;
    let idsNotificaciones = [];

    fetch(`/spring/notificaciones/${idUsuario}`)
        .then(res => res.json())
        .then(data => {
            const lista = document.getElementById('listaNotificaciones');
            lista.innerHTML = '';

            data.forEach(notif => {
                console.log(notif);

                const li = document.createElement('li');
                li.className = 'list-group-item';
                if (notif.leido === true){
                    li.style.backgroundColor = '#e5e5e5'

                }

                if (notif.leido !== true) {
                    idsNotificaciones.push(notif.id);
                    li.style.border = '1px solid rgba(154, 230, 113)'
                }

                const saludo = document.createElement('div');
                saludo.style.fontWeight = 'bold';
                saludo.textContent = `Hola ${notif.nombreUsuario}`;

                const mensajeSpan = document.createElement('span');
                mensajeSpan.textContent = notif.mensaje;

                const btnMarcarLeida = document.createElement('button');
                btnMarcarLeida.innerHTML = '<i class="bi bi-check-lg"></i>';
                btnMarcarLeida.title = 'Marcar como leída';

                li.appendChild(saludo);
                li.appendChild(mensajeSpan);
                li.appendChild(btnMarcarLeida);

                lista.appendChild(li);
            });

            console.log('IDs antes de marcar:', idsNotificaciones);

            // 🚀 Mover el segundo fetch AQUÍ
            if (idsNotificaciones.length > 0) {
                const indicador = document.getElementById('indicador');

                fetch('/spring/notificacion/leer', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(idsNotificaciones)
                })
                    .then(response => {
                        console.log('Fetch status:', response.status);
                        if (response.ok) {
                            console.log('Marcado como leído');
                            if (indicador) indicador.style.display = 'none';
                        } else {
                            console.log('Respuesta no OK');
                            alert('Error al marcar como leídas');
                        }
                    })
                    .catch(err => {
                        console.error('Fetch error:', err);
                        alert('Error en la conexión');
                    });
            }
        });
});

