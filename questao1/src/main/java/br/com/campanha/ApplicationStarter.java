package br.com.campanha;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.init.AbstractRepositoryPopulatorFactoryBean;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author : Ana Paula  anapaulasilva1000@gmail.com
 */

@EnableFeignClients
@EnableHystrix
@SpringBootApplication
@EnableAutoConfiguration
@EnableMongoRepositories(basePackages = {"br.com.campanha.repository","br.com.campanha.webhook.repository"})
public class ApplicationStarter {

    public static void main(String[] args) throws Exception {
    }

    /**
     * Responsável por ler o arquivo do resources (esses dados estão no arquivo dados.json)
     * e carregar para o MongoDB
     */
    @Bean
    public AbstractRepositoryPopulatorFactoryBean repositoryPopulator() {
    	Jackson2RepositoryPopulatorFactoryBean factoryBean = new Jackson2RepositoryPopulatorFactoryBean();
        ObjectMapper mapper = new ObjectMapper();
        
        mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        factoryBean.setResources(new Resource[] { new ClassPathResource("data.json") });
        factoryBean.setMapper(mapper);

        return factoryBean;
    }

}