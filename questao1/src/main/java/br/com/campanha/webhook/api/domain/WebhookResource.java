package br.com.campanha.webhook.api.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.MoreObjects;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Representa os dados do Webhook que devem ser recebidos pela API Rest de webhook
 * @author : Ana Paula  anapaulasilva1000@gmail.com
 */
@ApiModel(value="WebhookResource", description="Representa os dados do Webhook que devem ser recebidos pela API Rest de webhook")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebhookResource {

    @Size(min=10, max=200, message="Url tem tamanho de 10 a 200 caracteres.")
    @NotNull(message="Insira a URL")
    @ApiModelProperty(value = "URL do Webhook", dataType = "string", required = true)
    private String url;

    @ApiModelProperty(value = "Chave de acesso para a URL", dataType = "string", required = false)
    private String chaveAcesso;

    public WebhookResource() {
    }

    public WebhookResource(String url, String chaveAcesso) {
        this.url = url;
        this.chaveAcesso = chaveAcesso;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getChaveAcesso() {
        return chaveAcesso;
    }

    public void setChaveAcesso(String chaveAcesso) {
        this.chaveAcesso = chaveAcesso;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("url", url)
                .toString();
    }
}
