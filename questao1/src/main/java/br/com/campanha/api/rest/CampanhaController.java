package br.com.campanha.api.rest;

import br.com.campanha.api.domain.CampanhaRes;
import br.com.campanha.api.domain.ErrorInfo;
import br.com.campanha.domain.Campanha;
import br.com.campanha.exception.RecursoNaoEncontradoException;
import br.com.campanha.service.CampanhaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.created;

/**
 * @author : Ana Paula  anapaulasilva1000@gmail.com
 */
@RestController
@RequestMapping("/v1/campanhas")
@Api(value = "Campanha", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE,
tags = {"Endpoints da Campanha"}, description = "Aqui são tratadas todas as requisições para o serviço de campanha",
basePath = "/api/v1/campanhas")
public class CampanhaController {

    private static final Logger logger = LoggerFactory.getLogger(CampanhaController.class);

    @Autowired
    private CampanhaService campanhaService;
    
    

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    ErrorInfo handleInternalServerError(Exception ex) {
        return new ErrorInfo(ServletUriComponentsBuilder.fromCurrentRequest().path("").toUriString() , ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody ErrorInfo
    handleHttpMessageNotReadableException( HttpMessageNotReadableException ex) {
        return new ErrorInfo(ServletUriComponentsBuilder.fromCurrentRequest().path("").toUriString() ,ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody ErrorInfo
    handleValidationException( MethodArgumentNotValidException ex) {
        return new ErrorInfo(ServletUriComponentsBuilder.fromCurrentRequest().path("").toUriString() ,ex);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    @ResponseBody ErrorInfo
    handleNotFoundException( RecursoNaoEncontradoException ex) {
        return new ErrorInfo(ServletUriComponentsBuilder.fromCurrentRequest().path("").toUriString() ,ex);
    }

    
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Cria uma nova campanha com base nos parametros passados",
            notes = "Cria uma nova campanha e retorna o link.",
            response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400 , message = "Bad Request"),
            @ApiResponse(code = 500 , message = "Internal Server Error")})
    public ResponseEntity<?> cadastrarCampanha(@Valid @RequestBody CampanhaRes CampanhaRes){

        Campanha campanha = campanhaService.cadastrarCampanha(CampanhaRes.getNome(), CampanhaRes.getTimeId(),
                CampanhaRes.getInicioVigencia(), CampanhaRes.getFimVigencia());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(campanha.getId()).toUri();

        if(logger.isDebugEnabled()) {
            logger.debug("Campanha : {} criada com sucesso", campanha);
            logger.debug("O link gerado foi : {}", location);
        }

        return created(location).build();
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(value = "Busca pelas campanhas ativas",
            notes = "Para retornar as Campanhas ativas usando a data atual",
            response = CampanhaRes.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400 , message = "Bad Request"),
            @ApiResponse(code = 500 , message = "Internal Server Error")})
    public ResponseEntity<List<CampanhaRes>> buscarTodasCampanhas(){

        List<CampanhaRes> CampanhaRess = campanhaService.buscarTodasAsCampanhasAtivas(LocalDate.now()).stream()
                .map(CampanhaRes::new)
                .collect(Collectors.toList());

        CampanhaRess.forEach(CampanhaRes -> {
            CampanhaRes.add(linkTo(methodOn(CampanhaController.class).buscarPorId(CampanhaRes.getChave())).withSelfRel());
        });

        if(logger.isDebugEnabled()) {
            logger.debug("Total de campanhas ativas retornadas : {} ", CampanhaRess.size());
        }

        return new ResponseEntity<>(CampanhaRess, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(value = "Busca a campanha por id",
            notes = "Retorna a campanha por ID",
            response = CampanhaRes.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400 , message = "Bad Request"),
            @ApiResponse(code = 500 , message = "Internal Server Error")})
    public ResponseEntity<CampanhaRes> buscarPorId(@PathVariable String id){
        Optional<Campanha> campanhaOptional = campanhaService.buscarPorId(id);

        if(campanhaOptional.isPresent()) {
            CampanhaRes CampanhaRes = new CampanhaRes(campanhaOptional.get());
            CampanhaRes.add(linkTo(methodOn(CampanhaController.class).buscarPorId(CampanhaRes.getChave())).withSelfRel());
            return new ResponseEntity<>(CampanhaRes, HttpStatus.OK);
        }

        if(logger.isDebugEnabled()) {
            logger.debug("A campanha com ID: {} não foi encontrada", id);
        }

        throw new RecursoNaoEncontradoException();
    }


    @GetMapping(value = "/time/{time}", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(value = "Busca campanha por time",
            notes = "Retorna a campanha por time ativa nesta data",
            response = CampanhaRes.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400 , message = "Bad Request"),
            @ApiResponse(code = 500 , message = "Internal Server Error")})
    public ResponseEntity<List<CampanhaRes>> buscaPorTime(@PathVariable String time){
        List<CampanhaRes> CampanhaResultado = campanhaService.buscaPorTime(time).stream()
                .map(CampanhaRes::new)
                .collect(Collectors.toList());

        CampanhaResultado.forEach(CampanhaRes -> {
            CampanhaRes.add(linkTo(methodOn(CampanhaController.class).buscarPorId(CampanhaRes.getChave())).withSelfRel());
        });

        if(logger.isDebugEnabled()) {
            logger.debug("Quantidade de campanhas retornadas : {} ", CampanhaResultado.size());
        }

        return new ResponseEntity<>(CampanhaResultado, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "Exclui a campanha por id",
            notes = "Exclui a campanha por ID",
            response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400 , message = "Bad Request"),
            @ApiResponse(code = 500 , message = "Internal Server Error")})
    public ResponseEntity<?> deletarPorId(@PathVariable String id){

        if(logger.isDebugEnabled()) {
            logger.debug("Excluindo a campanha com ID: {}", id);
        }
        campanhaService.deletarPorId(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(value = "Atualiza a campanha por id",
            notes = "Atualiza a campanha por ID",
            response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400 , message = "Bad Request"),
            @ApiResponse(code = 500 , message = "Internal Server Error")})
    public ResponseEntity<?> atualizarCampanha(@PathVariable String id , @Valid @RequestBody CampanhaRes CampanhaRes){

        Optional<Campanha> campanhaOptional = campanhaService.buscarPorId(id);

        campanhaOptional.ifPresent(campanha -> {
            if(logger.isDebugEnabled()) {
                logger.debug("Atualizando a Campanha : {}", campanha);
            }
            campanha.atualizarDados(CampanhaRes);
            campanhaService.salvarCampanha(campanha);
        });

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}