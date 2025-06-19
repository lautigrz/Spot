// Manejar preview de fotos y texto
document.addEventListener("DOMContentLoaded", () => {

    const perfilInput = document.getElementById('fotoPerfil');
    const portadaInput = document.getElementById('fotoPortada');
    const nombreInput = document.getElementById('nombreComunidad');
    const descripcionInput = document.getElementById('descripcionComunidad');

    const previewPerfil = document.getElementById('previewPerfil');
    const previewComunidad = document.getElementById('previewComunidad');
    const previewNombre = document.getElementById('previewNombre');
    const previewDescripcion = document.getElementById('previewDescripcion');

    perfilInput.addEventListener('change', () => {
        leerArchivo(perfilInput, src => {
            previewPerfil.src = src || 'https://via.placeholder.com/60?text=Perfil';
        });
    });

    portadaInput.addEventListener('change', () => {
        leerArchivo(portadaInput, src => {
            previewComunidad.style.backgroundImage = src ? `url(${src})` : '';
        });
    });

    nombreInput.addEventListener('input', () => {
        previewNombre.textContent = nombreInput.value.trim() || 'Nombre de la comunidad';
    });

    descripcionInput.addEventListener('input', () => {
        previewDescripcion.textContent = descripcionInput.value.trim() || 'Aquí se mostrará la descripción de la comunidad...';
    });

// Inicializar con valores placeholder
    previewPerfil.src = 'https://via.placeholder.com/60?text=Perfil';
    previewComunidad.style.backgroundImage = '';

});








function leerArchivo(input, callback) {
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = e => callback(e.target.result);
        reader.readAsDataURL(input.files[0]);
    } else {
        callback('');
    }
}

