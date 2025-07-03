package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;
import com.mercadopago.MercadoPagoConfig;
import javax.annotation.PostConstruct;

@Service
public class ServicioMercadoPagoConfig {
    @PostConstruct
    public void init() throws Exception{
        MercadoPagoConfig.setAccessToken("APP_USR-1079416156893757-062912-47f33f73dd02effe79fa45bff0eeb84a-2521568719");
    }

}