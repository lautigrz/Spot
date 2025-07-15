document.addEventListener("DOMContentLoaded", () => {

    const idUsuario = document.getElementById("idUser").value;
    obtenerCancionActualDesdeServidor(idUsuario)
});
function obtenerCancionActualDesdeServidor(idUsuario) {
    fetch(`/spring/escuchando/${idUsuario}`)
        .then(response => {
            if (!response.ok) throw new Error('Error en la respuesta del servidor');
            return response.json();
        })
        .then(data => {

            console.log("Datos de la canción actual:", data);
            actualizarFooterCancion(data);

            console.log(data);

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


    imgElement.onload = function () {
        aplicarColoresDesdePaleta(colorThief, imgElement);
    };


}

function aplicarColoresDesdePaleta(colorThief, img) {
    const footer = document.getElementById("footer");
    const boton = document.getElementById("backToTop");

    const palette = colorThief.getPalette(img, 5);
    const [color1, color2] = palette;

    const gradient = `linear-gradient(to right,
                rgb(${color1[0]}, ${color1[1]}, ${color1[2]}),
                rgb(${color2[0]}, ${color2[1]}, ${color2[2]}))`;

    footer.style.background = gradient;
    footer.style.display = "flex";
    boton.style.display = "flex";

    const luminance = 0.299 * color1[0] + 0.587 * color1[1] + 0.114 * color1[2];

    console.log("luminaria",luminance);
    if (luminance > 162) {
        footer.style.setProperty("color", "black", "important");

    } else {

        footer.style.setProperty("color", "white", "important");

    }
}

