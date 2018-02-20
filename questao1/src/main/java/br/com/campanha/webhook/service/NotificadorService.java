package br.com.campanha.webhook.service;

import br.com.campanha.api.domain.CampanhaRes;
import feign.Headers;
import feign.RequestLine;

/**
 * @author : Ana Paula  anapaulasilva1000@gmail.com
 */
public interface NotificadorService {

    @RequestLine("POST")
    @Headers("Content-Type: application/json")
    void notifica(CampanhaRes campanhaResource);
}
