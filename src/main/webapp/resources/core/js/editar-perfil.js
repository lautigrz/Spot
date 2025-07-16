document.getElementById("filePerfil").addEventListener("change", function () {
    const file = this.files[0];
    if (file) {

        const reader = new FileReader();
        reader.onload = function (e) {
            document.getElementById("fotoPerfil").src = e.target.result;
        };
        reader.readAsDataURL(file);


        const formData = new FormData();
        formData.append("fotoPerfil", file);

        fetch("/spring/actualizar-foto-perfil", {
            method: "POST",
            body: formData
        })
            .then(response => {
                if (response.redirected) {
                    window.location.href = response.url; // redirige si corresponde
                } else {
                    return response.text().then(text => {
                        console.error("Respuesta inesperada del servidor:", text);
                    });
                }
            })

            .then(data => {
                console.log("Perfil actualizado:", data);
            })
            .catch(error => {
                console.error("Error al subir la foto de perfil:", error);
            });
    }
});

document.getElementById("filePortada").addEventListener("change", function () {
    const file = this.files[0];
    if (file) {

        const reader = new FileReader();
        reader.onload = function (e) {
            document.querySelector(".banner").style.backgroundImage = `url('${e.target.result}')`;
        };
        reader.readAsDataURL(file);


        const formData = new FormData();
        formData.append("fotoPortada", file);

        fetch("/spring/actualizar-portada", {
            method: "POST",
            body: formData
        })
            .then(response => {
                if (response.redirected) {
                    window.location.href = response.url;
                } else {
                    return response.text().then(text => {
                        console.error("Respuesta inesperada del servidor:", text);
                    });
                }
            })

            .then(data => {
                console.log("Perfil actualizado:", data);
            })
            .catch(error => {
                console.error("Error al subir la foto de perfil:", error);
            });
    }
});