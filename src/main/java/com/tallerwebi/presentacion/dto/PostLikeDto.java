package com.tallerwebi.presentacion.dto;

import com.tallerwebi.dominio.Comentario;
import com.tallerwebi.dominio.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostLikeDto {
    private Post post;
    private boolean liked;
    private List<Comentario> comentarios;

}
