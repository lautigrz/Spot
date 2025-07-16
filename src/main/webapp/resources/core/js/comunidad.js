
var messageInput = document.querySelector("#mensajeInput");
var messageArea = document.querySelector("#mensajeVista");
var messageForm = document.getElementById("enviar");
var stompClient = null;
var rol = document.getElementById("rol").value;


function connect() {

    const datos = {
        username: document.getElementById("username").value,
        idUser: document.getElementById("id").value,
        urlImage: document.getElementById("urlFoto").value,
        idComunidad: document.getElementById("comunidad").value
    }


    var socket = new SockJS("/spring/websocket");
    stompClient = Stomp.over(socket);

    console.log("Valor de idComunidad antes de conectar:", datos.idComunidad);

    stompClient.connect({}, function(frame) {
        onConnected(datos);
    }, onError);
    marcarUsuarioComoActivo(datos.username);

}

function onError(error) {
    console.error("Error al conectar STOMP:", error);
}

function onConnected(datos) {
    console.log("Suscrito a: ", `/topic/${datos.idComunidad}`);
    stompClient.subscribe(`/topic/${datos.idComunidad}`, onMessageReceived);

    let preescucha = document.getElementById("preescucha").value;

    if(preescucha > 0) {
        stompClient.subscribe(`/topic/estado-actual.${datos.idComunidad}`, function (message) {
            console.log("Estado actual recibido en broadcast:", message);
            var estado = JSON.parse(message.body);
            mostrarPlayerSincronizado(estado.canciones, estado.indiceActual, estado.segundosReproducidos, estado.idComunidad);
        });
    }

    stompClient.send(
        `/app/chat.register/${datos.idComunidad}`,
        {},
        JSON.stringify({sender: datos.username, type: "CHAT"}),
    );


    if (preescucha > 0) {
    stompClient.send(
        `/app/preescucha.estado.${datos.idComunidad}`,
        {},
        null
    );
    }


   obtenerCancionActualDesdeServidor(datos.idComunidad);
}

function send(event){
    var message = messageInput.value.trim();
    var username = document.getElementById("username").value;
    var idUser = document.getElementById("id").value;
    var urlImage = document.getElementById("urlFoto").value;
    var idComunidad = document.getElementById("comunidad").value;

    if(message && stompClient) {

        var chatMessage = {
            sender: username,
            content: messageInput.value,
            image: urlImage,
            type: "CHAT",
            id: idUser,
        };

        stompClient.send(`/app/chat.send/${idComunidad}`, {}, JSON.stringify(chatMessage));

        messageInput.value = "";

    }
    event.preventDefault();
}

function eliminarMensaje(idMensaje, idComunidad) {
    var deleteMessage = {
        id: idMensaje,
        type: "DELETE"
    };

    stompClient.send(`/app/chat.delete/${idComunidad}`, {}, JSON.stringify(deleteMessage));
}


function onMessageReceived(payload) {

    console.log("Mensaje recibido:", payload);
    var message = JSON.parse(payload.body);

    if(message.content && message.content.trim() !== "" && message.type === "CHAT") {
        const nuevoMensaje = crearMensajeHTML(message);
        messageArea.appendChild(nuevoMensaje);
        messageArea.scrollTop = messageArea.scrollHeight;
    }  else if (message.type === "DELETE") {
        const mensajeDiv = document.querySelector(`[data-id='${message.id}']`);
        if (mensajeDiv) {
            console.log("mensaje eliminado:", message.id);

            mensajeDiv.innerHTML = `
  <div class="d-flex align-items-start text-muted fst-italic border-bottom">
    <img src="${message.image}" alt="Usuario" 
         class="rounded-circle me-3" style="width: 50px; height: 50px; object-fit: cover;">
    <div class="d-flex align-items-center">
      <i class="bi bi-info-circle me-2"></i>
      <em>Este mensaje fue eliminado por el administrador porque no cumple las normas de la comunidad.</em>
    </div>
  </div>
`;
            mensajeDiv.classList.add("text-muted");
        }
    }else if(message.type === "PREESCUCHA") {
        console.log("Mensaje recibidoen preescucha:", message);
        mostrarPlayer(message.data.canciones, message.data.idComunidad);

    } else if(message.type === "PREESCUCHA_ESTADO" || message.type === "ESTADO_ACTUAL") {
        console.log("Estado sincronización recibido:", message);

        const canciones = message.canciones;
        const indiceActual = message.indiceActual;
        const segundosReproducidos = message.segundosReproducidos;
        const idComunidad = message.idComunidad;

        mostrarPlayerSincronizado(canciones, indiceActual, segundosReproducidos, idComunidad);
    }



}
function mostrarPlayerSincronizado(canciones, indiceActual, segundosReproducidos, idComunidad) {
    const audioPlayer = document.getElementById("audioPlayer");
    const playlist = document.getElementById("playlist-escucha");
    const footer = document.getElementById("footer");
    const playerContainer = document.getElementById("playerContainer");

    console.log("Mostrar player sincronizado con canciones:", canciones, indiceActual);


    // Validar si ya terminó
    if (indiceActual > canciones.length) {
        console.log("Preescucha finalizada (usuario)");
        playerContainer.style.display = "none";
        footer.style.display = "none";
        audioPlayer.pause();
        return;
    }

    // Limpiar y armar playlist
    playlist.innerHTML = "";
    canciones.forEach((c, idx) => {
        const li = document.createElement("li");
        li.textContent = `Canción ${idx + 1}: ${c.titulo}`;
        if (idx === indiceActual) li.style.fontWeight = "bold";
        playlist.appendChild(li);
    });

    // Mostrar contenedor del player
    playerContainer.style.display = "block";
    footer.style.display = "flex";

    // Función para reproducir la canción actual
    function reproducirActual() {
        const cancion = canciones[indiceActual];

        audioPlayer.src = cancion.ruta;
        audioPlayer.load();

        actualizarFooterCancion({
            urlImagen: cancion.urlPortada,
            artista: cancion.artista || "Desconocido",
            titulo: cancion.titulo,
            progreso: segundosReproducidos * 1000,
            duracion: cancion.duracion * 1000
        });

        Array.from(playlist.children).forEach((li, idx) => {
            li.style.fontWeight = (idx === indiceActual) ? "bold" : "normal";
        });

        function handler() {
            audioPlayer.currentTime = segundosReproducidos;
            audioPlayer.play();
            audioPlayer.removeEventListener("loadedmetadata", handler);
        }

        audioPlayer.removeEventListener("loadedmetadata", handler);
        audioPlayer.addEventListener("loadedmetadata", handler);
    }

    // Limpiar listeners previos y definir uno nuevo
    audioPlayer.onended = null;
    audioPlayer.removeEventListener('ended', onSongEnded);

    function onSongEnded() {
        console.log("Canción finalizada (usuario sincronizado). Esperando host...");
        // Opcional: mostrar UI de espera
    }

    audioPlayer.addEventListener('ended', onSongEnded);

    reproducirActual();
}




function crearMensajeHTML(message) {

    const container = document.createElement("div");
    container.className = "chat-message p-3 d-flex align-items-start my-2 bg-light rounded position-relative";
    container.setAttribute("data-id", message.id);
    const img = document.createElement("img");
    img.src = message.image || "default.jpg";
    img.alt = "User";
    img.className = "rounded-circle me-3";
    img.style.width = "50px";
    img.style.height = "50px";
    img.style.objectFit = "cover";


    const contentWrapper = document.createElement("div");
    contentWrapper.className = "d-flex align-items-center flex-grow-1 ";

    const textDiv = document.createElement("div");
    textDiv.className = "flex-grow-1";


    const link = document.createElement("a");
    link.href = `/spring/perfil/${message.idUsuario}`;
    link.style.textDecoration = "none"; // Quita subrayado
    link.style.color = "inherit";       // Hereda color

    const strong = document.createElement("strong");
    strong.textContent = message.sender;

    link.appendChild(strong);



    const fecha = new Date();
    const small = document.createElement("small");
    small.className = "text-muted";
    small.innerText =
        fecha.toLocaleDateString("en-GB", {
            day: "numeric",
            month: "numeric",
            year: "2-digit"
        })
        + " · " +
        fecha.toLocaleTimeString("en-US", {
            hour: "2-digit",
            minute: "2-digit",
            hour12: false
        });



    const p = document.createElement("p");
    p.className = "mb-0 d-inline";
    p.textContent = " " + message.content;

    textDiv.appendChild(link);
    textDiv.appendChild(document.createElement("br"));
    textDiv.appendChild(small);
    textDiv.appendChild(p);

    contentWrapper.appendChild(textDiv);

    container.appendChild(img);
    container.appendChild(contentWrapper);

    console.log("Rol:" + rol)
    if (rol === 'Admin') {
        var idComunidad = document.getElementById("comunidad").value;
        var urlImage = document.getElementById("urlFoto").value
        const menuContainer = document.createElement("div");
        menuContainer.className = "position-absolute top-0 end-0 dropdown";


        const btnMenu = document.createElement("button");
        btnMenu.className = "btn btn-sm btn-light dropdown";
        btnMenu.type = "button";
        btnMenu.setAttribute("data-bs-toggle", "dropdown");
        btnMenu.setAttribute("aria-expanded", "false");
        btnMenu.style.border = "none";
        btnMenu.style.boxShadow = "none";
        btnMenu.style.padding = "0 5px";
        btnMenu.innerHTML = "&#8942;";


        const dropdownMenu = document.createElement("ul");
        dropdownMenu.className = "dropdown-menu dropdown-menu-end";

        const eliminarItem = document.createElement("li");
        const eliminarLink = document.createElement("a");
        eliminarLink.className = "dropdown-item";
        eliminarLink.href = "#";
        eliminarLink.textContent = "Eliminar mensaje";
        eliminarLink.onclick = function(e) {
            e.preventDefault();
            eliminarMensaje(message.id, idComunidad, urlImage);
        };

        eliminarItem.appendChild(eliminarLink);
        dropdownMenu.appendChild(eliminarItem);

        menuContainer.appendChild(btnMenu);
        menuContainer.appendChild(dropdownMenu);

        container.appendChild(menuContainer);
    }


    return container;
}



function sincronizarSpotify(payload) {
    // Usamos la Spotify Web API para sincronizar la reproducción
    console.log("entrooo" + payload);
    const info = payload

    console.log(info.uris);
    const startTime = Date.now();

    const token = document.getElementById("token").value // Token OAuth del usuario

    fetch(`https://api.spotify.com/v1/me/player/play`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            uris: info.uris,
            offset: { uri: info.uriInicio },  // esta es la canción por la que querés empezar
            position_ms: info.positionMs // Asegúrate de que sea 'position_ms' con guion bajo
        })
    })
        .then(data => {
            // Calcular la latencia
            const endTime = Date.now();
            const latency = endTime - startTime;

            // Sumamos la latencia a positionMs
            const adjustedPositionMs = info.positionMs + latency;

            console.log('Reproduciendo en Spotify:', data);
            console.log('Latencia:', latency);
            console.log('Posicion ajustada:', adjustedPositionMs);

            // Puedes usar adjustedPositionMs para futuras sincronizaciones si es necesario.
        }) // Muestra la respuesta de Spotify
        .catch(error => console.error('Error al sincronizar la canción:', error));

}
function marcarUsuarioComoActivo(username) {
    console.log("user:" + username);
    const usuarios = document.querySelectorAll('li');
    usuarios.forEach(li => {
        const nombre = li.querySelector('strong')?.textContent?.trim();
        if (nombre === username) {
            const indicador = li.querySelector('.status-indicator');
            if (indicador) {
                indicador.classList.remove('bg-danger');
                indicador.classList.add('bg-success');
            }
        }
    });
}


messageForm.addEventListener("click", send, true);
window.addEventListener("load", function () {
    const btn = document.getElementById("sincronizar");
    const sincronizarse = document.getElementById("sincronizarse");
    if (btn) {
        btn.addEventListener("click", conectarAlCanalDeSincronizacion);
    }

    if (sincronizarse) {
        sincronizarse.addEventListener("click", async () => {
            let sincronizacion = await obtenerUnaSincronizacion();
            sincronizarSpotify(sincronizacion);
        });
    }

// se usa await que espera que la promesa termine
    (async () => {
        let estado = await existeUsuarioEnLaComunidad();
        console.log("existe:" + estado);

        if (estado) {
            connect();
        }

    })();


});



async function obtenerUnaSincronizacion() {
    const comunidad = document.getElementById("comunidad").value;
    const username = document.getElementById("username").value;
    const idUser = document.getElementById("id").value;

    // Esto asume que ChatMessage espera algo como: sender, content, etc.
    const message = {
        sender: username,
        content: "",
        id: idUser
    };

    try {
        const response = await fetch(`/spring/sincronizarme/${comunidad}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(message)
        });

        if (!response.ok) {
            throw new Error(`Error de red: ${response.status}`);
        }

        const data = await response.json();
        console.log("Sincronización recibida:", data);


        return data;

    } catch (error) {
        console.error("Error al sincronizar:", error);
    }
}

//funcion asincrona para verificar si el usuario existe en la comunidad
//devuelve una promesa (el resultado de si el usuario existe en la comunidad)

async function existeUsuarioEnLaComunidad() {
    let idUsuario = document.getElementById("id").value;
    let idComunidad = document.getElementById("comunidad").value;

    try {
        let response = await fetch(`/spring/usuario-en-comunidad/${idUsuario}/${idComunidad}`);
        if (!response.ok) {
            throw new Error(`Error: ${response.status}`);
        }
        let data = await response.text();
        console.log("Usuario en comunidad existe:", data);
        return true;
    } catch (error) {
        console.error("No se pudo verificar existencia:", error);
        return false;
    }
}

function mostrarPlayer(canciones,idComunidad) {
    const audioPlayer = document.getElementById("audioPlayer");
    const playlist = document.getElementById("playlist-escucha");
    const playerContainer = document.getElementById("playerContainer");
    const footer = document.getElementById("footer");

    playlist.innerHTML = "";

    canciones.forEach((c, idx) => {
        const li = document.createElement("li");
        li.textContent = `Canción ${idx + 1}: ${c.titulo}`;
        playlist.appendChild(li);
    });

    playerContainer.style.display = "block";
    footer.style.display = "flex";

    let indiceActual = 0;
    let segundosReproducidos = 0;

    function reproducirActual() {
        const cancionActual = canciones[indiceActual];
        audioPlayer.src = cancionActual.ruta;
        audioPlayer.play();

        actualizarFooterCancion({
            urlImagen: cancionActual.urlPortada,
            artista: cancionActual.artista || "Desconocido",
            titulo: cancionActual.titulo,
            progreso: 0,
            duracion: (cancionActual.duracion || 0) * 1000
        });

        Array.from(playlist.children).forEach((li, idx2) => {
            li.style.fontWeight = idx2 === indiceActual ? "bold" : "normal";
        });
    }

    function onSongEnded() {
        indiceActual++;
        segundosReproducidos = 0;
        console.log("Canción finalizada. Reproduciendo la siguiente...");

        if (stompClient && idComunidad) {
            console.log("Enviando actualización de estado a la comunidad:", idComunidad);
            stompClient.send(
                `/app/preescucha.actualizarEstado.${idComunidad}`,
                {},
                JSON.stringify({
                    type: 'ACTUALIZAR_ESTADO',
                    indiceActual: indiceActual,
                    segundosReproducidos: segundosReproducidos
                })
            );

        } else {
            console.error("stompClient o idComunidad no están definidos. No se puede enviar el estado.");
        }


        if (indiceActual < canciones.length) {
            reproducirActual();
        } else {
            console.log("Preescucha finalizada");
            playerContainer.style.display = "none";
            footer.style.display = "none";
        }
    }

    // Limpia listeners anteriores por seguridad
    audioPlayer.onended = null;
    audioPlayer.removeEventListener('ended', onSongEnded);
    audioPlayer.addEventListener('ended', onSongEnded);

    reproducirActual();
}




document.getElementById("reproduccionPreescucha").addEventListener("click", function() {
    const idComunidad = document.getElementById("comunidad").value;
    const idPreescucha = document.getElementById("preescucha").value;

    fetch(`/spring/api/preescucha/${idPreescucha}/canciones`)
        .then(response => response.json())
        .then(canciones => {
            console.log("Canciones recibidas:", canciones);

            stompClient.send(
                `/app/preescucha.iniciar.${idComunidad}`,
                {},
                JSON.stringify({
                    type: "PREESCUCHA",
                    data: {
                        idComunidad: idComunidad,
                        canciones: canciones
                    }
                })
            );
        })
        .catch(error => console.error("Error al traer canciones:", error));
});


function actualizarFooterCancion(dt){
    const colorThief = new ColorThief();
    const imgElement = document.getElementById("imageTrack");
    if (!imgElement) return;

    imgElement.crossOrigin = "anonymous";
    imgElement.src = dt.urlImagen;

    console.log("datos",dt);

    document.getElementById("artista").textContent = dt.artista;
    document.getElementById("titulo").textContent = dt.titulo;


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

        const luminance = 0.299 * color1[0] + 0.587 * color1[1] + 0.114 * color1[2];

        console.log("luminaria",luminance);
        if (luminance > 162) {
            footer.style.setProperty("color", "black", "important");

        } else {

            footer.style.setProperty("color", "white", "important");

        }
    }
}



