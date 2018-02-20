package br.com.campanha.service.impl;

import br.com.campanha.domain.Campanha;
import br.com.campanha.fixture.CampanhaGenerator;
import br.com.campanha.repository.CampanhaRep;
import br.com.campanha.service.CampanhaService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

/**
 * Testes referente ao serviço de campanha
 *
 * @author : Ana Paula  anapaulasilva1000@gmail.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CampanhaServiceTest {

    @Autowired
    private CampanhaService campanhaService;

    @Autowired
    private CampanhaRep campanhaRepository;

    @Before
    public void setUp() throws Exception {
        campanhaRepository.deleteAll();
        campanhaRepository.save((Campanha) CampanhaGenerator.getCampanhas());
    }

    /**
     * Testa o requisito 2 -
     * caso exista uma campanha ou N campanhas associadas naquele período, o sistema deverá somar um dia no término da vigência de cada campanha já existente.*
     * Caso a data final da vigência seja igual a outra campanha, deverá ser acrescido um dia a mais de forma que as campanhas não tenham a mesma data de término de vigência.
     * @throws Exception
     */
    @Test
    public void campanhaDeveSerCadastradasComDadosCorretos(){
        Campanha campanha = campanhaService.cadastrarCampanha("Campanha 3", "TIME-1003", LocalDate.of(2018,04,21),LocalDate.of(2018,05,20));

        assertThat(campanha)
                .as("A campanha 3 deve ser cadastrada na base de dados e ter os dados corretos conforme os parametros")
                .isNotNull()
                .extracting("nome", "timeId", "inicioVigencia", "fimVigencia")
                .contains("Campanha 3", "TIME-1003", LocalDate.of(2018,04,21),LocalDate.of(2018,05,20));


        assertThat(campanhaRepository.findAll())
                .as("As campanhas 1 e 2 devem ter seus dados atualizados conforme regras de data de fim vigência")
                .extracting("nome", "timeCoracaoId", "inicioVigencia", "fimVigencia")
                .contains(tuple("Campanha 1",  "TIME-1001", LocalDate.of(2018,02,21),LocalDate.of(2018,03,20)),
                        tuple("Campanha 2",  "TIME-1002", LocalDate.of(2018,03,21),LocalDate.of(2018,04,20)),
                        tuple("Campanha 3",  "TIME-1003", LocalDate.of(2018,04,21),LocalDate.of(2018,05,20)));

    }

    @Test
    public void buscarCampanhasAtivasPorPeriodo(){

    	 assertThat(campanhaService.buscarCampanhasAtivasPorPeriodo(LocalDate.of(2018, 03, 01), LocalDate.of(2018, 04, 01)))
         .as("Deve trazer somente uma Campanha que está dentro da vigência deste período")
         .hasSize(1);

    	 assertThat(campanhaService.buscarCampanhasAtivasPorPeriodo(LocalDate.of(2018, 03, 01), LocalDate.of(2018, 05, 01)))
         .as("Deve trazer somente uma Campanha que está dentro da vigência deste período")
         .hasSize(2);

    	 assertThat(campanhaService.buscarCampanhasAtivasPorPeriodo(LocalDate.of(2018, 03, 01), LocalDate.of(2018, 06, 01)))
         .as("Deve trazer somente uma Campanha que está dentro da vigência deste período")
         .hasSize(2);
    }

}
