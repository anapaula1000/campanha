package br.com.campanha.webhook.exception;

public class WebhookCadastradoException  extends RuntimeException  {

    /**
	 * @author : Ana Paula  anapaulasilva1000@gmail.com
	 */
	private static final long serialVersionUID = 1L;

	public WebhookCadastradoException() {
        super("Webhook(URL) já cadastrado");
    }
}