package br.com.campanha.api.domain;

import br.com.campanha.domain.Campanha;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.google.common.base.MoreObjects;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * @author : Ana Paula  anapaulasilva1000@gmail.com
 */

@ApiModel(value="CampanhaRes", description="Valida os dados da campanha que devem ser recebidos e retornados pela API Rest de campanha")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CampanhaRes extends ResourceSupport {

    @Size(min=10, max=100, message="Nome da campanha deve ter entre de 10 a 100 caracteres.")
    @NotNull(message="Insira um nome para a campanha.")
    @ApiModelProperty(value = "Nome da campanha", dataType = "string", required = true)
    private String nome;
    
    @Size(min=5, max=100, message="Nome do time deve ter entre de 5 a 100 caracteres.")
    @NotNull(message="Insira um nome para o time.")
    @ApiModelProperty(value = "Id do time", dataType = "string", required = true)
    private String timeId;

    @NotNull(message="Insira a data de inicio da vigência para a campanha")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @ApiModelProperty(value = "Data de inicio de vigência da campanha", dataType = "date", required = true)
    private LocalDate inicioVigencia;

    @NotNull(message="Insira a data de fim da vigência para a campanha")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @ApiModelProperty(value = "Data de fim de vigência", dataType = "date", required = true)
    private LocalDate fimVigencia;

    @JsonIgnore
    private String chave;

    public CampanhaRes() {
    }

    public CampanhaRes(Campanha campanha) {
        this.nome = campanha.getNome();
        this.timeId = campanha.getTimeId();
        this.inicioVigencia = campanha.getInicioVigencia();
        this.fimVigencia = campanha.getFimVigencia();
        this.chave = campanha.getId();
    }

    public CampanhaRes(String nome, String timeId, LocalDate inicioVigencia, LocalDate fimVigencia) {
        this.nome = nome;
        this.timeId = timeId;
        this.inicioVigencia = inicioVigencia;
        this.fimVigencia = fimVigencia;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("nome", nome)
                .add("timeId", timeId)
                .add("inicioVigencia", inicioVigencia)
                .add("fimVigencia", fimVigencia)
                .toString();
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTimeId() {
        return timeId;
    }

    public void setTimetimeIdId(String timeCoracaoId) {
        this.timeId = timeCoracaoId;
    }

    public LocalDate getInicioVigencia() {
        return inicioVigencia;
    }

    public void setInicioVigencia(LocalDate inicioVigencia) {
        this.inicioVigencia = inicioVigencia;
    }

    public LocalDate getFimVigencia() {
        return fimVigencia;
    }

    public void setFimVigencia(LocalDate fimVigencia) {
        this.fimVigencia = fimVigencia;
    }

    public String getChave() {
        return chave;
    }
}
