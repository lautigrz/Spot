document.getElementById("buscador").addEventListener("keyup", function (e) {

    const buscar = e.target.value.toLowerCase();
    const resultados = document.getElementById("resultados");


    if (buscar.length === 0) {
        resultados.style.display = "none";
        resultados.innerHTML = "";
        return;
    }

    fetch(`/spring/buscar-comunidad/${buscar}`)
        .then(response => response.json())
        .then(data => {
            console.log(data);

            resultados.innerHTML = "";

            if (data.length === 0) {
                return;
            }


            data.forEach(comunidad => {
                resultados.style.display = "block";
                const link = document.createElement("a");
                link.href = `/spring/comunidad/${comunidad.id}`;
                link.classList.add("resultado-item");


                const img = document.createElement("img");
                img.src = comunidad.urlFoto;
                img.alt = comunidad.nombre;
                img.classList.add("resultado-img");


                const texto = document.createElement("div");
                texto.classList.add("resultado-texto");

                const nombre = document.createElement("h6");
                nombre.textContent = comunidad.nombre;

                const descripcion = document.createElement("p");
                descripcion.textContent = comunidad.descripcion || ""; // opcional

                texto.appendChild(nombre);
                texto.appendChild(descripcion);


                link.appendChild(img);
                link.appendChild(texto);

                resultados.appendChild(link);
            });
        })
        .catch(error => {
            console.error('Error:', error);
        });

})