package com.tallerwebi.presentacion.dto;

import com.tallerwebi.dominio.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostLikeDto {
    private Post post;
    private boolean liked;
}
