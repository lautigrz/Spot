package com.tallerwebi.dominio;

import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.resources.preference.Preference;
import com.mercadopago.resources.preference.PreferenceBackUrls;
import org.springframework.stereotype.Service;
import com.mercadopago.resources.*;
import com.mercadopago.resources.preference.PreferenceItem;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class ServicioMercadoPago {

    public Preference crearPreferenciaPago(String titulo, BigDecimal precio, String successUrl,
                                           String failureUrl, String albumId,Long usuarioId, Long preescuchaId,
                                           String notificationUrl) throws Exception{
        PreferenceItemRequest item =
                PreferenceItemRequest.builder()
                        .title(titulo)
                        .quantity(1)
                        .unitPrice(precio)
                        .build();

        System.out.println("successUrl = " + successUrl);
        System.out.println("failureUrl = " + failureUrl);

        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success(successUrl)
                .failure(failureUrl)
                .build();

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("usuarioId", usuarioId);
        metadata.put("preescuchaId", preescuchaId);


        PreferenceRequest preferenceRequest =
                PreferenceRequest.builder()
                        .items(Collections.singletonList(item))
                        .backUrls(backUrls)
                        .notificationUrl(notificationUrl)
                        .externalReference(albumId)
                        .metadata(metadata)
                        .autoReturn("approved")
                        .build();

        PreferenceClient cliente = new PreferenceClient();

        try {
            return cliente.create(preferenceRequest);
        } catch (MPApiException e) {
            System.out.println("MPApiException: " + e.getApiResponse().getContent());
            throw e;
        }
    }
}
