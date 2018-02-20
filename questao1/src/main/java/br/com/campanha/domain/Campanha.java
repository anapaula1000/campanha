package br.com.campanha.domain;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import br.com.campanha.api.domain.CampanhaRes;
import io.swagger.annotations.ApiModelProperty;

/**
 * Entidade representa os dados do documento MongoDB de Campanhas.
 * @author : Ana Paula  anapaulasilva1000@gmail.com
 */
@Document
public class Campanha {

    @Id
    private String id;

    @Indexed
    @Size(min=10, max=100, message="Nome da campanha deve ter entre de 10 a 100 caracteres.")
    @NotNull(message="Insira um nome para a campanha.")
    @ApiModelProperty(value = "Nome da campanha", dataType = "string", required = true)
    private String nome;

    @Indexed
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

    public Campanha(String nome, String timeId, LocalDate inicioVigencia, LocalDate fimVigencia) {
        this.nome = nome;
        this.timeId = timeId;
        this.inicioVigencia = inicioVigencia;
        this.fimVigencia = fimVigencia;
    }

    public Campanha() {
    }

    public void atualizarDados(CampanhaRes CampanhaRes){
        setNome(CampanhaRes.getNome());
        setTimeId(CampanhaRes.getTimeId());
        setInicioVigencia(CampanhaRes.getInicioVigencia());
        setFimVigencia(CampanhaRes.getFimVigencia());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setTimeId(String timeId) {
        this.timeId = timeId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Campanha campanha = (Campanha) o;
        return Objects.equals(getNome(), campanha.getNome()) &&
                Objects.equals(getTimeId(), campanha.getTimeId()) &&
                Objects.equals(getInicioVigencia(), campanha.getInicioVigencia()) &&
                Objects.equals(getFimVigencia(), campanha.getFimVigencia());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNome(), getTimeId(), getInicioVigencia(), getFimVigencia());
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("nome", nome)
                .add("timeId", timeId)
                .add("inicioVigencia", inicioVigencia)
                .add("fimVigencia", fimVigencia)
                .toString();
    }
}