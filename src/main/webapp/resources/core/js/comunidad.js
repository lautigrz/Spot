
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


    stompClient.send(
        `/app/chat.register/${datos.idComunidad}`,
        {},
        JSON.stringify({sender: datos.username, type: "JOIN"}),
    );


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

            mensajeDiv.innerHTML = "<em>Este mensaje fue eliminado</em>";

            mensajeDiv.classList.add("text-muted", "fst-italic");
        }
    }

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

    const strong = document.createElement("strong");
    strong.textContent = message.sender;


    const fecha = new Date();
    const small = document.createElement("small");
    small.className = "text-muted";
    small.innerText = fecha.toLocaleDateString("en-GB", {
        day: "2-digit", month: "short", year: "numeric",
    }) + " · " + fecha.toLocaleTimeString("en-US", {
        hour: "2-digit", minute: "2-digit"
    });


    const p = document.createElement("p");
    p.className = "mb-0 d-inline";
    p.textContent = " " + message.content;


    const heartIcon = document.createElement("i");
    heartIcon.className = "fa-regular fa-heart ms-3 corazon";
    heartIcon.title = "Like";
    heartIcon.style.cursor = "pointer";


    textDiv.appendChild(strong);
    textDiv.appendChild(document.createElement("br"));
    textDiv.appendChild(small);
    textDiv.appendChild(p);

    contentWrapper.appendChild(textDiv);
    contentWrapper.appendChild(heartIcon);

    container.appendChild(img);
    container.appendChild(contentWrapper);

    console.log("Rol:" + rol)
    if (rol === 'Admin') {
        var idComunidad = document.getElementById("comunidad").value;
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
            eliminarMensaje(message.id, idComunidad);
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
document.addEventListener('DOMContentLoaded', function() {
    // Tu listener de corazón
    const heartIcons = document.querySelectorAll('.corazon');
    heartIcons.forEach(heartIcon => {
        heartIcon.addEventListener('click', () => {
            if (heartIcon.classList.contains('text-danger')) {
                heartIcon.classList.remove('fas', 'fa-heart', 'ms-3', 'text-danger');
                heartIcon.classList.add('fa-regular', 'fa-heart', 'ms-3');
            } else {
                heartIcon.classList.remove('fa-regular', 'fa-heart', 'ms-3');
                heartIcon.classList.add('fas', 'fa-heart', 'ms-3', 'text-danger');
            }
        });
    });


});


