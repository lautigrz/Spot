document.addEventListener('click', function(event) {
    // ACEPTAR
    if (event.target.closest('.btn-aceptar-reco')) {
        const boton = event.target.closest('.btn-aceptar-reco');
        const idRecomendacion = boton.dataset.idRecomendacion;
        const idComunidad = document.getElementById("comunidad").value;


        console.log("idReco" + idRecomendacion);

        fetch(`/spring/aceptar-reco/${idComunidad}/${idRecomendacion}`, {
            method: 'POST'
        })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    document.getElementById(`recomendacion-${data.idRecomendacion}`).remove();

                }
            })
            .catch(error => {
                console.error('Error al aceptar recomendación:', error);
            });
    }

    // ELIMINAR
    if (event.target.closest('.btn-eliminar-reco')) {
        const boton = event.target.closest('.btn-eliminar-reco');
        const idRecomendacion = boton.dataset.idRecomendacion;
        const idComunidad = boton.dataset.idComunidad;

        fetch(`/spring/eliminar-reco/${idComunidad}/${idRecomendacion}`, {
            method: 'POST'
        })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    document.getElementById(`recomendacion-${data.idRecomendacion}`).remove();
                }
            })
            .catch(error => {
                console.error('Error al eliminar recomendación:', error);
            });
    }
});
