package com.tallerwebi.dominio;

public interface ServicioAdmin {
    void hacerAdminAUnMiembro(Long idComunidad, Long idMiembro);
    void eliminarMiembroDeComunidad(Long idComunidad, Long idMiembro);
    void eliminarMensajeDeComundad(Long idComunidad, Long idMensaje);
}
