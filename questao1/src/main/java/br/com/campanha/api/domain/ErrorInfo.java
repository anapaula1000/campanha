package br.com.campanha.api.domain;

import com.google.common.base.MoreObjects;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Trata as mensagens de erro/excecao
 * @author : Ana Paula  anapaulasilva1000@gmail.com
 */
@ApiModel(value="ErrorInfo", description="Trata as mensagens de erro/exceção que serão retornadas pela API")
public class ErrorInfo {

    @ApiModelProperty(value = "URL:",dataType = "string", required = true)
    public final String url;

    @ApiModelProperty(value = "Exceção: ",dataType = "string", required = true)
    public final String ex;

    public ErrorInfo(String url, Exception ex) {
        this.url = url;
        this.ex = ex.getLocalizedMessage();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("url", url)
                .add("ex", ex)
                .toString();
    }
}