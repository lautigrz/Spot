document.addEventListener('DOMContentLoaded', () => {

    const btnShare = document.querySelectorAll('.btn-share');
    let postIdActual = null; // Variable para guardar el postId seleccionado
    const compartirModal = new bootstrap.Modal(document.getElementById('compartirModal'));
    btnShare.forEach((btn) => {
        btn.addEventListener('click', () => {
            postIdActual = btn.getAttribute('data-post-id');

            compartirModal.show();
        });
    });

    const confirmarBtn = document.getElementById('btnConfirmarEnvio');
    confirmarBtn.addEventListener('click', () => {
        const checkboxes = document.querySelectorAll('#compartirModal .form-check-input');

        const comunidadesSeleccionadas = Array.from(checkboxes)
            .filter(checkbox => checkbox.checked)
            .map(checkbox => checkbox.value);

        console.log('Post ID:', postIdActual);
        console.log('Comunidades seleccionadas:', comunidadesSeleccionadas);

        compartirPost(postIdActual, document.getElementById("usuarioID").value, comunidadesSeleccionadas)
            .then(() => {
                // Cerrar modal después de enviar
                compartirModal.hide();
            });
    });
});


function compartirPost(postId, userId, comunidades) {
    return fetch(`/spring/compartir-post/${postId}/${userId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json' //
        },
        body: JSON.stringify(comunidades)
    })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                document.getElementById(`recomendacion-${data.idRecomendacion}`).remove();
            }
            return data;
        })
        .catch(error => {
            console.error('Error al aceptar recomendación:', error);
        });
}
