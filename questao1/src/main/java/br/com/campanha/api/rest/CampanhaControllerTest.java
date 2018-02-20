package br.com.campanha.api.rest;

import br.com.campanha.api.domain.CampanhaRes;
import br.com.campanha.domain.Campanha;
import br.com.campanha.fixture.CampanhaGenerator;
import br.com.campanha.repository.CampanhaRep;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.tuple;

/**
 * @author : Ana Paula  anapaulasilva1000@gmail.com
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CampanhaControllerTest {

    @Autowired
    private CampanhaController campanhaController;

    @Autowired
    private CampanhaRep campanhaRepository;

    @Before
    public void setUp() throws Exception {
        campanhaRepository.deleteAll();
        campanhaRepository.save((Campanha)CampanhaGenerator.getCampanhas());
        HttpServletRequest mockRequest = new MockHttpServletRequest();
        ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
    }

    @Test
    public void cadastroCampanhaTest() throws Exception {
        ResponseEntity<?> responseEntity = campanhaController.cadastrarCampanha(CampanhaGenerator.criarCampanhaVigencia());
        assertThat(responseEntity).as("Campanha deve ser criada com sucesso!").isNotNull();
        assertThat(responseEntity.getStatusCode()).as("O Status deve ser criado com sucesso!").isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders().get("location")).as("Essa é a URL da campanha criada").isNotNull();
    }

    @Test
    public void naoTemCampanhaAtiva() throws Exception {
        ResponseEntity<List<CampanhaRes>> campanhas = campanhaController.buscarTodasCampanhas();
        assertThat(campanhas.getBody()).as("Não existe nenhuma campanha ativa nesta data.").isEmpty();
    }

    @Test
    public void temCampanhaAtiva() throws Exception {
        campanhaRepository.save((Campanha)CampanhaGenerator.getCampanhasAtivas());
        ResponseEntity<List<CampanhaRes>> campanhas = campanhaController.buscarTodasCampanhas();
        assertThat(campanhas.getBody()).as("Deve retornar as 4 campanhas ativas").hasSize(4)
                .extracting("nome", "timeId", "inicioVigencia", "fimVigencia")
                .contains(tuple("Campanha 10", "TIME-1001", LocalDate.now(), LocalDate.now().plusDays(3)),
                		tuple("Campanha 12", "TIME-1001", LocalDate.now(), LocalDate.now().plusDays(4)),
                		tuple("Campanha 15", "TIME-1001", LocalDate.now(), LocalDate.now().plusDays(5)),
                        tuple("Campanha 20", "TIME-1001", LocalDate.now(), LocalDate.now().plusDays(6)));
    }


    @Test
    public void buscarPorId() throws Exception {
        Campanha campanha = campanhaRepository.findAll().stream().findAny().get();
        final ResponseEntity<CampanhaRes> responseEntity = campanhaController.buscarPorId(campanha.getId());
        assertThat(responseEntity.getStatusCode()).as("O Status deve ser OK").isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).as("Deve retornar a Campanha")
                .isNotNull()
                .hasNoNullFieldsOrProperties();
    }

    @Test
    public void deletarPorId() throws Exception {
        Campanha campanha = campanhaRepository.findAll().stream().findAny().get();
        final ResponseEntity<?> responseEntity = campanhaController.deletarPorId(campanha.getId());
        assertThat(responseEntity.getStatusCode()).as("O Status deve ser NO_CONTENT").isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(campanhaRepository.findById(campanha.getId())).as("A campanha vai ser excluída").isNull();
    }

    @Test
    public void atualizarCampanha() throws Exception {
        Campanha campanha = campanhaRepository.findAll().stream().findAny().get();
        final ResponseEntity<?> responseEntity = campanhaController.atualizarCampanha(campanha.getId(), CampanhaGenerator.criarCampanhaVigencia());
        assertThat(responseEntity.getStatusCode()).as("O Status deve ser NO_CONTENT").isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(campanhaRepository.findOne(campanha.getId()))
                .as("Os dados da campanha vão ser atualizados")
                .extracting("nome", "timeId", "inicioVigencia", "fimVigencia")
                .contains("Campanha 4", "TIME-1004", LocalDate.of(2018,02,20),LocalDate.of(2018,02,20));
    }


    @Test
    public void handleValidationException() throws Exception {
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> { campanhaController.cadastrarCampanha(CampanhaGenerator.criarCampanhaComDadosFaltando()); })
                .withNoCause();
    }

    @Test
    public void vamosEncontrarCampanhaPorTime() throws Exception{
        campanhaRepository.save((Campanha)CampanhaGenerator.getCampanhasAtivas());
        final ResponseEntity<List<CampanhaRes>> responseEntity = campanhaController.buscaPorTime("TIME-1001");

        assertThat(responseEntity.getStatusCode()).as("O Status deve ser OK").isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).as("Retornar a listas das campanhas ativas para o TIME-1001 - 4 Campanhas")
                .hasSize(2);
    }

}
