package br.com.campanha.webhook.service;

import br.com.campanha.domain.Campanha;
import br.com.campanha.webhook.domain.Webhook;
import org.springframework.stereotype.Service;

/**
 * @author : Ana Paula  anapaulasilva1000@gmail.com
 */
@Service
public interface WebhookService {
    Webhook cadastrarWebhook(String url, String chaveAcesso);
    void avisaaTodosSobreAtualizacao(Campanha campanha);
}
