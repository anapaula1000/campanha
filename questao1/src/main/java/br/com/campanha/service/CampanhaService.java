package br.com.campanha.service;

import br.com.campanha.domain.Campanha;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface para o serviço de campanha
 * @author : Ana Paula  anapaulasilva1000@gmail.com
 */
public interface CampanhaService {

    List<Campanha> buscarTodasAsCampanhasAtivas(LocalDate hoje);

    List<Campanha> buscarCampanhasAtivasPorPeriodo(LocalDate inicioVigencia, LocalDate fimVigencia);

    Campanha cadastrarCampanha(@NotNull String nomeDaCampanha, @NotNull String idDoTime,
                               @NotNull LocalDate inicioVigencia, @NotNull LocalDate fimVigencia);

    Optional<Campanha> buscarPorId(String id);

    void deletarPorId(String id);

    void salvarCampanha(Campanha campanha);

    List<Campanha> buscaPorTime(String time);

}
