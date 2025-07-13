package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
public class ServicioAudioImpl implements ServicioAudio {

    private RepositorioAudio repositorioAudio;

    public ServicioAudioImpl(RepositorioAudio repositorioAudio) {
        this.repositorioAudio = repositorioAudio;
    }


    @Override
    public void guardar(List<MultipartFile> audios, String url) {

    }
}
