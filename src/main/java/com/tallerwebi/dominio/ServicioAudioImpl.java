package com.tallerwebi.dominio;

import com.mpatric.mp3agic.Mp3File;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    @Override
    public Long duracionDeAudio(String rutaRelativa) throws Exception {

        String rutaOriginal = rutaRelativa;


        if (rutaOriginal.startsWith("\\spring")) {
            rutaOriginal = rutaOriginal.substring(7);
        } else if (rutaOriginal.startsWith("/spring")) {
            rutaOriginal = rutaOriginal.substring(7);
        }

        String basePath = System.getProperty("user.dir");
        File archivo = new File(basePath, rutaOriginal);

        if (!archivo.exists()) {
            throw new Exception("Archivo no encontrado: " + archivo.getAbsolutePath());
        }

        Mp3File mp3file = new Mp3File(archivo);
        if (mp3file.hasId3v2Tag()) {
            int durationInSeconds = (int) mp3file.getLengthInSeconds();
            return Long.valueOf(durationInSeconds);
        } else {
            throw new Exception("Archivo MP3 no tiene ID3v2 tag");
        }
    }


}
