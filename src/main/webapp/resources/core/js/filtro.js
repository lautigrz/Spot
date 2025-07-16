document.addEventListener('DOMContentLoaded', function() {
    const btnFiltrar = document.getElementById('btnFiltrarCompras');

    btnFiltrar.addEventListener('click', function() {
        const idUsuario = document.getElementById('usuarioID').value;
        const orden = document.getElementById('ordenFiltro').value;

        const url = `/spring/compras-usuario/${idUsuario}?orden=${orden}`;

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error al obtener los datos filtrados');
                }
                return response.json();
            })
            .then(data => {
                console.log('Datos filtrados:', data);

                const tbody = document.querySelector('#modalMisPreescuchasCompradas tbody');
                tbody.innerHTML = '';

                data.forEach(pre => {
                    const tr = document.createElement('tr');
                    tr.innerHTML = `
            <td>
              <img src="${pre.urlFotoPreescucha}" alt="Foto preescucha"
                class="rounded-circle img-fluid"
                style="width: 50px; height: 50px; object-fit: cover;">
            </td>
            <td>${pre.nombrePreescucha}</td>
            <td>${pre.artistaNombre}</td>
            <td>${pre.precio}</td>
            <td>${pre.fechaCompra}</td>
            <td>${pre.fechaInicio}</td>
            <td>
              <a href="/comunidad-preescucha/${pre.idPreescucha}" class="btn btn-primary btn-sm">Ir</a>
            </td>
          `;
                    tbody.appendChild(tr);
                });
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al filtrar las compras.');
            });
    });
});