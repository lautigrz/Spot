let timeoutCancion;
let progresoActual = 0;
let duracionCancion = 0;
let intervaloProgreso = null;

document.addEventListener("DOMContentLoaded", () => {

    const idComunidad = document.getElementById("comunidad").value;

});
function obtenerCancionActualDesdeServidor(idComunidad) {
    fetch(`/spring/cancion/${idComunidad}`)
        .then(response => {
            if (!response.ok) throw new Error('Error en la respuesta del servidor');
            return response.json();
        })
        .then(data => {
            actualizarFooterCancion(data);

            if (timeoutCancion) clearTimeout(timeoutCancion);
            if (intervaloProgreso) clearInterval(intervaloProgreso);

            progresoActual = data.progreso;
            duracionCancion = data.duracion;

            const tiempoRestanteMs = duracionCancion - progresoActual;

            // Animar barra de progreso
            intervaloProgreso = setInterval(() => {
                progresoActual += 1000;

                if (progresoActual >= duracionCancion) {
                    clearInterval(intervaloProgreso);
                    return;
                }

                const porcentaje = Math.min((progresoActual / duracionCancion) * 100, 100);
                document.getElementById("barraProgreso").style.width = porcentaje + "%";
                document.getElementById("tiempoActual").textContent = millisToMinSeg(progresoActual);
            }, 1000);

            // Consultar cuando termine la canción
            timeoutCancion = setTimeout(() => {
                obtenerCancionActualDesdeServidor(idComunidad);
            }, tiempoRestanteMs + 1000);
        })
        .catch(error => {
            console.error("Error al obtener la canción actual:", error);

        });
}

function actualizarFooterCancion(dt) {
    const colorThief = new ColorThief();
    const imgElement = document.getElementById("imageTrack");
    if (!imgElement) return;

    imgElement.crossOrigin = "anonymous";
    imgElement.src = dt.urlImagen;

    document.getElementById("artista").textContent = dt.artista;
    document.getElementById("titulo").textContent = dt.titulo;

    document.getElementById("tiempoActual").textContent = millisToMinSeg(dt.progreso);
    document.getElementById("tiempoTotal").textContent = millisToMinSeg(dt.duracion);

    const progresoPorcentaje = Math.min((dt.progreso / dt.duracion) * 100, 100);
    document.getElementById("barraProgreso").style.width = progresoPorcentaje + "%";

    imgElement.onload = function () {
        aplicarColoresDesdePaleta(colorThief, imgElement);
    };

    function aplicarColoresDesdePaleta(colorThief, img) {
        const footer = document.getElementById("footer");
        const palette = colorThief.getPalette(img, 5);
        const [color1, color2] = palette;

        const gradient = `linear-gradient(to right,
                rgb(${color1[0]}, ${color1[1]}, ${color1[2]}),
                rgb(${color2[0]}, ${color2[1]}, ${color2[2]}))`;

        footer.style.background = gradient;
        footer.style.display = "flex";
    }
}

function millisToMinSeg(ms) {
    const min = Math.floor(ms / 60000);
    const seg = Math.floor((ms % 60000) / 1000).toString().padStart(2, '0');
    return `${min}:${seg}`;
}