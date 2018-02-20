package br.com.campanha.webhook.repository;

import br.com.campanha.webhook.domain.Webhook;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author : Ana Paula  anapaulasilva1000@gmail.com
 */
public interface WebhookRepository  extends MongoRepository<Webhook, String> {
}
