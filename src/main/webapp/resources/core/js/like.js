document.addEventListener('DOMContentLoaded', () => {



    const usuarioElem = document.getElementById('usuarioID');
    const artistaElem = document.getElementById('artistaID');

    const idUsuario = usuarioElem ? usuarioElem.value : null;
    const artistaID = artistaElem ? artistaElem.value : null;

    const idFinal = idUsuario || artistaID || null;

    const botonesLike = document.querySelectorAll('.btn-like');

    botonesLike.forEach(btn => {
        const likeCountSpan = btn.querySelector('.like-count');
        const likeText = btn.querySelector('.like-text');
        const icon = btn.querySelector('i');

        let likes = parseInt(likeCountSpan.textContent);

        btn.addEventListener('click', () => {
            const postId = btn.getAttribute('data-post-id');

            btn.classList.toggle('liked');
            if (btn.classList.contains('liked')) {
                icon.className = 'bi bi-hand-thumbs-up-fill me-2';
                likeText.textContent = 'Te gusta';

                likes++;
                likeCountSpan.style.color = '#fff';

                darLikeYGuardarlo(postId, idFinal);
            } else {
                icon.className = 'bi bi-hand-thumbs-up me-2';
                likeText.textContent = 'Me gusta';
                btn.style.backgroundColor = '#f5f5f5';
                btn.style.color = '#333';
                btn.style.borderColor = '#ddd';
                likes--;
                likeCountSpan.style.color = '#333';

                removerLikeDeLaBd(postId, idFinal);
            }

            likeCountSpan.textContent = likes;

            btn.classList.add('pop');
            btn.addEventListener('animationend', () => {
                btn.classList.remove('pop');
            }, { once: true });
        });


        if (btn.classList.contains('liked')) {
            icon.className = 'bi bi-hand-thumbs-up-fill me-2';
            likeText.textContent = 'Te gusta';

        }

    });





});

function removerLikeDeLaBd(postId, idUsuario) {
    fetch(`/spring/dislike/${postId}/${idUsuario}`, {
        method: 'POST'
    })
        .then(res => {
            if (!res.ok) throw new Error('Error al quitar like en el posteo con ID: ' + postId);
            return res.json();
        })
        .then(data => {
            console.log(data.mensaje);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function darLikeYGuardarlo(postId, idUsuario) {
    fetch(`/spring/like/${postId}/${idUsuario}`, {
        method: 'POST'
    })
        .then(res => {
            if (!res.ok) throw new Error('Error al dar like en el posteo con ID: ' + postId);
            return res.json();
        })
        .then(data => {
            console.log(data.mensaje);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
