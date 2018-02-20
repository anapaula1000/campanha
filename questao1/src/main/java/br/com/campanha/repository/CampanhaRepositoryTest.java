package br.com.campanha.repository;

import br.com.campanha.domain.Campanha;
import br.com.campanha.fixture.CampanhaGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author : Ana Paula  anapaulasilva1000@gmail.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CampanhaRepositoryTest {

    @Autowired
    private CampanhaRep CampanhaRep;

    @Before
    public void setUp() throws Exception {
        CampanhaRep.deleteAll();
        CampanhaRep.save((Campanha)CampanhaGenerator.getCampanhas());
    }

    @Test
    public void deveTrazerCampanhasComDataDeFimVigenciaoSuperiorADataDoRecebida() throws Exception {


        assertThat(CampanhaRep.buscarTodasAsCampanhasAtivas(LocalDate.of(2018, 02, 20)))
                .as("Deve Trazer somente a Campanha 1 - que esta ativa - Data fim vig�ncia em 20/02/2018")
                .hasSize(1);

        assertThat(CampanhaRep.buscarTodasAsCampanhasAtivas(LocalDate.of(2018, 03, 20)))
                .as("Deve Trazer as todas as campanhas que tem datas de fim vig�ncia inferior ao par�metro")
                .hasSize(2);
    }

    /**
     * Testa o requisito 1 -  O Sistema n�o dever� retornar campanhas que est�o com a data de vig�ncia vencidas
     * @throws Exception
     */
    @Test
    public void naoDeveTrazerCampanhasComDataDeFimVigenciaoInferiorADataRepassada() throws Exception {

        assertThat(CampanhaRep.buscarTodasAsCampanhasAtivas(LocalDate.of(2018, 01, 02)))
                .as("N�o existe campanhas ativas com essa data de in�cio de vig�ncia!")
                .isNullOrEmpty();


        assertThat(CampanhaRep.buscarTodasAsCampanhasAtivas(LocalDate.of(2018, 04, 05)))
                .as("N�o existe campanhas ativas com essa data de fim de vig�ncia!")
                .isNullOrEmpty();
    }

    /**
     * Faz parte o requisito 2 - trazer as campanhas ativas em um determinado per�odo
     * @throws Exception
     */
    @Test
    public void deveTrazerCampanhasAtivasPorPeriodo() throws Exception {


        assertThat(CampanhaRep.buscarCampanhasAtivasPorPeriodo(LocalDate.of(2018, 03, 01), LocalDate.of(2018, 04, 01)))
                .as("Deve trazer somente uma Campanha que est� dentro da vig�ncia deste per�odo")
                .hasSize(1);

        assertThat(CampanhaRep.buscarCampanhasAtivasPorPeriodo(LocalDate.of(2018, 03, 01), LocalDate.of(2018, 05, 01)))
                .as("Deve trazer somente uma Campanha que est� dentro da vig�ncia deste per�odo")
                .hasSize(2);

        assertThat(CampanhaRep.buscarCampanhasAtivasPorPeriodo(LocalDate.of(2018, 03, 01), LocalDate.of(2018, 06, 01)))
                .as("Deve trazer somente uma Campanha que est� dentro da vig�ncia deste per�odo")
                .hasSize(2);
    }

    /**
     * Faz parte o requisito 2 - trazer as campanhas ativas no per�odo
     * @throws Exception
     */
    @Test
    public void naoDeveTrazerCampanhasQuandoPeriodoEstiverForaDaDataDeVigencia() throws Exception {

        assertThat(CampanhaRep.buscarCampanhasAtivasPorPeriodo(LocalDate.of(2018, 01, 01), LocalDate.of(2018, 01, 01)))
                .as("N�o existe nenhuma campanha vig�nte neste per�odo")
                .isEmpty();

    }

    /**
     * Buscando time-1001
     * @throws Exception
     */
    @Test
    
    public void buscaPorTimeId() throws Exception {
        assertThat(CampanhaRep.buscaTimeId("TIME-1002", LocalDate.of(2018,03,21),LocalDate.of(2018,04,20)))
                .as("Deve encontrar uma Campanha com o Time TIME-1002")
                .hasSize(1);
    }
}
