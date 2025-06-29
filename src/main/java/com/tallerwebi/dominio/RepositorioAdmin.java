package com.tallerwebi.dominio;

public interface RepositorioAdmin {
    void hacerAdminAUnMiembro(Long idComunidad, Long idMiembro);
    void eliminarMiembroDeComunidad(Long idComunidad, Long idMiembro);
    void eliminarMensajeDeComundad(Long idComunidad, Long idMensaje);
}
