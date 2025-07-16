package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Audio;
import com.tallerwebi.dominio.RepositorioAudio;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioAudioImpl implements RepositorioAudio {

    @Autowired
    private SessionFactory sessionFactory;
    public RepositorioAudioImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Audio audio) {
        sessionFactory.getCurrentSession().save(audio);
    }
}
