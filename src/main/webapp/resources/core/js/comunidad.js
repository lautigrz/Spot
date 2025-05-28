"user static";

var username = document.getElementById("username").value;
var idUser = document.getElementById("id").value;


var messageInput = document.querySelector("#mensajeInput");
var messageArea = document.querySelector("#mensajeVista");
var messageForm = document.getElementById("enviar");
var urlImage = document.getElementById("urlFoto").value;
var idComunidad = document.getElementById("comunidad").value;
var stompClient = null;


function connect() {



        var socket = new SockJS("/spring/websocket");
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected);
        // ← solo se llama cuando realmente se conecta



}
function onConnected(options) {

   // stompClient.subscribe("/topic/public", onMessageReceived);
    stompClient.subscribe(`/topic/${idComunidad}`, onMessageReceived);
  //  stompClient.subscribe("/user/queue/playback", sincronizarSpotify);

    stompClient.send(
        `/app/chat.register/${idComunidad}`,
        { id: idUser},
        JSON.stringify({sender: username ,type: "JOIN"}),
    );

}


window.onload = function() {
    const btn = document.getElementById("sincronizar");
    if (btn) {
        btn.addEventListener("click", conectarAlCanalDeSincronizacion);
    }
};

function conectarAlCanalDeSincronizacion(){

    stompClient.subscribe("/user/queue/playback", sincronizarSpotify);

    stompClient.send(
        "/app/chat.repro",
        {},
        JSON.stringify({sender: username ,type: "JOIN"}),
    )
}

function send(event){
    var message = messageInput.value.trim();
    if(message && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            image: urlImage,
            type: "CHAT",
        };
        stompClient.send("/app/chat.send", {"id": idUser}, JSON.stringify(chatMessage));
        messageInput.value = "";

    }
    event.preventDefault();
}

function onMessageReceived(payload) {

    var message = JSON.parse(payload.body);

    if(message.content && message.content.trim() !== "") {
        const nuevoMensaje = crearMensajeHTML(message);
        messageArea.appendChild(nuevoMensaje);
        messageArea.scrollTop = messageArea.scrollHeight;
    }



}

function crearMensajeHTML(message) {
    // Crear contenedor principal
    const container = document.createElement("div");
    container.className = "chat-message p-3 d-flex align-items-start my-2 bg-light rounded";

    // Imagen de usuario
    const img = document.createElement("img");
    img.src = message.image || "default.jpg";
    img.alt = "User";
    img.className = "rounded-circle me-3";
    img.style.width = "50px";
    img.style.height = "50px";
    img.style.objectFit = "cover";

    // Contenedor del texto
    const contentWrapper = document.createElement("div");
    contentWrapper.className = "d-flex align-items-center flex-grow-1 ";

    const textDiv = document.createElement("div");
    textDiv.className = "flex-grow-1";
    // Usuario
    const strong = document.createElement("strong");
    strong.textContent = message.sender;

    // Fecha
    const fecha = new Date();
    const small = document.createElement("small");
    small.className = "text-muted";
    small.innerText = fecha.toLocaleDateString("en-GB", {
        day: "2-digit", month: "short", year: "numeric",
    }) + " · " + fecha.toLocaleTimeString("en-US", {
        hour: "2-digit", minute: "2-digit"
    });

    // Mensaje
    const p = document.createElement("p");
    p.className = "mb-0 d-inline";
    p.textContent = " " + message.content;

    // Like
    const heartIcon = document.createElement("i");
    heartIcon.className = "fa-regular fa-heart ms-3 corazon";
    heartIcon.title = "Like";
    heartIcon.style.cursor = "pointer";

    // Armado del árbol
    textDiv.appendChild(strong);
    textDiv.appendChild(document.createElement("br"));
    textDiv.appendChild(small);
    textDiv.appendChild(p);

    contentWrapper.appendChild(textDiv);
    contentWrapper.appendChild(heartIcon);

    container.appendChild(img);
    container.appendChild(contentWrapper);

    return container;
}

function sincronizarSpotify(payload) {
    // Usamos la Spotify Web API para sincronizar la reproducción
    console.log("entrooo");
    const info = JSON.parse(payload.body);

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
            console.log('Posición ajustada:', adjustedPositionMs);

            // Puedes usar adjustedPositionMs para futuras sincronizaciones si es necesario.
        }) // Muestra la respuesta de Spotify
        .catch(error => console.error('Error al sincronizar la canción:', error));

}


messageForm.addEventListener("click", send, true);
window.addEventListener("load", function () {
    connect();
});

document.addEventListener('DOMContentLoaded', function() {
    const heartIcons = document.querySelectorAll('.corazon');
    heartIcons.forEach(heartIcon => {
        heartIcon.addEventListener('click', () => {

            if(heartIcon.classList.contains('text-danger')) {
                heartIcon.classList.remove('fas', 'fa-heart', 'ms-3', 'text-danger');
                heartIcon.classList.add('fa-regular', 'fa-heart', 'ms-3');

            }else{
                heartIcon.classList.remove('fa-regular', 'fa-heart', 'ms-3');
                heartIcon.classList.add('fas', 'fa-heart', 'ms-3', 'text-danger');
            }


        });
    });
});

