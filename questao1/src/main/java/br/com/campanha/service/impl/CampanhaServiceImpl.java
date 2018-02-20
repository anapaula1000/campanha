package br.com.campanha.service.impl;

import br.com.campanha.domain.Campanha;
import br.com.campanha.repository.CampanhaRep;
import br.com.campanha.service.CampanhaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Serviço para funcionalidades relacionadas a Campanha
 * @author : Ana Paula  anapaulasilva1000@gmail.com
 */
@Service
@Validated
public class CampanhaServiceImpl implements CampanhaService {

    private static final Logger logger = LoggerFactory.getLogger(CampanhaService.class);

    private static final int NUMERO_DIAS = 1;

    @Autowired
    private CampanhaRep campanhaRepository;

    /**
     * Busca todas as campanhas ativas (Quando a data de fim de vigência for menor ou igual a data do parametro)
     * @param hoje(LocalDate) - data que será usada como data de corte para buscar as campanhas ativas
     * @return List<Campanha> - Lista de campanhas ativas
     */
    @Override
    public List<Campanha> buscarTodasAsCampanhasAtivas(LocalDate hoje) {
        return campanhaRepository.buscarTodasAsCampanhasAtivas(hoje);
    }

    /**
     * Busca todas as campanhas cuja a vigência estiver dentro das datas passadas no parametro
     * @param inicioVigencia - Data de inicio de vigência das campanhas a serem buscadas
     * @param fimVigencia - Data de fim de vigência das campanhas a serem buscadas
     * @return List<Campanha> - Lista de campanhas ativas
     */
    @Override
    public List<Campanha> buscarCampanhasAtivasPorPeriodo(LocalDate inicioVigencia, LocalDate fimVigencia) {
        return campanhaRepository.buscarCampanhasAtivasPorPeriodo(inicioVigencia, fimVigencia);
    }

    @Override
    public Campanha cadastrarCampanha(String nomeDaCampanha, String idDoTime, LocalDate inicioVigencia, LocalDate fimVigencia) {

        if(logger.isDebugEnabled()) {
            logger.debug("Cadastrando Campanha com nome {} - Data de Inicio Vigência: {}  - Data de Fim Vigência : {} ",
                    nomeDaCampanha, inicioVigencia, fimVigencia);
        }

        List<Campanha> campanhas = campanhaRepository.buscarCampanhasAtivasPorPeriodo(inicioVigencia, fimVigencia);
        Collections.sort(campanhas, Comparator.comparing(Campanha::getFimVigencia));

        campanhas.forEach(campanhaCadastrada -> {
            campanhaCadastrada.setFimVigencia(campanhaCadastrada.getFimVigencia().plusDays(NUMERO_DIAS));
            adicionaDiaAoFimVigenciaRecursivo(campanhaCadastrada, campanhas);
        } );

        campanhaRepository.save((Campanha)campanhas);
        return campanhaRepository.save(new Campanha(nomeDaCampanha, idDoTime, inicioVigencia, fimVigencia));
    }

    @Override
    public Optional<Campanha> buscarPorId(String id) {
        return Optional.ofNullable(campanhaRepository.findOne(id));
    }

    @Override
    public void deletarPorId(String id) {
        campanhaRepository.deleteById(id);
    }

    @Override
    public void salvarCampanha(Campanha campanha) {
        campanhaRepository.save(campanha);
    }

    @Override
    public List<Campanha> buscaPorTime(String time) {
        return campanhaRepository.buscaTimeId(time, LocalDate.now(), LocalDate.now());
    }

    /**
     * @param campanha - Campanha que vai ser comparadas com as outras campanhas para se validar a necessidade de se
     *                   adicionar mais um dia no final de vigência da campanha.
     *
     * @param campanhasCadastradas - Listas de campanhas cadastras com vigência dentro da vigência da nova campanha a ser cadastrada
     */
    private void adicionaDiaAoFimVigenciaRecursivo(Campanha campanha, List<Campanha> campanhasCadastradas){
        if(campanhasCadastradas.stream()
                .filter(campanhaCadastrada -> !campanhaCadastrada.equals(campanha))
                .anyMatch(campanhaCadastrada -> campanhaCadastrada.getFimVigencia().isEqual(campanha.getFimVigencia()))){

            if(logger.isDebugEnabled()) {
                logger.debug("Adcionando fim de vigência na Campanha: {}", campanha);
            }
            campanha.setFimVigencia(campanha.getFimVigencia().plusDays(NUMERO_DIAS));
            adicionaDiaAoFimVigenciaRecursivo(campanha, campanhasCadastradas);
        }
    }
}