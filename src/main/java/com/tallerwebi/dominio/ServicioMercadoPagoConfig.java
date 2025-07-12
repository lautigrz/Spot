package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;
import com.mercadopago.MercadoPagoConfig;
import javax.annotation.PostConstruct;

@Service
public class ServicioMercadoPagoConfig {
    @PostConstruct
    public void init() throws Exception{
        MercadoPagoConfig.setAccessToken("APP_USR-3263206858514125-051615-1d525d361fd3448675a2d3edeb67032d-2441404485");
    }

}