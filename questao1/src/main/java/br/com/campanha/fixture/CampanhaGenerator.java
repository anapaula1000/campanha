package br.com.campanha.fixture;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.campanha.api.domain.CampanhaRes;
import br.com.campanha.domain.Campanha;

/**
 * Massa de teste
 *
 * @author : Ana Paula  anapaulasilva1000@gmail.com
 */
public class CampanhaGenerator {

    public static List<Campanha> getCampanhas(){
        List<Campanha> campanhas = new ArrayList<Campanha>();
        campanhas.add(new Campanha("Campanha 1",  "TIME-1001", LocalDate.of(2018,02,21),LocalDate.of(2018,03,20)));
        campanhas.add(new Campanha("Campanha 2",  "TIME-1002", LocalDate.of(2018,03,21),LocalDate.of(2018,04,20)));
        campanhas.add(new Campanha("Campanha 3",  "TIME-1003", LocalDate.of(2018,04,21),LocalDate.of(2018,05,20)));
        campanhas.add(new Campanha("Campanha 4",  "TIME-1004", LocalDate.of(2018,05,21),LocalDate.of(2018,06,20)));
        return campanhas;
    }

    public static List<Campanha> getCampanhasAtivas(){
        List<Campanha> campanhas = new ArrayList<Campanha>();
        campanhas.add(new Campanha("Campanha 10",  "TIME-1001", LocalDate.now(), LocalDate.now().plusDays(3)));
        campanhas.add(new Campanha("Campanha 12",  "TIME-1001", LocalDate.now(), LocalDate.now().plusDays(4)));
        campanhas.add(new Campanha("Campanha 15",  "TIME-1001", LocalDate.now(), LocalDate.now().plusDays(5)));
        campanhas.add(new Campanha("Campanha 20",  "TIME-1001", LocalDate.now(), LocalDate.now().plusDays(6)));
        return campanhas;
    }

    public static CampanhaRes criarCampanhaVigencia(){
        return new CampanhaRes("Campanha 0",  "TIME-1000", LocalDate.of(2018,02,20),LocalDate.of(2018,02,28));

    }

    public static CampanhaRes criarCampanhaComDadosFaltando(){
        return new CampanhaRes(null,  "TIME-1004", LocalDate.of(2018,02,20),LocalDate.of(2018,02,20));

    }
}
