document.getElementById("buscador").addEventListener("keyup", function (e) {

    const buscar = e.target.value.toLowerCase();
    const resultados = document.getElementById("resultados");


    if (buscar.length === 0) {
        resultados.innerHTML = "";
        return;
    }

    fetch(`/spring/buscar-comunidad/${buscar}`)
        .then(response => response.json())
        .then(data => {
            console.log(data);
            // Limpia resultados anteriores
            resultados.innerHTML = "";

            if (data.length === 0) {
                resultados.innerHTML = "<p>No se encontraron comunidades.</p>";
                return;
            }


            data.forEach(comunidad => {
                const link = document.createElement("a");
                link.href = `/spring/comunidad/${comunidad.id}`; // ajusta URL a tu ruta
                link.classList.add("resultado-item");

                // Crea la imagen
                const img = document.createElement("img");
                img.src = comunidad.urlFoto; // URL de la foto
                img.alt = comunidad.nombre;
                img.classList.add("resultado-img");

                // Contenedor de texto
                const texto = document.createElement("div");
                texto.classList.add("resultado-texto");

                const nombre = document.createElement("h6");
                nombre.textContent = comunidad.nombre;

                const descripcion = document.createElement("p");
                descripcion.textContent = comunidad.descripcion || ""; // opcional

                texto.appendChild(nombre);
                texto.appendChild(descripcion);

                // Agrega imagen y texto al enlace
                link.appendChild(img);
                link.appendChild(texto);

                resultados.appendChild(link);
            });
        })
        .catch(error => {
            console.error('Error:', error);
        });

})